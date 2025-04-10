package com.hid_web.be.controller.professor.response;

import com.hid_web.be.storage.professor.BiographyEntryEntity;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BiographyEntryResponse {
    private String year;
    private String description;

    public static BiographyEntryResponse of(BiographyEntryEntity entity) {
        return BiographyEntryResponse.builder()
                .year(entity.getYear())
                .description(entity.getDescription())
                .build();
    }
}
