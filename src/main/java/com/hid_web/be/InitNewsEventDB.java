package com.hid_web.be;

import com.hid_web.be.domain.community.NewsEventCategory;
import com.hid_web.be.storage.community.NewsEventEntity;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitNewsEventDB {

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
            for (int i = 1; i <= 10; i++) {
                NewsEventEntity newsEvent = new NewsEventEntity();
                newsEvent.setThumbnailUrl("https://example.com/image" + i + ".jpg");
                newsEvent.setCreatedDate(LocalDateTime.now().minusDays(i));
                newsEvent.setTitle("뉴스이벤트 제목 " + i);
                newsEvent.setCategory(i % 4 == 0 ? NewsEventCategory.RECRUIT :
                        i % 4 == 1 ? NewsEventCategory.CONTEST :
                                i % 4 == 2 ? NewsEventCategory.LECTURE_SEMINAR :
                                        NewsEventCategory.AWARD);
                newsEvent.setContent("뉴스 & 이벤트 본문 내용 " + i);
                newsEvent.setAttachmentUrl(i % 2 == 0 ? "https://example.com/file" + i + ".pdf" : null);
                newsEvent.setViews(0);
                em.persist(newsEvent);
            }
        }
    }
}
