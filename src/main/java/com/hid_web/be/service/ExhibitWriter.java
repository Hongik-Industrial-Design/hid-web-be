package com.hid_web.be.service;

import com.hid_web.be.domain.exhibit.ExhibitAdditionalThumbnailEntity;
import com.hid_web.be.domain.exhibit.ExhibitArtistEntity;
import com.hid_web.be.domain.exhibit.ExhibitDetailImageEntity;
import com.hid_web.be.domain.exhibit.ExhibitEntity;
import com.hid_web.be.repository.ExhibitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExhibitWriter {

    private final ExhibitRepository exhibitRepository;
    private final ExhibitArtistWriter exhibitArtistWriter;

    public ExhibitEntity createExhibit(String mainThumbnailImageUrl,
                                       String title,
                                       String subtitle,
                                       String text,
                                       String imageUrl,
                                       String videoUrl,
                                       List<String> additionalThumbnailImageUrls,
                                       List<String> detailImageUrls,
                                       List<String> artistNames) {

        ExhibitEntity exhibitEntity = new ExhibitEntity();

        List<ExhibitArtistEntity> exhibitArtistEntities = exhibitArtistWriter.createArtistList(artistNames);
        exhibitEntity.setExhibitArtistEntityList(exhibitArtistEntities);

        exhibitEntity.setTitle(title);
        exhibitEntity.setSubtitle(subtitle);
        exhibitEntity.setMainThumbnailImageUrl(mainThumbnailImageUrl);
        exhibitEntity.setText(text);
        exhibitEntity.setImageUrl(imageUrl);
        exhibitEntity.setVideoUrl(videoUrl);

        List<ExhibitAdditionalThumbnailEntity> additionalThumbnails = new ArrayList<>();
        for (int i = 0; i < additionalThumbnailImageUrls.size(); i++) {
            ExhibitAdditionalThumbnailEntity additionalThumbnail = new ExhibitAdditionalThumbnailEntity();
            additionalThumbnail.setAdditionalThumbnailImageUrl(additionalThumbnailImageUrls.get(i));
            additionalThumbnail.setPosition(i + 1);
            additionalThumbnails.add(additionalThumbnail);
        }
        exhibitEntity.setAdditionalThumbnails(additionalThumbnails);

        List<ExhibitDetailImageEntity> detailImages = new ArrayList<>();
        for (int i = 0; i < detailImageUrls.size(); i++) {
            ExhibitDetailImageEntity detailImage = new ExhibitDetailImageEntity();
            detailImage.setDetailImageUrl(detailImageUrls.get(i));
            detailImage.setPosition(i + 1);
            detailImages.add(detailImage);
        }
        exhibitEntity.setDetailImages(detailImages);

        return exhibitRepository.save(exhibitEntity);
    }
}

