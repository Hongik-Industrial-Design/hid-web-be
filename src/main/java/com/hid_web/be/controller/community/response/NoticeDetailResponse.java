package com.hid_web.be.controller.community.response;

import com.hid_web.be.storage.community.NoticeEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeDetailResponse {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdDate;
    private int views;
    private String attachmentUrl;
    private String imageUrl;
    private String content;

    public NoticeDetailResponse(NoticeEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.createdDate = entity.getCreatedDate();
        this.views = entity.getViews();
        this.attachmentUrl = entity.getAttachmentUrl();
        this.imageUrl = entity.getImageUrl();
        this.content = entity.getContent();
    }

}
