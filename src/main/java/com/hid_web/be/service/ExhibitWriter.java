package com.hid_web.be.service;

import com.hid_web.be.domain.exhibit.*;
import com.hid_web.be.repository.ExhibitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExhibitWriter {
    private final ExhibitRepository exhibitRepository;

    public ExhibitEntity createExhibit(String exhibitUUID,
                                       String mainThumbnailImageUrl,
                                       Map<Integer, String> additionalThumbnailImageMap,
                                       Map<Integer, String> detailImageMap,
                                       ExhibitDetail exhibitDetail,
                                       List<ExhibitArtist> exhibitArtistList) {

        ExhibitEntity exhibitEntity = new ExhibitEntity();
        exhibitEntity.setExhibitUUID(exhibitUUID);
        exhibitEntity.setMainThumbnailImageUrl(mainThumbnailImageUrl);

        List<ExhibitAdditionalThumbnailEntity> additionalThumbnails = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : additionalThumbnailImageMap.entrySet()) {
            ExhibitAdditionalThumbnailEntity additionalThumbnail = new ExhibitAdditionalThumbnailEntity();
            additionalThumbnail.setPosition(entry.getKey());
            additionalThumbnail.setAdditionalThumbnailImageUrl(entry.getValue());
            additionalThumbnails.add(additionalThumbnail);
        }

        List<ExhibitDetailImageEntity> detailImages = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : detailImageMap.entrySet()) {
            ExhibitDetailImageEntity detailImage = new ExhibitDetailImageEntity();
            detailImage.setPosition(entry.getKey());
            detailImage.setDetailImageUrl(entry.getValue());
            detailImages.add(detailImage);
        }

        exhibitEntity.setExhibitAdditionalThumbnailImageEntityList(additionalThumbnails);
        exhibitEntity.setExhibitDetailImageEntityList(detailImages);

        exhibitEntity.setTitleKo(exhibitDetail.getTitleKo());
        exhibitEntity.setTitleEn(exhibitDetail.getTitleEn());
        exhibitEntity.setSubtitleKo(exhibitDetail.getSubtitleKo());
        exhibitEntity.setSubtitleEn(exhibitDetail.getSubtitleEn());
        exhibitEntity.setTextKo(exhibitDetail.getTextKo());
        exhibitEntity.setTextEn(exhibitDetail.getTextEn());
        exhibitEntity.setVideoUrl(exhibitDetail.getVideoUrl());

        List<ExhibitArtistEntity> exhibitArtistEntityList = new ArrayList<>();
        for (ExhibitArtist artist : exhibitArtistList) {
            ExhibitArtistEntity artistEntity = new ExhibitArtistEntity();
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

        return exhibitRepository.save(exhibitEntity);
    }

    public void deleteExhibit(Long exhibitId) {
        exhibitRepository.deleteById(exhibitId);
    }

}



