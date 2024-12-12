package com.hid_web.be.controller.response;

import com.hid_web.be.storage.ExhibitEntity;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitPreviewResponse {
    private Long exhibitId;
    private String mainImgUrl;
    private String titleKo;
    private String titleEn;
    private String subTitleKo;
    private String subTitleEn;

    public static ExhibitPreviewResponse of(ExhibitEntity exhibitEntity) {
        return ExhibitPreviewResponse.builder()
                .exhibitId(exhibitEntity.getExhibitId())
                .mainImgUrl(exhibitEntity.getMainImgUrl())
                .titleKo(exhibitEntity.getTitleKo())
                .titleEn(exhibitEntity.getTitleEn())
                .subTitleKo(exhibitEntity.getSubTitleKo())
                .subTitleEn(exhibitEntity.getSubTitleEn())
                .build();
    }
}

