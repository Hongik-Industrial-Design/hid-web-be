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
            notice1.setTitle("[세종테크노파크] 세종RISE 센터 사업 대학생 대상 교육 수요조사.");
            notice1.setAuthor("TA");
            notice1.setCreatedDate(LocalDateTime.of(2024, 3, 2, 0, 0));
            notice1.setAttachmentUrl("https://example.com/file1.pdf");
            notice1.setImageUrl("https://example.com/image1.jpg");
            notice1.setContent("세종 지역 대학생을 대상으로 한 교육 수요조사입니다. 상세 내용은 첨부파일을 참고해주세요.");
            em.persist(notice1);

            NoticeEntity notice2 = new NoticeEntity();
            notice2.setTitle("(중요) 기자재 대여 안내");
            notice2.setAuthor("Council");
            notice2.setCreatedDate(LocalDateTime.of(2024, 3, 1, 0, 0));
            notice2.setAttachmentUrl(null);
            notice2.setImageUrl("https://example.com/image2.jpg");
            notice2.setContent("기자재 대여에 관한 새로운 규정을 확인해주세요. 이미지와 상세 내용을 참고 바랍니다.");
            em.persist(notice2);
        }
    }
}
