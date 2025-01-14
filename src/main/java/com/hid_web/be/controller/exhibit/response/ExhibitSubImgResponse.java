package com.hid_web.be.controller.exhibit.response;

import com.hid_web.be.storage.exhibit.ExhibitSubImgEntity;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitSubImgResponse {
    private String subImgUrl;
    private int position;

    public static ExhibitSubImgResponse of(ExhibitSubImgEntity exhibitSubImgEntity) {
        return ExhibitSubImgResponse.builder()
                .subImgUrl(exhibitSubImgEntity.getSubImgUrl())
                .position(exhibitSubImgEntity.getPosition())
                .build();
    }
}
