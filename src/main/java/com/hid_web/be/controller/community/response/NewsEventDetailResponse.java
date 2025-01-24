package com.hid_web.be.controller.community.response;

import com.hid_web.be.storage.community.NewsEventEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsEventDetailResponse {
    private Long id;
    private String title;
    private LocalDateTime createdDate;
    private int views;
    private String thumbnailUrl;
    private String content;
    private String attachmentUrl;
    private String category;

    public NewsEventDetailResponse(NewsEventEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.createdDate = entity.getCreatedDate();
        this.views = entity.getViews();
        this.thumbnailUrl = entity.getThumbnailUrl();
        this.content = entity.getContent();
        this.attachmentUrl = entity.getAttachmentUrl();
        this.category = entity.getCategory().name();
    }

}
