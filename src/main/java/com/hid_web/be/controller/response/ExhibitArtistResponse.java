package com.hid_web.be.controller.response;

import com.hid_web.be.domain.exhibit.ExhibitArtistEntity;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitArtistResponse {
    private String name;

    public static ExhibitArtistResponse of(ExhibitArtistEntity exhibitArtistEntity) {
        return ExhibitArtistResponse.builder()
                .name(exhibitArtistEntity.getName())
                .build();
    }
}