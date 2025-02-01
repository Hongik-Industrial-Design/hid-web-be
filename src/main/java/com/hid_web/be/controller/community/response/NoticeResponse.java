package com.hid_web.be.controller.community.response;

import com.hid_web.be.storage.community.NoticeEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeResponse {
    private Long id;
    private String title;
    private String author;
    private LocalDate createdDate;
    private List<String> attachmentUrls; // 첨부파일 S3 URL 리스트
    private boolean isImportant;

    public NoticeResponse(NoticeEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.createdDate = entity.getCreatedDate();
        this.attachmentUrls = entity.getAttachmentUrls();
        this.isImportant = entity.isImportant();
    }

}
