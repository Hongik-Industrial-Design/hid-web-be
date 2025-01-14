package com.hid_web.be.controller.request;

import com.hid_web.be.domain.exhibit.ExhibitType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateExhibitDetailRequest {
    // 전시 타입
    @NotNull(message = "전시 타입은 필수입니다.")
    private ExhibitType exhibitType;

    // 전시 연도
    @NotNull(message = "전시 연도는 필수입니다.")
    private Integer year;

    // 졸업 전시 전공 이름
    private String major; // 졸업 전시 필터링

    // 소모임 전시 이름
    private String club;  // 소모임 전시 필터링

    private String titleKo;
    private String titleEn;
    private String subTitleKo;
    private String subTitleEn;
    private String textKo;
    private String textEn;
    private String videoUrl;
}
