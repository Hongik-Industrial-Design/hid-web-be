package com.hid_web.be.controller.exhibit.response;

import com.hid_web.be.domain.exhibit.ExhibitType;
import com.hid_web.be.domain.s3.S3UrlConverter;
import com.hid_web.be.storage.exhibit.ExhibitEntity;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA, Jackson 라이브러리에서 기본 생성자 필요
@AllArgsConstructor(access = AccessLevel.PROTECTED) // 빌더 패턴에서 필요
public class ExhibitResponse {
    private Long exhibitId;
    private ExhibitType exhibitType;
    private String year;
    private String major;
    private String club;
    private String instagramUrl;
    private String behanceUrl;
    private String mainImgUrl;
    private List<ExhibitDetailImgResponse> detailImgs;
    private String titleKo;
    private String titleEn;
    private String subTitleKo;
    private String subTitleEn;
    private String descriptionKo;
    private String descriptionEn;
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
                .instagramUrl(exhibitEntity.getInstagramUrl())
                .behanceUrl(exhibitEntity.getBehanceUrl())
                .mainImgUrl(S3UrlConverter.convertCloudfrontUrlFromObjectKey(exhibitEntity.getMainImgObjectKey()))
                .detailImgs(exhibitEntity.getDetailImgEntities().stream()
                        .map(ExhibitDetailImgResponse::of)
                        .toList())
                .titleKo(exhibitEntity.getTitleKo())
                .titleEn(exhibitEntity.getTitleEn())
                .subTitleKo(exhibitEntity.getSubTitleKo())
                .subTitleEn(exhibitEntity.getSubTitleEn())
                .descriptionKo(exhibitEntity.getDescriptionKo())
                .descriptionEn(exhibitEntity.getDescriptionEn())
                .videoUrl(exhibitEntity.getVideoUrl())
                .artists(exhibitEntity.getArtistEntities().stream()
                        .map(ExhibitArtistResponse::of)
                        .toList())
                .build();
    }
}

