package com.hid_web.be.controller.exhibit.request;

import com.hid_web.be.domain.exhibit.ExhibitType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExhibitDetailRequest {
    @NotNull(message = "전시 타입은 필수입니다.")
    private ExhibitType exhibitType;

    @NotNull(message = "전시 연도는 필수입니다.")
    private Integer year;

    private String major;
    private String club;
    private String titleKo;
    private String titleEn;
    private String subTitleKo;
    private String subTitleEn;
    private String textKo;
    private String textEn;
    private String videoUrl;
}