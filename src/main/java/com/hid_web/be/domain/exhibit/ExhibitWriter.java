package com.hid_web.be.domain.exhibit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.hid_web.be.storage.exhibit.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// Implement Layer의 Exhibit에 대해 DB Create, Update, Delete를 담당하는 구현체
@Component
@RequiredArgsConstructor
public class ExhibitWriter {
    private final ExhibitRepository exhibitRepository;

    public ExhibitEntity createExhibit(String exhibitUUID,
                                       String mainImgUrl,
                                       List<ExhibitSubImg> subImgs,
                                       List<ExhibitDetailImg> detailImgs,
                                       ExhibitDetail details,
                                       List<ExhibitArtist> artists) {
        // 전시 엔티티 생성
        ExhibitEntity exhibitEntity = new ExhibitEntity();
        // 전시 고유 UUID 저장
        exhibitEntity.setExhibitUUID(exhibitUUID);
        // 메인 이미지 Urls 엔티티에 저장
        exhibitEntity.setMainImgUrl(mainImgUrl);

        // 부가 이미지 Urls 엔티티에 저장
        List<ExhibitSubImgEntity> additionalImageEntities = new ArrayList<>();
        for (ExhibitSubImg additionalThumbnailImage : subImgs) {
            ExhibitSubImgEntity additionalImageEntity = new ExhibitSubImgEntity();
            additionalImageEntity.setPosition(additionalThumbnailImage.getPosition());
            additionalImageEntity.setSubImgUrl(additionalThumbnailImage.getUrl());
            additionalImageEntities.add(additionalImageEntity);
        }
        exhibitEntity.setSubImgEntities(additionalImageEntities);

        // 상세 이미지 Urls 엔티티에 저장
        List<ExhibitDetailImgEntity> detailImageEntities = new ArrayList<>();
        for (ExhibitDetailImg detailImage : detailImgs) {
            ExhibitDetailImgEntity detailImageEntity = new ExhibitDetailImgEntity();
            detailImageEntity.setPosition(detailImage.getPosition());
            detailImageEntity.setDetailImgUrl(detailImage.getUrl());
            detailImageEntities.add(detailImageEntity);
        }
        exhibitEntity.setDetailImgEntities(detailImageEntities);

        // 상세 Texts 엔티티에 저장
        exhibitEntity.setExhibitType(details.getExhibitType());
        exhibitEntity.setYear(details.getYear());
        exhibitEntity.setMajor(details.getMajor());
        exhibitEntity.setClub(details.getClub());
        exhibitEntity.setTitleKo(details.getTitleKo());
        exhibitEntity.setTitleEn(details.getTitleEn());
        exhibitEntity.setSubTitleKo(details.getSubTitleKo());
        exhibitEntity.setSubTitleEn(details.getSubTitleEn());
        exhibitEntity.setTextKo(details.getTextKo());
        exhibitEntity.setTextEn(details.getTextEn());
        exhibitEntity.setVideoUrl(details.getVideoUrl());

        // 전시 Artists 엔티티에 저장
        List<ExhibitArtistEntity> exhibitArtistEntityList = new ArrayList<>();
        for (ExhibitArtist artist : artists) {
            ExhibitArtistEntity artistEntity = new ExhibitArtistEntity();
            artistEntity.setArtistUUID(UUID.randomUUID().toString());
            artistEntity.setNameKo(artist.getNameKo());
            artistEntity.setNameEn(artist.getNameEn());
            artistEntity.setRole(artist.getRole());
            artistEntity.setEmail(artist.getEmail());
            artistEntity.setInstagramUrl(artist.getInstagramUrl());
            artistEntity.setBehanceUrl(artist.getBehanceUrl());
            artistEntity.setLinkedinUrl(artist.getLinkedinUrl());
            artistEntity.setProfileImgUrl(artist.getProfileImgUrl());

            exhibitArtistEntityList.add(artistEntity);
        }
        exhibitEntity.setArtistEntities(exhibitArtistEntityList);

        // Spring Data JPA를 이용하여 DB에 저장
        return exhibitRepository.save(exhibitEntity);
    }

    public void deleteExhibit(Long exhibitId) {
        exhibitRepository.deleteById(exhibitId);
    }

}



