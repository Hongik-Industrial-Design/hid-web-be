package com.hid_web.be.controller.response;

import com.hid_web.be.domain.exhibit.ExhibitEntity;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitPreviewResponse {
    private Long exhibitId;
    private String mainThumbnailUrl;
    private String titleKo;
    private String titleEn;
    private String subtitleKo;
    private String subtitleEn;

    public static ExhibitPreviewResponse of(ExhibitEntity exhibitEntity) {
        return ExhibitPreviewResponse.builder()
                .exhibitId(exhibitEntity.getExhibitId())
                .mainThumbnailUrl(exhibitEntity.getMainThumbnailImageUrl())
                .titleKo(exhibitEntity.getTitleKo())
                .titleEn(exhibitEntity.getTitleEn())
                .subtitleKo(exhibitEntity.getSubtitleKo())
                .subtitleEn(exhibitEntity.getSubtitleEn())
                .build();
    }
}

