package com.hid_web.be.controller.community.response;

import com.hid_web.be.storage.community.NoticeEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeResponse {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdDate;
    private String attachmentUrl;
    private boolean isImportant;

    public NoticeResponse(NoticeEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.createdDate = entity.getCreatedDate();
        this.attachmentUrl = entity.getAttachmentUrl();
        this.isImportant = entity.isImportant();
    }

}
