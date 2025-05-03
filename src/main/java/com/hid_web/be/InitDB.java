package com.hid_web.be;

import com.hid_web.be.storage.content.ContentMainVideoEntity;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit() {
            Long year2024 = 2024L;
            List<ContentMainVideoEntity> existingVideos2024 = em.createQuery(
                            "SELECT v FROM ContentMainVideoEntity v WHERE v.year = :year", ContentMainVideoEntity.class)
                    .setParameter("year", year2024)
                    .getResultList();

            if (existingVideos2024.isEmpty()) {
                ContentMainVideoEntity contentMainVideoEntity2024 = new ContentMainVideoEntity();
                contentMainVideoEntity2024.setTitle("2024 졸업 전시 영상");
                contentMainVideoEntity2024.setS3ObjectKey("graduation-videos/2024/2024_graduation_video.mp4");
                contentMainVideoEntity2024.setYear(year2024);
                em.persist(contentMainVideoEntity2024);
            }

            Long year2023 = 2023L;
            List<ContentMainVideoEntity> existingVideos2023 = em.createQuery(
                            "SELECT v FROM ContentMainVideoEntity v WHERE v.year = :year", ContentMainVideoEntity.class)
                    .setParameter("year", year2023)
                    .getResultList();

            if (existingVideos2023.isEmpty()) {
                ContentMainVideoEntity contentMainVideoEntity2023 = new ContentMainVideoEntity();
                contentMainVideoEntity2023.setTitle("2023 졸업 전시 영상");
                contentMainVideoEntity2023.setS3ObjectKey("graduation-videos/2023/2023_graduation_video.mp4");
                contentMainVideoEntity2023.setYear(year2023);
                em.persist(contentMainVideoEntity2023);
            }
        }
    }
}