package com.hid_web.be.controller.community.response;

import com.hid_web.be.storage.community.NoticeEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeDetailResponse {
    private Long id;
    private String title;
    private String author;
    private LocalDate createdDate;
    private int views;
    private List<String> attachmentUrls; // S3에 저장된 파일 다운로드 URL 리스트
    private List<String> imageUrls; // 본문 이미지 URL 리스트
    private String content;
    private boolean isImportant;

    public NoticeDetailResponse(NoticeEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.createdDate = entity.getCreatedDate();
        this.views = entity.getViews();
        this.attachmentUrls = entity.getAttachmentUrls();
        this.imageUrls = entity.getImageUrls();
        this.content = entity.getContent().replace("\n", "<br>");
        this.isImportant = entity.isImportant();
    }

}
