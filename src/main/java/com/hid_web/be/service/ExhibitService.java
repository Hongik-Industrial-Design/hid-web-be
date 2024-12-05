package com.hid_web.be.service;

import java.io.IOException;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.hid_web.be.domain.exhibit.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExhibitService {
    private final S3Writer s3Writer;
    private final ExhibitWriter exhibitWriter;
    private final ExhibitReader exhibitReader;
    private final ExhibitExtractor exhibitExtractor;

    public List<ExhibitEntity> findAllExhibit() {
        return exhibitReader.findAllExhibit();
    }

    public ExhibitEntity findExhibitByExhibitId(Long exhibitId) {
        return exhibitReader.findExhibitById(exhibitId);
    }

    public ExhibitEntity createExhibit(MultipartFile mainThumbnailImageFile,
                                       List<ExhibitAdditionalThumbnailImage> additionalThumbnailImages,
                                       List<ExhibitDetailImage> detailImages,
                                       ExhibitDetail exhibitDetail,
                                       List<ExhibitArtist> exhibitArtistList) throws IOException {
        // 전시 고유 UUID
        String exhibitUUID = UUID.randomUUID().toString();

        // 메인 이미지 S3 저장
        String mainThumbnailImageUrl = s3Writer.writeFileV2(mainThumbnailImageFile, exhibitUUID + "/main-thumbnail-image");

        // 부가 이미지 S3 저장
        // 필수가 아니므로 널 확인
        if (additionalThumbnailImages != null) {
            for (ExhibitAdditionalThumbnailImage additionalThumbnailImage : additionalThumbnailImages) {
                String additionalThumbnailImageUrl = s3Writer.writeFileV2(additionalThumbnailImage.getFile(), exhibitUUID + "/additional-thumbnails-images");
                additionalThumbnailImage.setUrl(additionalThumbnailImageUrl);
            }
        }

        // 상세 이미지 S3 저장
        for (ExhibitDetailImage detailImage : detailImages) {
            String detailImageUrl = s3Writer.writeFileV2(detailImage.getFile(), exhibitUUID + "/additional-thumbnails-images");
            detailImage.setUrl(detailImageUrl);
        }

        // 전시 아티스트 프로필 이미지 S3 저장
        for (ExhibitArtist artist : exhibitArtistList) {
                String profileImageUrl = s3Writer.writeFileV2(artist.getProfileImageFile(), exhibitUUID + "/profile-images");
                artist.setProfileImageFileUrl(profileImageUrl);
        }

        // ExhibitWriter로 DB 저장 처리
        ExhibitEntity exhibitEntity = exhibitWriter.createExhibit(
                exhibitUUID,
                mainThumbnailImageUrl,
                additionalThumbnailImages,
                detailImages,
                exhibitDetail,
                exhibitArtistList
        );

        return exhibitEntity;
    }

    @Transactional
    public ExhibitEntity updateExhibit(Long exhibitId,
                                       MultipartFile mainThumbnailImageFile,
                                       List<ExhibitAdditionalThumbnailImage> additionalThumbnailImages,
                                       List<ExhibitDetailImage> detailImages,
                                       ExhibitDetail exhibitDetail,
                                       List<ExhibitArtist> exhibitArtists) throws IOException {
        ExhibitEntity exhibitEntity = exhibitReader.findExhibitById(exhibitId);

        // 메인 이미지 Update
        if (mainThumbnailImageFile != null) {
            String mainThumbnailImageUrl = s3Writer.writeFileV2(mainThumbnailImageFile, exhibitEntity.getExhibitUUID() + "/main-thumbnail-image");
            exhibitEntity.setMainThumbnailImageUrl(mainThumbnailImageUrl);
        }

        // 부가 이미지 Update - Url로 엔티티 식별
        if (additionalThumbnailImages != null) {
            // 기존 이미지 Entity 리스트를 키가 Url인 HashMap으로 변환
            Map<String, ExhibitAdditionalThumbnailEntity> additionalImageMapByUrl = exhibitExtractor.extractAdditionalImageMapByUrl(exhibitEntity);

            // 기존 Url Set, 수정 후 Url Set을 가지는 UrlState 생성
            Set<String> currentUrls = additionalImageMapByUrl.keySet();
            ExhibitUrlState additionalImagesUrlState = new ExhibitUrlState(currentUrls);

            // 이 부분을 구현 Presentation Layer, Business Layer, Implement Layer, Data Access Layer 중에서 Implement Layer의 ExhibitExtractor에 구현하여 4-Tier 레이어드 아키텍처를 구현
            /*Map<String, ExhibitAdditionalThumbnailEntity> entityMap = exhibitEntity.getExhibitAdditionalThumbnailImageEntityList()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getAdditionalThumbnailImageUrl(), // 키: URL
                        e -> e // 값: 해당 엔티티 자체
                ));*/

            // 수정 후 유효한 이미지가 하나도 없을 경우 기존 이미지 전체 삭제
            /*if (additionalThumbnailImages.isEmpty()) {
                // S3에서 삭제 처리
                for (String url : currentUrls) {
                    s3Writer.deleteObject(url); // S3에서 삭제
                }

                // DB에서 삭제 처리
                exhibitEntity.getExhibitAdditionalThumbnailImageEntityList().clear();
            }*/

            for (ExhibitAdditionalThumbnailImage additionalThumbnailImage : additionalThumbnailImages) {
                switch (additionalThumbnailImage.getType()) {
                    // S3에 파일 업로드 (file 존재, url = null)
                    case FILE:
                        String uploadedUrl = s3Writer.writeFileV2(additionalThumbnailImage.getFile(), exhibitEntity.getExhibitUUID() + "/additional-thumbnails-images");

                        // 수정 후 Url Set에 추가
                        additionalImagesUrlState.getUpdatedUrls().add(uploadedUrl);

                        // 새로운 Entity 생성 후 기존 이미지 Entity 리스트에 추가
                        ExhibitAdditionalThumbnailEntity createdEntity = new ExhibitAdditionalThumbnailEntity(null, uploadedUrl, additionalThumbnailImage.getPosition());
                        exhibitEntity.getExhibitAdditionalThumbnailImageEntityList().add(createdEntity);

                        break;

                    // 파일 업로드가 아닌 기존 이미지에 대한 처리 (file = null, url 존재)
                    case URL:
                        // 유효한 Url Set에 추가
                        additionalImagesUrlState.getUpdatedUrls().add(additionalThumbnailImage.getUrl());

                        // 기존에 존재하는 엔티티이므로 해시 맵에서 꺼내, 포지션 업데이트
                        ExhibitAdditionalThumbnailEntity updatedEntity = additionalImageMapByUrl.get(additionalThumbnailImage.getUrl());
                        updatedEntity.setPosition(additionalThumbnailImage.getPosition());
                }
            }

            //기존 Url Set, 수정 후 Url Set을 비교하여 삭제할 Url Set 반환
            Set<String> deletedUrls = additionalImagesUrlState.extractDeletedUrls();

            // S3에서 삭제
            for (String url : deletedUrls) {
                s3Writer.deleteObject(url); // S3에서 삭제
            }

            // DB에서 삭제 - 기존 이미지 Entity 리스트에서 삭제 대상 제거하여 JPA의 더티 체킹으로 처리
            exhibitEntity.getExhibitAdditionalThumbnailImageEntityList().removeIf(
                    e -> deletedUrls.contains(e.getAdditionalThumbnailImageUrl())
            );
        }

        // 상세 이미지 Update - 부가 이미지 Update와 동일한 로직으로 구현
        if (detailImages != null) {
            // 기존 이미지 Entity 리스트를 키가 Url인 HashMap으로 변환
            Map<String, ExhibitDetailImageEntity> detailImageMapByUrl = exhibitExtractor.extractDetailImageMapByUrl(exhibitEntity);

            // 기존 Url Set, 수정 후 Url Set을 가지는 UrlState 생성
            Set<String> currentUrls = detailImageMapByUrl.keySet();
            ExhibitUrlState detailImagesUrlState = new ExhibitUrlState(currentUrls);

            // 수정 후 유효한 이미지가 하나도 없을 경우 기존 이미지 전체 삭제
            /*if (detailImages.isEmpty()) {
                // S3에서 삭제
                for (String url : currentUrls) {
                    s3Writer.deleteObject(url); // S3에서 삭제
                }

                // DB에서 삭제 - 기존 이미지 Entity 리스트에서 삭제 대상 제거하여 JPA의 더티 체킹으로 처리
                exhibitEntity.getExhibitDetailImageEntityList().clear();
            }*/

            for (ExhibitDetailImage detailImage : detailImages) {
                switch (detailImage.getType()) {
                    // S3에 파일 업로드 (file 존재, url = null)
                    case FILE:
                        String uploadedUrl = s3Writer.writeFileV2(detailImage.getFile(), exhibitEntity.getExhibitUUID() + "/detail-images");

                        // 수정 후 Url Set에 추가
                        detailImagesUrlState.getUpdatedUrls().add(uploadedUrl);

                        // 새로운 Entity 생성 후 기존 이미지 Entity 리스트에 추가
                        ExhibitDetailImageEntity createdEntity = new ExhibitDetailImageEntity(null, uploadedUrl, detailImage.getPosition());
                        exhibitEntity.getExhibitDetailImageEntityList().add(createdEntity);

                        break;

                    // 파일 업로드가 아닌 기존 이미지에 대한 처리 (file = null, url 존재)
                    case URL:
                        // 유효한 Url Set에 추가
                        detailImagesUrlState.getUpdatedUrls().add(detailImage.getUrl());

                        // 기존에 존재하는 엔티티이므로 해시 맵에서 꺼내, 포지션 업데이트
                        ExhibitDetailImageEntity updatedEntity = detailImageMapByUrl.get(detailImage.getUrl());
                        updatedEntity.setPosition(detailImage.getPosition());
                }
            }

            // 기존 Url Set, 수정 후 Url Set을 비교하여 삭제할 Url Set 반환
            Set<String> deletedUrls = detailImagesUrlState.extractDeletedUrls();

            // S3에서 삭제
            for (String url : deletedUrls) {
                s3Writer.deleteObject(url); // S3에서 삭제
            }

            // 기존 이미지 Entity 리스트에서 삭제 대상 제거
            exhibitEntity.getExhibitDetailImageEntityList().removeIf(
                    e -> deletedUrls.contains(e.getDetailImageUrl())
            );
        }

        // 전시 상세 텍스트 Update
        if (exhibitDetail != null) {
            if (exhibitDetail.getTitleKo() != null) {
                exhibitEntity.setTitleKo(exhibitDetail.getTitleKo());
            }
            if (exhibitDetail.getTitleEn() != null) {
                exhibitEntity.setTitleEn(exhibitDetail.getTitleEn());
            }
            if (exhibitDetail.getSubtitleKo() != null) {
                exhibitEntity.setSubtitleKo(exhibitDetail.getSubtitleKo());
            }
            if (exhibitDetail.getSubtitleEn() != null) {
                exhibitEntity.setSubtitleEn(exhibitDetail.getSubtitleEn());
            }
            if (exhibitDetail.getTextKo() != null) {
                exhibitEntity.setTextKo(exhibitDetail.getTextKo());
            }
            if (exhibitDetail.getTextEn() != null) {
                exhibitEntity.setTextEn(exhibitDetail.getTextEn());
            }
            if (exhibitDetail.getVideoUrl() != null) {
                exhibitEntity.setVideoUrl(exhibitDetail.getVideoUrl());
            }
        }

        // 전시 아티스트 Update - UUID로 엔티티 식별
        if (exhibitArtists != null) {
            // 기존 아티스트 리스트를 키가 UUID인 HashMap으로 변환
            Map<String, ExhibitArtistEntity> artistMapByUUID = exhibitExtractor.extractArtistMapByUUID(exhibitEntity.getExhibitArtistEntityList());

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
            for (ExhibitArtist artist : exhibitArtists) {
                ExhibitArtistEntity artistEntity = artistMapByUUID.get(artist.getArtistUUID());

                switch (artist.getType()) {
                    // S3에 파일 업로드 (file 존재, url = null)
                    case FILE:
                        String uploadedUrl = s3Writer.writeFileV2(artist.getProfileImageFile(), exhibitEntity.getExhibitUUID() + "/profile-images");

                        // 새로운 학생을 추가한 후 프로필 사진에 프로필 이미지 업로드하는 경우
                        if (artistEntity == null) {
                            // 새로운 아티스트 추가
                            artistEntity = new ExhibitArtistEntity(null, UUID.randomUUID().toString(), uploadedUrl);
                            // 새로운 Entity 생성 후 기존 이미지 Entity 리스트에 추가
                            exhibitEntity.getExhibitArtistEntityList().add(artistEntity);
                        }

                        // 기존 학생의 프로필 사진을 다시 업로드하는 경우
                        artistEntity.setProfileImageUrl(uploadedUrl);
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
                if (artist.getArtistNameKo() != null) {
                    artistEntity.setArtistNameKo(artist.getArtistNameKo());
                }
                if (artist.getArtistNameEn() != null) {
                    artistEntity.setArtistNameEn(artist.getArtistNameEn());
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
                s3Writer.deleteObject(artistMapByUUID.get(uuid).getProfileImageUrl()); // S3에서 삭제
            }

            // DB에서 삭제 - 기존 이미지 Entity 리스트에서 삭제 대상 제거하여 JPA의 더티 체킹으로 처리
            exhibitEntity.getExhibitArtistEntityList().removeIf(
                    artist -> deletedUUIDs.contains(artist.getArtistUUID())
            );
        }

        return exhibitEntity;
    }

    public void deleteExhibit(Long exhibitId) {
        ExhibitEntity exhibitEntity = exhibitReader.findExhibitById(exhibitId);

        s3Writer.deleteObjects(exhibitEntity.getExhibitUUID());
        exhibitWriter.deleteExhibit(exhibitId);
    }
}

