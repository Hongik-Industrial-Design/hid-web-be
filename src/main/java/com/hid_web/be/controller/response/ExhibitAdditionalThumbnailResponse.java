package com.hid_web.be.controller.response;

import com.hid_web.be.domain.exhibit.ExhibitAdditionalThumbnailEntity;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitAdditionalThumbnailResponse {
    private String additionalThumbnailImageUrl;
    private int position;

    public static ExhibitAdditionalThumbnailResponse of(ExhibitAdditionalThumbnailEntity exhibitAdditionalThumbnailEntity) {
        return ExhibitAdditionalThumbnailResponse.builder()
                .additionalThumbnailImageUrl(exhibitAdditionalThumbnailEntity.getAdditionalThumbnailImageUrl())
                .position(exhibitAdditionalThumbnailEntity.getPosition())
                .build();
    }
}
