package com.hid_web.be.controller.exhibit.response;

import com.hid_web.be.domain.s3.S3UrlConverter;
import com.hid_web.be.storage.exhibit.ExhibitArtistEntity;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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
                .profileImgUrl(S3UrlConverter.convertCloudfrontUrlFromObjectKey(exhibitArtistEntity.getProfileImgObjectKey()))
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