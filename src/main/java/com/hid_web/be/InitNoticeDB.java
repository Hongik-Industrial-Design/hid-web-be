package com.hid_web.be;

import com.hid_web.be.storage.community.NoticeEntity;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitNoticeDB {

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
            for (int i = 1; i <= 20; i++) {
                NoticeEntity notice = new NoticeEntity();
                notice.setTitle(i % 5 == 0 ? "중요 공지사항 " + i : "일반 공지사항 " + i);
                notice.setAuthor(i % 3 == 0 ? "TA" : "Council");
                notice.setCreatedDate(LocalDateTime.now().minusDays(20-i));
                notice.setViews(0);
                notice.setAttachmentUrl(i % 4 == 0 ? "https://example.com/file" + i + ".pdf" : null);
                notice.setImportant(i % 5 == 0);
                em.persist(notice);
            }
        }
    }
}
