package com.hid_web.be.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.hid_web.be.repository.ExhibitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hid_web.be.domain.exhibit.*;

// Implement Layer의 Exhibit에 대해 DB Create, Update, Delete를 담당하는 구현체
@Service
@RequiredArgsConstructor
public class ExhibitWriter {
    private final ExhibitRepository exhibitRepository;

    public ExhibitEntity createExhibit(String exhibitUUID,
                                       String mainThumbnailImageUrl,
                                       List<ExhibitAdditionalThumbnailImage> additionalThumbnailImages,
                                       List<ExhibitDetailImage> detailImages,
                                       ExhibitDetail exhibitDetail,
                                       List<ExhibitArtist> exhibitArtistList) {
        // 전시 엔티티 생성
        ExhibitEntity exhibitEntity = new ExhibitEntity();
        // 전시 고유 UUID 저장
        exhibitEntity.setExhibitUUID(exhibitUUID);
        // 메인 이미지 Urls 엔티티에 저장
        exhibitEntity.setMainThumbnailImageUrl(mainThumbnailImageUrl);

        // 부가 이미지 Urls 엔티티에 저장
        List<ExhibitAdditionalThumbnailEntity> additionalImageEntities = new ArrayList<>();
        for (ExhibitAdditionalThumbnailImage additionalThumbnailImage : additionalThumbnailImages) {
            ExhibitAdditionalThumbnailEntity additionalImageEntity = new ExhibitAdditionalThumbnailEntity();
            additionalImageEntity.setPosition(additionalThumbnailImage.getPosition());
            additionalImageEntity.setAdditionalThumbnailImageUrl(additionalThumbnailImage.getUrl());
            additionalImageEntities.add(additionalImageEntity);
        }
        exhibitEntity.setExhibitAdditionalThumbnailImageEntityList(additionalImageEntities);

        // 상세 이미지 Urls 엔티티에 저장
        List<ExhibitDetailImageEntity> detailImageEntities = new ArrayList<>();
        for (ExhibitDetailImage detailImage : detailImages) {
            ExhibitDetailImageEntity detailImageEntity = new ExhibitDetailImageEntity();
            detailImageEntity.setPosition(detailImage.getPosition());
            detailImageEntity.setDetailImageUrl(detailImage.getUrl());
            detailImageEntities.add(detailImageEntity);
        }
        exhibitEntity.setExhibitDetailImageEntityList(detailImageEntities);

        // 상세 Texts 엔티티에 저장
        exhibitEntity.setTitleKo(exhibitDetail.getTitleKo());
        exhibitEntity.setTitleEn(exhibitDetail.getTitleEn());
        exhibitEntity.setSubtitleKo(exhibitDetail.getSubtitleKo());
        exhibitEntity.setSubtitleEn(exhibitDetail.getSubtitleEn());
        exhibitEntity.setTextKo(exhibitDetail.getTextKo());
        exhibitEntity.setTextEn(exhibitDetail.getTextEn());
        exhibitEntity.setVideoUrl(exhibitDetail.getVideoUrl());

        // 전시 Artists 엔티티에 저장
        List<ExhibitArtistEntity> exhibitArtistEntityList = new ArrayList<>();
        for (ExhibitArtist artist : exhibitArtistList) {
            ExhibitArtistEntity artistEntity = new ExhibitArtistEntity();
            artistEntity.setArtistUUID(UUID.randomUUID().toString());
            artistEntity.setArtistNameKo(artist.getArtistNameKo());
            artistEntity.setArtistNameEn(artist.getArtistNameEn());
            artistEntity.setRole(artist.getRole());
            artistEntity.setEmail(artist.getEmail());
            artistEntity.setInstagramUrl(artist.getInstagramUrl());
            artistEntity.setBehanceUrl(artist.getBehanceUrl());
            artistEntity.setLinkedinUrl(artist.getLinkedinUrl());
            artistEntity.setProfileImageUrl(artist.getProfileImageFileUrl());

            exhibitArtistEntityList.add(artistEntity);
        }
        exhibitEntity.setExhibitArtistEntityList(exhibitArtistEntityList);

        // Spring Data JPA를 이용하여 DB에 저장
        return exhibitRepository.save(exhibitEntity);
    }

    public void deleteExhibit(Long exhibitId) {
        exhibitRepository.deleteById(exhibitId);
    }

}



