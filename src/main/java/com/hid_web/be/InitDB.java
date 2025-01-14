package com.hid_web.be;

import com.hid_web.be.storage.exhibit.ExhibitEntity;
import com.hid_web.be.storage.exhibit.ExhibitArtistEntity;
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

//    @PostConstruct
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

            exhibitEntity.setMainImgUrl("대표 이미지");

            ExhibitArtistEntity exhibitArtistEntityEntityKZ = new ExhibitArtistEntity();

            exhibitEntity.setArtistEntities(exhibitArtistEntities);

            exhibitEntity.setTitleKo("여름 전시 제목");
            exhibitEntity.setTitleEn("Summer Exhibit Title");
            exhibitEntity.setSubTitleKo("여름 전시 부제");
            exhibitEntity.setSubTitleEn("Summer Exhibit Subtitle");
            exhibitEntity.setTextKo("홍익대학교 산업디자인학과 2024 여름 전시 - 1");
            exhibitEntity.setTextEn("HID Design 2024 Summer Exhibit - 1");
            exhibitEntity.setVideoUrl("전시 영상 - 1");

            exhibitArtistEntityEntityKZ.setNameEn("Designer A");
            exhibitArtistEntities.add(exhibitArtistEntityEntityKZ);

            ExhibitArtistEntity exhibitArtistEntityEntityBM = new ExhibitArtistEntity();
            exhibitArtistEntityEntityBM.setNameEn("Designer B");
            exhibitArtistEntities.add(exhibitArtistEntityEntityBM);

            em.persist(exhibitEntity);
        }

        public void dbInit2() {
            ExhibitEntity exhibitEntity = new ExhibitEntity();
            List<ExhibitArtistEntity> exhibitArtistEntities = new ArrayList<>();

            exhibitEntity.setArtistEntities(exhibitArtistEntities);

            exhibitEntity.setTitleKo("여름 전시 제목");
            exhibitEntity.setTitleEn("Summer Exhibit Title");
            exhibitEntity.setSubTitleKo("여름 전시 부제");
            exhibitEntity.setSubTitleEn("Summer Exhibit Subtitle");
            exhibitEntity.setTextKo("홍익대학교 산업디자인학과 2024 여름 전시 - 2");
            exhibitEntity.setTextEn("HID Design 2024 Summer Exhibit - 2");
            exhibitEntity.setVideoUrl("전시 영상 - 2");

            exhibitEntity.setMainImgUrl("대표 이미지");

            ExhibitArtistEntity exhibitArtistEntityEntityJS = new ExhibitArtistEntity();
            exhibitArtistEntityEntityJS.setNameEn("Designer C");
            exhibitArtistEntities.add(exhibitArtistEntityEntityJS);

            ExhibitArtistEntity exhibitArtistEntityEntityYY = new ExhibitArtistEntity();
            exhibitArtistEntityEntityYY.setNameEn("Designer D");
            exhibitArtistEntities.add(exhibitArtistEntityEntityYY);

            em.persist(exhibitEntity);
        }

    }
}