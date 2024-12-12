package com.hid_web.be.controller.response;

import com.hid_web.be.storage.ExhibitDetailImgEntity;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitDetailImgResponse {
    private String detailImgUrl;
    private int position;

    public static ExhibitDetailImgResponse of(ExhibitDetailImgEntity exhibitDetailImgEntity) {
        return ExhibitDetailImgResponse.builder()
                .detailImgUrl(exhibitDetailImgEntity.getDetailImgUrl())
                .position(exhibitDetailImgEntity.getPosition())
                .build();
    }
}
