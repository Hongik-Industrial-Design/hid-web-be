package com.hid_web.be.controller.exhibit.response;

import com.hid_web.be.domain.exhibit.ExhibitType;
import com.hid_web.be.domain.s3.S3UrlConverter;
import com.hid_web.be.storage.exhibit.ExhibitEntity;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitPreviewResponse {
    private Long exhibitId;
    private ExhibitType type;
    private String year;
    private String club;
    private String major;
    private String mainImgUrl;
    private String titleKo;
    private String titleEn;
    private String subTitleKo;
    private String subTitleEn;

    public static ExhibitPreviewResponse of(ExhibitEntity exhibitEntity) {
        return ExhibitPreviewResponse.builder()
                .exhibitId(exhibitEntity.getExhibitId())
                .type(exhibitEntity.getType())          // 전시 유형 추가
                .year(exhibitEntity.getYear())
                .club(exhibitEntity.getClub())
                .major(exhibitEntity.getMajor())
                .mainImgUrl(S3UrlConverter.convertCloudfrontUrlFromObjectKey(exhibitEntity.getMainImgObjectKey()))
                .titleKo(exhibitEntity.getTitleKo())
                .titleEn(exhibitEntity.getTitleEn())
                .subTitleKo(exhibitEntity.getSubTitleKo())
                .subTitleEn(exhibitEntity.getSubTitleEn())
                .build();
    }
}

