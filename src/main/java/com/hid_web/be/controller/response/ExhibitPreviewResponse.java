package com.hid_web.be.controller.response;

import com.hid_web.be.domain.exhibit.ExhibitEntity;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitPreviewResponse {
    private Long exhibitId;
    private String title;
    private String subtitle;
    private String thumbnailUrl;

    public static ExhibitPreviewResponse of(ExhibitEntity exhibitEntity) {
        return ExhibitPreviewResponse.builder()
                .exhibitId(exhibitEntity.getExhibitId())
                .title(exhibitEntity.getTitle())
                .subtitle(exhibitEntity.getSubtitle())
                .thumbnailUrl(exhibitEntity.getMainThumbnailImageUrl())
                .build();
    }
}
