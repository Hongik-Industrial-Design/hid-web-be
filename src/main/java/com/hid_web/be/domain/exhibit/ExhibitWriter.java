package com.hid_web.be.domain.exhibit;

import com.hid_web.be.storage.exhibit.ExhibitArtistEntity;
import com.hid_web.be.storage.exhibit.ExhibitDetailImgEntity;
import com.hid_web.be.storage.exhibit.ExhibitEntity;
import com.hid_web.be.storage.exhibit.ExhibitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Implement Layer의 Exhibit에 대해 DB Create, Update, Delete를 담당하는 구현체
@Component
@RequiredArgsConstructor
public class ExhibitWriter {
    private final ExhibitRepository exhibitRepository;

    public ExhibitEntity createExhibit(String exhibitUUID,
                                       String mainImgUrl,
                                       List<ExhibitDetailImg> detailImgs,
                                       ExhibitDetail details,
                                       List<ExhibitArtist> artists) {
        // 전시 엔티티 생성
        ExhibitEntity exhibitEntity = new ExhibitEntity();
        // 전시 고유 UUID 저장
        exhibitEntity.setExhibitUUID(exhibitUUID);
        // 메인 이미지 Urls 엔티티에 저장
        exhibitEntity.setMainImgObjectKey(mainImgUrl);

        // 상세 이미지 Urls 엔티티에 저장
        List<ExhibitDetailImgEntity> detailImageEntities = new ArrayList<>();
        for (ExhibitDetailImg detailImage : detailImgs) {
            ExhibitDetailImgEntity detailImageEntity = new ExhibitDetailImgEntity();
            detailImageEntity.setPosition(detailImage.getPosition());
            detailImageEntity.setDetailImgObjectKey(detailImage.getUrl());
            detailImageEntities.add(detailImageEntity);
        }
        exhibitEntity.setDetailImgEntities(detailImageEntities);

        // 상세 Texts 엔티티에 저장
        exhibitEntity.setType(details.getType());
        exhibitEntity.setYear(details.getYear());
        exhibitEntity.setMajor(details.getMajor());
        exhibitEntity.setClub(details.getClub());
        exhibitEntity.setBehanceUrl(details.getBehanceUrl());
        exhibitEntity.setInstagramUrl(details.getInstagramUrl());
        exhibitEntity.setTitleKo(details.getTitleKo());
        exhibitEntity.setTitleEn(details.getTitleEn());
        exhibitEntity.setSubTitleKo(details.getSubTitleKo());
        exhibitEntity.setSubTitleEn(details.getSubTitleEn());
        exhibitEntity.setDescriptionKo(details.getDescriptionKo());
        exhibitEntity.setDescriptionEn(details.getDescriptionEn());
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
            artistEntity.setProfileImgObjectKey(artist.getProfileImgUrl());

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



