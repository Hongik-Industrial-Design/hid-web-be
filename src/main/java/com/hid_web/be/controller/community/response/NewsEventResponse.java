package com.hid_web.be.controller.community.response;

import com.hid_web.be.storage.community.NewsEventEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsEventResponse {
    private Long id;
    private String thumbnailUrl;
    private LocalDateTime createdDate;
    private String title;
    private String category;

    public NewsEventResponse(NewsEventEntity entity) {
        this.id = entity.getId();
        this.thumbnailUrl = entity.getThumbnailUrl();
        this.createdDate = entity.getCreatedDate();
        this.title = entity.getTitle();
        this.category = entity.getCategory().name();
    }

}
