package com.hid_web.be.controller.response;

import com.hid_web.be.domain.exhibit.ExhibitArtistEntity;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitArtistResponse {
    private Long id;
    private String artistUUID;
    private String profileImageUrl;
    private String artistNameKo;
    private String artistNameEn;
    private String role;
    private String email;
    private String instagramUrl;
    private String behanceUrl;
    private String linkedinUrl;

    public static ExhibitArtistResponse of(ExhibitArtistEntity exhibitArtistEntity) {
        return ExhibitArtistResponse.builder()
                .id(exhibitArtistEntity.getId())
                .artistUUID(exhibitArtistEntity.getArtistUUID())
                .profileImageUrl(exhibitArtistEntity.getProfileImageUrl())
                .artistNameKo(exhibitArtistEntity.getArtistNameKo())
                .artistNameEn(exhibitArtistEntity.getArtistNameEn())
                .role(exhibitArtistEntity.getRole())
                .email(exhibitArtistEntity.getEmail())
                .instagramUrl(exhibitArtistEntity.getInstagramUrl())
                .behanceUrl(exhibitArtistEntity.getBehanceUrl())
                .linkedinUrl(exhibitArtistEntity.getLinkedinUrl())
                .build();
    }
}
