package com.hid_web.be.domain.exhibit;

import com.hid_web.be.domain.s3.S3UrlConverter;
import com.hid_web.be.domain.s3.S3Writer;
import com.hid_web.be.storage.exhibit.ExhibitArtistEntity;
import com.hid_web.be.storage.exhibit.ExhibitDetailImgEntity;
import com.hid_web.be.storage.exhibit.ExhibitEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExhibitService {
    private final S3Writer s3Writer;
    private final ExhibitWriter exhibitWriter;
    private final ExhibitReader exhibitReader;
    private final ExhibitExtractor exhibitExtractor;

    public List<ExhibitEntity> findExhibitsByTypeYearAndTerm(ExhibitType exhibitType, String year, String term) {
        if ("ALL".equals(term)) {
            return exhibitReader.findByTypeAndYear(exhibitType, year);
        }

        if (exhibitType == ExhibitType.CLUB) {
            return exhibitReader.findByTypeAndYearAndClub(exhibitType, year, term);
        } else {
            return exhibitReader.findByTypeAndYearAndMajor(exhibitType, year, term);
        }
    }

    public ExhibitEntity findExhibitByExhibitId(Long exhibitId) {
        return exhibitReader.findByExhibitId(exhibitId);
    }

    public ExhibitEntity createExhibit(MultipartFile mainImgFile,
                                       List<ExhibitDetailImg> detailImgs,
                                       ExhibitDetail details,
                                       List<ExhibitArtist> artists) throws IOException {
        // 전시 고유 UUID
        String exhibitUUID = UUID.randomUUID().toString();

        // 메인 이미지 S3 저장
        String mainImgUrl = s3Writer.writeFile(mainImgFile, exhibitUUID + "/main-image");

        // 상세 이미지 S3 저장
        for (ExhibitDetailImg detailImg : detailImgs) {
            String detailImageUrl = s3Writer.writeFile(detailImg.getFile(), exhibitUUID + "/detail-images");
            detailImg.setUrl(detailImageUrl);
        }

        // 전시 아티스트 프로필 이미지 S3 저장
        for (ExhibitArtist artist : artists) {
                String profileImgUrl = s3Writer.writeFile(artist.getProfileImgFile(), exhibitUUID + "/profile-images");
                artist.setProfileImgUrl(profileImgUrl);
        }

        // ExhibitWriter로 DB 저장 처리
        ExhibitEntity exhibitEntity = exhibitWriter.createExhibit(
                exhibitUUID,
                mainImgUrl,
                detailImgs,
                details,
                artists
        );

        return exhibitEntity;
    }

    @Transactional
    public ExhibitEntity updateExhibit(Long exhibitId,
                                       MultipartFile mainImgFile,
                                       List<ExhibitDetailImg> detailImgs,
                                       ExhibitDetail details,
                                       List<ExhibitArtist> artists) throws IOException {
        ExhibitEntity exhibitEntity = exhibitReader.findByExhibitId(exhibitId);

        // 메인 이미지 Update
        if (mainImgFile != null) {
            String mainImgObjectKey = s3Writer.writeFile(mainImgFile, exhibitEntity.getExhibitUUID() + "/main-image");
            exhibitEntity.setMainImgObjectKey(mainImgObjectKey);
        }

        // 상세 이미지 Update
        if (detailImgs != null) {
            // 기존 이미지 Entity 리스트를 키가 Url인 HashMap으로 변환
            Map<String, ExhibitDetailImgEntity> detailImgMapByObjectKey = exhibitExtractor.extractDetailImgMapByObjectKey(exhibitEntity);

            // 기존 Url Set, 수정 후 Url Set을 가지는 UrlState 생성
            Set<String> currentObjectKeys = detailImgMapByObjectKey.keySet();
            ExhibitObjectKeyState detailImgsObjectKeyState = new ExhibitObjectKeyState(currentObjectKeys);

            // 수정 후 유효한 이미지가 하나도 없을 경우 기존 이미지 전체 삭제
            /*if (detailImgs.isEmpty()) {
                // S3에서 삭제
                for (String url : currentUrls) {
                    s3Writer.deleteObject(url); // S3에서 삭제
                }

                // DB에서 삭제 - 기존 이미지 Entity 리스트에서 삭제 대상 제거하여 JPA의 더티 체킹으로 처리
                exhibitEntity.getDetailImgEntities().clear();
            }*/

            for (ExhibitDetailImg detailImg : detailImgs) {
                switch (detailImg.getType()) {
                    // S3에 파일 업로드 (file 존재, url = null)
                    case FILE:
                        String uploadedObjectKey = s3Writer.writeFile(detailImg.getFile(), exhibitEntity.getExhibitUUID() + "/detail-images");

                        // 수정 후 Object Key Set에 추가
                        detailImgsObjectKeyState.getUpdatedObjectKeys().add(uploadedObjectKey);

                        // 새로운 Entity 생성 후 기존 이미지 Entity 리스트에 추가
                        ExhibitDetailImgEntity createdEntity = new ExhibitDetailImgEntity(null, uploadedObjectKey, detailImg.getPosition());
                        exhibitEntity.getDetailImgEntities().add(createdEntity);

                        break;

                    // 파일 업로드가 아닌 기존 이미지에 대한 처리 (file = null, url 존재)
                    case URL:
                        // 유효한 Url Set에 추가
                        detailImgsObjectKeyState.getUpdatedObjectKeys().add(S3UrlConverter.convertObjectKeyFromUrl(detailImg.getUrl()));

                        // 기존에 존재하는 엔티티이므로 해시 맵에서 꺼내, 포지션 업데이트
                        ExhibitDetailImgEntity updatedEntity = detailImgMapByObjectKey.get(S3UrlConverter.convertObjectKeyFromUrl(detailImg.getUrl()));
                        updatedEntity.setPosition(detailImg.getPosition());
                }
            }

            // 기존 Url Set, 수정 후 Url Set을 비교하여 삭제할 Url Set 반환
            Set<String> deletedObjectKeys = detailImgsObjectKeyState.extractDeletedObjectKeys();

            // S3에서 삭제
            for (String objectKey : deletedObjectKeys) {
                s3Writer.deleteObject(objectKey); // S3에서 삭제
            }

            // 기존 이미지 Entity 리스트에서 삭제 대상 제거
            exhibitEntity.getDetailImgEntities().removeIf(
                    e -> deletedObjectKeys.contains(e.getDetailImgObjectKey())
            );
        }

        // 전시 상세 텍스트 Update
        if (details != null) {
            /*
            if (details.getType() != null) {
                exhibitEntity.setType(details.getType());
            }
            if (details.getYear() != null) {
                exhibitEntity.setYear(details.getYear());
            }
            if (details.getMajor() != null) {
                exhibitEntity.setMajor(details.getMajor());
            )
            if (details.getClub() != null) {
                exhibitEntity.setClub(details.getClub());
            }
            */
            if (details.getBehanceUrl() != null) {
                exhibitEntity.setBehanceUrl(details.getBehanceUrl());
            }
            if (details.getInstagramUrl() != null) {
                exhibitEntity.setInstagramUrl(details.getInstagramUrl());
            }
            if (details.getTitleKo() != null) {
                exhibitEntity.setTitleKo(details.getTitleKo());
            }
            if (details.getTitleEn() != null) {
                exhibitEntity.setTitleEn(details.getTitleEn());
            }
            if (details.getSubTitleKo() != null) {
                exhibitEntity.setSubTitleKo(details.getSubTitleKo());
            }
            if (details.getSubTitleEn() != null) {
                exhibitEntity.setSubTitleEn(details.getSubTitleEn());
            }
            if (details.getDescriptionKo() != null) {
                exhibitEntity.setDescriptionKo(details.getDescriptionKo());
            }
            if (details.getDescriptionEn() != null) {
                exhibitEntity.setDescriptionEn(details.getDescriptionEn());
            }
            if (details.getVideoUrl() != null) {
                exhibitEntity.setVideoUrl(details.getVideoUrl());
            }
        }

        // 전시 아티스트 Update - UUID로 엔티티 식별
        if (artists != null) {
            // 기존 아티스트 리스트를 키가 UUID인 HashMap으로 변환
            Map<String, ExhibitArtistEntity> artistMapByUUID = exhibitExtractor.extractArtistMapByUUID(exhibitEntity.getArtistEntities());

            Set<String> currentUUIDs = artistMapByUUID.keySet();
            ExhibitUUIDState artistUUIDState = new ExhibitUUIDState(currentUUIDs);

            // 수정 후 아티스트가 하나도 없을 경우 기존 이미지 전체 삭제
            /*if (exhibitArtists.isEmpty()) {
                // S3에서 삭제
                for (String uuid : currentUUIDs) {
                    s3Writer.deleteObject(artistMapByUUID.get(uuid).getProfileImageUrl()); // S3에서 삭제
                }

                // DB에서 삭제 - 기존 이미지 Entity 리스트에서 삭제 대상 제거하여 JPA의 더티 체킹으로 처리
                exhibitEntity.getExhibitArtistEntityList().clear();
            }*/

            // 수정 후 아티스트 UUID 목록을 기반으로 업데이트 또는 추가 처리
            for (ExhibitArtist artist : artists) {
                ExhibitArtistEntity artistEntity = artistMapByUUID.get(artist.getArtistUUID());

                switch (artist.getType()) {
                    // S3에 파일 업로드 (file 존재, url = null)
                    case FILE:
                        String uploadedObjectKey = s3Writer.writeFile(artist.getProfileImgFile(), exhibitEntity.getExhibitUUID() + "/profile-images");

                        // 새로운 학생을 추가한 후 프로필 사진에 프로필 이미지 업로드하는 경우
                        if (artistEntity == null) {
                            // 새로운 아티스트 추가
                            artistEntity = new ExhibitArtistEntity(null, UUID.randomUUID().toString(), uploadedObjectKey);
                            // 새로운 Entity 생성 후 기존 이미지 Entity 리스트에 추가
                            exhibitEntity.getArtistEntities().add(artistEntity);
                        }

                        // 기존 학생의 프로필 사진을 다시 업로드하는 경우
                        artistEntity.setProfileImgObjectKey(uploadedObjectKey);
                        artistUUIDState.getUpdatedUUIDs().add(artistEntity.getArtistUUID());

                        // 수정 후 UUID Set에 추가
                        artistUUIDState.getUpdatedUUIDs().add(artistEntity.getArtistUUID());

                        break;

                    // 파일 업로드가 아닌 기존 이미지에 대한 처리 (file = null, url 존재)
                    case URL:
                        // 유효한 UUID Set에 추가
                        artistUUIDState.getUpdatedUUIDs().add(artist.getArtistUUID());
                }

                // 텍스트 정보 수정
                if (artist.getNameKo() != null) {
                    artistEntity.setNameKo(artist.getNameKo());
                }
                if (artist.getNameEn() != null) {
                    artistEntity.setNameEn(artist.getNameEn());
                }
                if (artist.getRole() != null) {
                    artistEntity.setRole(artist.getRole());
                }
                if (artist.getEmail() != null) {
                    artistEntity.setEmail(artist.getEmail());
                }
                if (artist.getInstagramUrl() != null) {
                    artistEntity.setInstagramUrl(artist.getInstagramUrl());
                }
                if (artist.getBehanceUrl() != null) {
                    artistEntity.setBehanceUrl(artist.getBehanceUrl());
                }
                if (artist.getLinkedinUrl() != null) {
                    artistEntity.setLinkedinUrl(artist.getLinkedinUrl());
                }
            }

            Set<String> deletedUUIDs = artistUUIDState.extractDeletedUUIDs();

            // S3에서 삭제
            for (String uuid : deletedUUIDs) {
                s3Writer.deleteObject(artistMapByUUID.get(uuid).getProfileImgObjectKey()); // S3에서 삭제
            }

            // DB에서 삭제 - 기존 이미지 Entity 리스트에서 삭제 대상 제거하여 JPA의 더티 체킹으로 처리
            exhibitEntity.getArtistEntities().removeIf(
                    artist -> deletedUUIDs.contains(artist.getArtistUUID())
            );
        }

        return exhibitEntity;
    }

    public void deleteExhibit(Long exhibitId) {
        ExhibitEntity exhibitEntity = exhibitReader.findByExhibitId(exhibitId);;

        s3Writer.deleteObjects(exhibitEntity.getExhibitUUID());
        exhibitWriter.deleteExhibit(exhibitId);
    }

    public List<ExhibitEntity> searchExhibits(
            String searchTerm,
            ExhibitType exhibitType,
            String year,
            SearchType searchType
    ) {
        switch (searchType) {
            case ARTIST:
                return exhibitReader.searchByArtistName(searchTerm, exhibitType, year);
            case TITLE:
                return exhibitReader.searchByTitle(searchTerm, exhibitType, year);
            default:
                throw new IllegalArgumentException("유효하지 않은 검색 타입입니다: " + searchType);
        }
    }

}

