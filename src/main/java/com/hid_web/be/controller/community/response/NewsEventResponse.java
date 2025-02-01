package com.hid_web.be.controller.community.response;

import com.hid_web.be.domain.community.NewsEventCategory;
import com.hid_web.be.storage.community.NewsEventEntity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsEventResponse {
    private Long id;
    private String thumbnailUrl;
    private LocalDate createdDate;
    private String title;
    private NewsEventCategory category;

    public NewsEventResponse(NewsEventEntity entity) {
        this.id = entity.getId();
        this.thumbnailUrl = entity.getThumbnailUrl();
        this.createdDate = entity.getCreatedDate();
        this.title = entity.getTitle();
        this.category = entity.getCategory();
    }

}
