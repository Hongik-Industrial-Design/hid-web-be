package com.hid_web.be.service;

import com.hid_web.be.domain.exhibit.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

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
                                       List<MultipartFile> additionalThumbnailImageFiles,
                                       List<MultipartFile> detailImageFiles,
                                       ExhibitDetail exhibitDetail,
                                       List<ExhibitArtist> exhibitArtistList) throws IOException {

        String exhibitUUID = UUID.randomUUID().toString();

        String mainThumbnailImageUrl = s3Writer.writeFile(mainThumbnailImageFile, exhibitUUID + "/main-thumbnail-image");

        List<String> additionalThumbnailImageUrls = s3Writer.writeFiles(additionalThumbnailImageFiles, exhibitUUID + "/additional-thumbnails-images");
        List<String> detailImageUrls = s3Writer.writeFiles(detailImageFiles, exhibitUUID + "/detail-images");

        Map<Integer, String> additionalThumbnailImageMap = exhibitExtractor.extractUrlMapWithPosition(additionalThumbnailImageUrls);
        Map<Integer, String> detailImageMap = exhibitExtractor.extractUrlMapWithPosition(detailImageUrls);

        for (ExhibitArtist artist : exhibitArtistList) {
                String profileImageUrl = s3Writer.writeFile(artist.getProfileImageFile(), exhibitUUID + "/profile-images");
                artist.setProfileImageFileUrl(profileImageUrl);
        }

        ExhibitEntity exhibitEntity = exhibitWriter.createExhibit(
                exhibitUUID,
                mainThumbnailImageUrl,
                additionalThumbnailImageMap,
                detailImageMap,
                exhibitDetail,
                exhibitArtistList
        );

        return exhibitEntity;
    }

    @Transactional
    public ExhibitEntity updateExhibit(Long exhibitId,
                                       MultipartFile mainThumbnailImageFile,
                                       List<ExhibitAdditionalThumbnailImage> additionalThumbnailImages,
                                       ExhibitDetail exhibitDetail) throws IOException {
        ExhibitEntity exhibitEntity = exhibitReader.findExhibitById(exhibitId);

        if (mainThumbnailImageFile != null) {
            String mainThumbnailImageUrl = s3Writer.writeFile(mainThumbnailImageFile, exhibitEntity.getExhibitUUID() + "/main-thumbnail-image" + UUID.randomUUID());
            exhibitEntity.setMainThumbnailImageUrl(mainThumbnailImageUrl);
        }

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

            // 요청 내 유효한 이미지가 하나도 없을 경우 기존 이미지 전체 삭제
            if (additionalThumbnailImages.isEmpty()) {
                // S3 삭제 처리
                for (String url : currentUrls) {
                    s3Writer.deleteObject(url); // S3에서 삭제
                }

                // DB 삭제 처리
                exhibitEntity.getExhibitAdditionalThumbnailImageEntityList().clear();
            }

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

            // 기존 이미지 Entity 리스트에서 삭제 대상 제거
            exhibitEntity.getExhibitAdditionalThumbnailImageEntityList().removeIf(
                    e -> deletedUrls.contains(e.getAdditionalThumbnailImageUrl())
            );
        }

        if (exhibitDetail != null) {
            if (exhibitDetail.getTitleKo() != null) {
                exhibitEntity.setTitleKo(exhibitDetail.getTitleKo());
                System.out.println(exhibitDetail.getTextKo());
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

        return exhibitEntity;
    }

    public void deleteExhibit(Long exhibitId) {
        ExhibitEntity exhibitEntity = exhibitReader.findExhibitById(exhibitId);

        s3Writer.deleteFolder(exhibitEntity.getExhibitUUID());
        exhibitWriter.deleteExhibit(exhibitId);
    }
}

