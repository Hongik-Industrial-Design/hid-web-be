package com.hid_web.be.controller.community.response;

import com.hid_web.be.domain.community.NewsEventCategory;
import com.hid_web.be.storage.community.NewsEventEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsEventDetailResponse {
    private Long id;
    private String title;
    private LocalDate createdDate;
    private int views;
    private String thumbnailUrl;
    private String content;
    private List<String> imageUrls; // 본문 이미지 URL 리스트
    private List<String> attachmentUrls; // S3에 저장된 첨부파일 다운로드 URL 리스트
    private NewsEventCategory category;

    public NewsEventDetailResponse(NewsEventEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.createdDate = entity.getCreatedDate();
        this.views = entity.getViews();
        this.thumbnailUrl = entity.getThumbnailUrl();
        this.content = entity.getContent().replace("\n", "<br>");
        this.imageUrls = entity.getImageUrls();
        this.attachmentUrls = entity.getAttachmentUrls();
        this.category = entity.getCategory();
    }

}
