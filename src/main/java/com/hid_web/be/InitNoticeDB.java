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

            NoticeEntity notice1 = new NoticeEntity();
            notice1.setTitle("공지사항 1");
            notice1.setAuthor("TA");
            notice1.setCreatedDate(LocalDateTime.of(2024, 3, 2, 0, 0));
            notice1.setHasAttachment(true);
            em.persist(notice1);
            
            NoticeEntity notice2 = new NoticeEntity();
            notice2.setTitle("공지사항 2");
            notice2.setAuthor("Council");
            notice2.setCreatedDate(LocalDateTime.of(2024, 3, 1, 0, 0));
            notice2.setHasAttachment(false);
            em.persist(notice2);
        }
    }
}
