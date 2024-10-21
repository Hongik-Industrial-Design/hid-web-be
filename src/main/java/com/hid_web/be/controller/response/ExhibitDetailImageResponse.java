package com.hid_web.be.controller.response;

import com.hid_web.be.domain.exhibit.ExhibitDetailImageEntity;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitDetailImageResponse {
    private String detailImageUrl;
    private int position;

    public static ExhibitDetailImageResponse of(ExhibitDetailImageEntity exhibitDetailImageEntity) {
        return ExhibitDetailImageResponse.builder()
                .detailImageUrl(exhibitDetailImageEntity.getDetailImageUrl())
                .position(exhibitDetailImageEntity.getPosition())
                .build();
    }
}
