package com.hid_web.be;

import com.hid_web.be.domain.community.NewsEventCategory;
import com.hid_web.be.domain.community.NoticeAuthorType;
import com.hid_web.be.storage.community.NewsEventEntity;
import com.hid_web.be.storage.community.NoticeEntity;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitCommunityDB {

    private final InitService initService;

    //@PostConstruct
//    public void init() {
//        initService.dbInit();
//    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

//        public void dbInit() {
//
//            NoticeEntity notice = NoticeEntity.builder()
//                    .uuid("notice-uuid-123")
//                    .title("공지사항 예제")
//                    .author(NoticeAuthorType.valueOf("TA"))
//                    .createdDate(LocalDate.now())
//                    .content("공지사항 본문 내용입니다.")
//                    .views(0)
//                    .isImportant(true)
//                    .attachmentUrls(List.of("https://s3-bucket-url/notice/attachments/sample.pdf"))
//                    .imageUrls(List.of("https://s3-bucket-url/notice/images/sample.jpg"))
//                    .build();
//            em.persist(notice);
//
//            NewsEventEntity newsEvent = NewsEventEntity.builder()
//                    .uuid("newsEvent-uuid-456")
//                    .title("뉴스이벤트 예제")
//                    .category(NewsEventCategory.AWARD)
//                    .createdDate(LocalDate.now())
//                    .content("뉴스이벤트 본문 내용입니다.")
//                    .views(0)
//                    .thumbnailUrl("https://s3-bucket-url/newsEvent/thumbnails/sample-thumbnail.jpg")
//                    .attachmentUrls(List.of("https://s3-bucket-url/newsEvent/attachments/sample-file.pdf"))
//                    .imageUrls(List.of("https://s3-bucket-url/newsEvent/images/sample1.jpg"))
//                    .build();
//            em.persist(newsEvent);
//
//        }
    }
}