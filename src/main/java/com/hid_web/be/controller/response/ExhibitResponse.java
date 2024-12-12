package com.hid_web.be.controller.response;

import com.hid_web.be.storage.ExhibitEntity;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitResponse {
    private Long exhibitId;
    private String mainImgUrl;
    private List<ExhibitSubImgResponse> subImgs;
    private List<ExhibitDetailImgResponse> detailImgs;
    private String titleKo;
    private String titleEn;
    private String subTitleKo;
    private String subTitleEn;
    private String textKo;
    private String textEn;
    private String videoUrl;
    private List<ExhibitArtistResponse> artists;

    // ExhibitEntity를 ExhibitResponse로 변환하는 정적 팩토리 메서드
    public static ExhibitResponse of(ExhibitEntity exhibitEntity) {
        return ExhibitResponse.builder()
                .exhibitId(exhibitEntity.getExhibitId())
                .mainImgUrl(exhibitEntity.getMainImgUrl())
                .subImgs(exhibitEntity.getSubImgEntities().stream()
                        .map(ExhibitSubImgResponse::of)
                        .toList())
                .detailImgs(exhibitEntity.getDetailImgEntities().stream()
                        .map(ExhibitDetailImgResponse::of)
                        .toList())
                .titleKo(exhibitEntity.getTitleKo())
                .titleEn(exhibitEntity.getTitleEn())
                .subTitleKo(exhibitEntity.getSubTitleKo())
                .subTitleEn(exhibitEntity.getSubTitleEn())
                .textKo(exhibitEntity.getTextKo())
                .textEn(exhibitEntity.getTextEn())
                .videoUrl(exhibitEntity.getVideoUrl())
                .artists(exhibitEntity.getArtistEntities().stream()
                        .map(ExhibitArtistResponse::of)
                        .toList())
                .build();
    }
}
