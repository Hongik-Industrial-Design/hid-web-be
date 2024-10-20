package com.hid_web.be;

import com.hid_web.be.domain.exhibit.ExhibitEntity;
import com.hid_web.be.domain.exhibit.ExhibitArtistEntity;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            ExhibitEntity exhibitEntity = new ExhibitEntity();
            List<ExhibitArtistEntity> exhibitArtistEntities = new ArrayList<>();
            ExhibitArtistEntity exhibitArtistEntityEntityKZ = new ExhibitArtistEntity();

            exhibitArtistEntityEntityKZ.setName("Designer A");
            exhibitArtistEntities.add(exhibitArtistEntityEntityKZ);

            ExhibitArtistEntity exhibitArtistEntityEntityBM = new ExhibitArtistEntity();
            exhibitArtistEntityEntityBM.setName("Designer B");
            exhibitArtistEntities.add(exhibitArtistEntityEntityBM);

            exhibitEntity.setExhibitArtistEntityList(exhibitArtistEntities);
            exhibitEntity.setText("홍익대학교 산업디자인학과 2024 여름 전시 - 1");
            exhibitEntity.setImageUrl("전시 이미지 - 1");
            exhibitEntity.setVideoUrl("전시 영상 - 1");

            exhibitEntity.setTitle("여름 전시 제목");
            exhibitEntity.setSubtitle("여름 전시 부제");
            exhibitEntity.setMainThumbnailImageUrl("대표 이미지");

            em.persist(exhibitEntity);
        }

        public void dbInit2() {
            ExhibitEntity exhibitEntity = new ExhibitEntity();
            List<ExhibitArtistEntity> exhibitArtistEntities = new ArrayList<>();

            ExhibitArtistEntity exhibitArtistEntityEntityJS = new ExhibitArtistEntity();
            exhibitArtistEntityEntityJS.setName("Designer C");
            exhibitArtistEntities.add(exhibitArtistEntityEntityJS);

            ExhibitArtistEntity exhibitArtistEntityEntityYY = new ExhibitArtistEntity();
            exhibitArtistEntityEntityYY.setName("Designer D");
            exhibitArtistEntities.add(exhibitArtistEntityEntityYY);

            exhibitEntity.setExhibitArtistEntityList(exhibitArtistEntities);
            exhibitEntity.setText("홍익대학교 산업디자인학과 2024 여름 전시 - 2");
            exhibitEntity.setImageUrl("전시 이미지 - 2");
            exhibitEntity.setVideoUrl("전시 영상 - 2");

            exhibitEntity.setTitle("여름 전시 제목");
            exhibitEntity.setSubtitle("여름 전시 부제");
            exhibitEntity.setMainThumbnailImageUrl("대표 이미지");

            em.persist(exhibitEntity);
        }

    }
}