package com.hid_web.be.controller.exhibit.response;

import com.hid_web.be.domain.exhibit.ExhibitType;
import com.hid_web.be.storage.exhibit.ExhibitEntity;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitResponse {
    private Long exhibitId;
    private ExhibitType exhibitType;
    private String year;
    private String major;
    private String club;
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
                .exhibitType(exhibitEntity.getType())
                .year(exhibitEntity.getYear())
                .major(exhibitEntity.getMajor())
                .club(exhibitEntity.getClub())
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
