package com.hid_web.be.controller.response;

import com.hid_web.be.domain.exhibit.ExhibitEntity;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitResponse {

    private Long exhibitId;
    private List<ExhibitArtistResponse> exhibitArtistList;
    private String thumbnailUrl;
    private String text;
    private String imageUrl;
    private String videoUrl;

    // ExhibitEntity를 ExhibitResponse로 변환하는 정적 팩토리 메서드
    public static ExhibitResponse of(ExhibitEntity exhibitEntity) {
        return ExhibitResponse.builder()
                .exhibitId(exhibitEntity.getExhibitId())
                .exhibitArtistList(exhibitEntity.getExhibitArtistEntityList().stream()
                        .map(ea -> ExhibitArtistResponse.of(ea))
                        .toList())
                .thumbnailUrl(exhibitEntity.getMainThumbnailImageUrl())
                .text(exhibitEntity.getText())
                .imageUrl(exhibitEntity.getImageUrl())
                .videoUrl(exhibitEntity.getVideoUrl())
                .build();
    }
}