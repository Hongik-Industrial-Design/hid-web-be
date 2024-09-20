package com.hid_web.be.controller.response;

import com.hid_web.be.domain.exhibit.ExhibitEntity;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitPreviewResponse {
    private Long exhibitId; // 전시 번호
    private String title; // 제목
    private String subtitle; // 부제
    private String thumbnailUrl; // 썸네일 URL

    public static ExhibitPreviewResponse of(ExhibitEntity exhibitEntity) {
        return ExhibitPreviewResponse.builder()
                .exhibitId(exhibitEntity.getExhibitId())
                .title(exhibitEntity.getTitle())
                .subtitle(exhibitEntity.getSubtitle())
                .thumbnailUrl(exhibitEntity.getThumbnailUrl())
                .build();
    }
}
