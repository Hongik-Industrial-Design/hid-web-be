package com.hid_web.be.controller.response;

import com.hid_web.be.domain.exhibit.ExhibitEntity;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitResponse {

    private Long exhibitId; // 전시 번호
    private List<ExhibitArtistResponse> exhibitArtistList; // 참여 학생 리스트
    private String text; // 텍스트
    private String imageUrl; // 이미지 URL
    private String videoUrl; // 영상 URL

    // ExhibitEntity를 ExhibitResponse로 변환하는 정적 팩토리 메서드
    public static ExhibitResponse of(ExhibitEntity exhibitEntity) {
        return ExhibitResponse.builder()
                .exhibitId(exhibitEntity.getExhibitId())
                .exhibitArtistList(exhibitEntity.getExhibitArtistEntityList().stream()
                        .map(ea -> ExhibitArtistResponse.of(ea))
                        .toList()) // 참여 학생 목록 변환
                .text(exhibitEntity.getText())
                .imageUrl(exhibitEntity.getImageUrl())
                .videoUrl(exhibitEntity.getVideoUrl())
                .build();
    }
}