package com.hid_web.be.service;

import com.hid_web.be.domain.exhibit.ExhibitArtistEntity;
import com.hid_web.be.domain.exhibit.ExhibitEntity;
import com.hid_web.be.repository.ExhibitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExhibitService {
    private final ExhibitRepository exhibitRepository;

    public List<ExhibitEntity> findAllExhibit() {
        return exhibitRepository.findAll();
    }

    public ExhibitEntity findExhibitByExhibitId(Long exhibitId) {
        return exhibitRepository.findExhibitByExhibitId(exhibitId);
    }

    public ExhibitEntity createExhibit(String thumbnailUrl) {
        ExhibitEntity exhibitEntity = new ExhibitEntity();
        List<ExhibitArtistEntity> exhibitArtistEntities = new ArrayList<>();

        ExhibitArtistEntity exhibitArtistEntityKZ = new ExhibitArtistEntity();

        exhibitArtistEntityKZ.setName("테스트 디자이너 A");
        exhibitArtistEntities.add(exhibitArtistEntityKZ);

        ExhibitArtistEntity exhibitArtistEntityBM = new ExhibitArtistEntity();
        exhibitArtistEntityBM.setName("테스트 디자이너 B");
        exhibitArtistEntities.add(exhibitArtistEntityBM);

        exhibitEntity.setExhibitArtistEntityList(exhibitArtistEntities);

        exhibitEntity.setTitle("테스트 제목");
        exhibitEntity.setSubtitle("테스트 부제");
        exhibitEntity.setThumbnailUrl(thumbnailUrl);
        exhibitEntity.setText("테스트 설명");
        exhibitEntity.setImageUrl("테스트 이미지");
        exhibitEntity.setVideoUrl("테스트 영상");

        return exhibitRepository.save(exhibitEntity);
    }
}