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
    private String mainThumbnailImageUrl;
    private List<ExhibitAdditionalThumbnailResponse> exhibitAdditionalThumbnailImageList;
    private List<ExhibitDetailImageResponse> exhibitDetailImageEntityList;
    private String titleKo;
    private String titleEn;
    private String subtitleKo;
    private String subtitleEn;
    private String textKo;
    private String textEn;
    private String videoUrl;
    private List<ExhibitArtistResponse> exhibitArtistList;

    // ExhibitEntity를 ExhibitResponse로 변환하는 정적 팩토리 메서드
    public static ExhibitResponse of(ExhibitEntity exhibitEntity) {
        return ExhibitResponse.builder()
                .exhibitId(exhibitEntity.getExhibitId())
                .mainThumbnailImageUrl(exhibitEntity.getMainThumbnailImageUrl())
                .exhibitAdditionalThumbnailImageList(exhibitEntity.getExhibitAdditionalThumbnailImageEntityList().stream()
                        .map(ExhibitAdditionalThumbnailResponse::of)
                        .toList())
                .exhibitDetailImageEntityList(exhibitEntity.getExhibitDetailImageEntityList().stream()
                        .map(ExhibitDetailImageResponse::of)
                        .toList())
                .titleKo(exhibitEntity.getTitleKo())
                .titleEn(exhibitEntity.getTitleEn())
                .subtitleKo(exhibitEntity.getSubtitleKo())
                .subtitleEn(exhibitEntity.getSubtitleEn())
                .textKo(exhibitEntity.getTextKo())
                .textEn(exhibitEntity.getTextEn())
                .videoUrl(exhibitEntity.getVideoUrl())
                .exhibitArtistList(exhibitEntity.getExhibitArtistEntityList().stream()
                        .map(ExhibitArtistResponse::of)
                        .toList())
                .build();
    }
}
