package com.hid_web.be.service;

import com.hid_web.be.domain.exhibit.ExhibitAdditionalThumbnailEntity;
import com.hid_web.be.domain.exhibit.ExhibitArtistEntity;
import com.hid_web.be.domain.exhibit.ExhibitDetailImageEntity;
import com.hid_web.be.domain.exhibit.ExhibitEntity;
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

    public ExhibitEntity createExhibit(String mainThumbnailImageUrl,
                                       Map<Integer, String> additionalThumbnailImageMap,
                                       Map<Integer, String> detailImageMap,
                                       String title,
                                       String subtitle,
                                       String text,
                                       String imageUrl,
                                       String videoUrl,
                                       List<String> profileImageUrls,
                                       List<String> artistNames) {

        ExhibitEntity exhibitEntity = new ExhibitEntity();


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

        exhibitEntity.setMainThumbnailImageUrl(mainThumbnailImageUrl);
        exhibitEntity.setAdditionalThumbnailImages(additionalThumbnails);
        exhibitEntity.setDetailImages(detailImages);

        exhibitEntity.setTitle(title);
        exhibitEntity.setSubtitle(subtitle);
        exhibitEntity.setText(text);
        exhibitEntity.setImageUrl(imageUrl);
        exhibitEntity.setVideoUrl(videoUrl);

        List<ExhibitArtistEntity> exhibitArtistEntities = new ArrayList<>();

        for (int i = 0; i < artistNames.size(); i++) {
            ExhibitArtistEntity artistEntity = new ExhibitArtistEntity();

            artistEntity.setName(artistNames.get(i));

            if (i < profileImageUrls.size()) {
                artistEntity.setProfileImageUrl(profileImageUrls.get(i));
            }

            exhibitArtistEntities.add(artistEntity);
        }
        exhibitEntity.setExhibitArtistEntityList(exhibitArtistEntities);



        return exhibitRepository.save(exhibitEntity);
    }
}


