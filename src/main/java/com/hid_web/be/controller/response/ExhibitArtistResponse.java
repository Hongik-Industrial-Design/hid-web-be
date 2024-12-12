package com.hid_web.be.controller.response;

import com.hid_web.be.storage.ExhibitArtistEntity;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitArtistResponse {
    private Long id;
    private String artistUUID;
    private String profileImgUrl;
    private String nameKo;
    private String nameEn;
    private String role;
    private String email;
    private String instagramUrl;
    private String behanceUrl;
    private String linkedinUrl;

    public static ExhibitArtistResponse of(ExhibitArtistEntity exhibitArtistEntity) {
        return ExhibitArtistResponse.builder()
                .id(exhibitArtistEntity.getArtistId())
                .artistUUID(exhibitArtistEntity.getArtistUUID())
                .profileImgUrl(exhibitArtistEntity.getProfileImgUrl())
                .nameKo(exhibitArtistEntity.getNameKo())
                .nameEn(exhibitArtistEntity.getNameEn())
                .role(exhibitArtistEntity.getRole())
                .email(exhibitArtistEntity.getEmail())
                .instagramUrl(exhibitArtistEntity.getInstagramUrl())
                .behanceUrl(exhibitArtistEntity.getBehanceUrl())
                .linkedinUrl(exhibitArtistEntity.getLinkedinUrl())
                .build();
    }
}
