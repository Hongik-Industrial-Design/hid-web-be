package com.hid_web.be.controller.exhibit.response;

import com.hid_web.be.domain.s3.S3UrlConverter;
import com.hid_web.be.storage.exhibit.ExhibitDetailImgEntity;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitDetailImgResponse {
    private String detailImgUrl;
    private int position;

    public static ExhibitDetailImgResponse of(ExhibitDetailImgEntity exhibitDetailImgEntity) {
        return ExhibitDetailImgResponse.builder()
                .detailImgUrl(S3UrlConverter.convertCloudfrontUrlFromObjectKey(exhibitDetailImgEntity.getDetailImgObjectKey()))
                .position(exhibitDetailImgEntity.getPosition())
                .build();
    }
}