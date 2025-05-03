package com.hid_web.be.controller.exhibit.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class UpdateExhibitDetailRequest {
    /**
     * 졸업 전시와 소모임 전시 가이드가 달라질 시 불필요한 필드
     * Update에선 ExhibitType 변경을 막고 삭제하고 다시 생성하도록 하는 것이 명확할 수 있으므로 추후 변경 가능성
     */
    /*
    private ExhibitType exhibitType;
    */

    @Pattern(regexp = "^\\d{4}$", message = "연도는 4자리 숫자여야 합니다.")
    private String year;

    /**
     * Major, Club 둘 중 하나만 존재함을 검증하는 것은 다음 레이어에서 수행하도록 필요할 시 추가
     * Update에선 Major, Club 변경을 막고 삭제하고 다시 생성하도록 하는 것이 명확할 수 있으므로 추후 변경 가능성
     */
    /*
    private String major;
    private String club;
     */

    @URL(host = "www.behance.net", message = "Behance URL만 허용됩니다")
    private String behanceUrl;

    @URL(host = "www.instagram.com", message = "Instagram URL만 허용됩니다")
    private String instagramUrl;

    @Size(max = 33, message = "한글 제목은 33자를 초과할 수 없습니다")
    private String titleKo;

    @Size(max = 50, message = "영문 제목은 50자를 초과할 수 없습니다")
    private String titleEn;

    @Size(max = 300, message = "한글 부제목은 300자를 초과할 수 없습니다")
    private String subTitleKo;

    @Size(max = 700, message = "영문 부제목은 700자를 초과할 수 없습니다")
    private String subTitleEn;

    @Size(max = 1000, message = "한글 본문은 1000자를 초과할 수 없습니다")
    private String descriptionKo;

    @Size(max = 1000, message = "영문 본문은 1000자를 초과할 수 없습니다")
    private String descriptionEn;

    /**
     * 영상 선택
     */
    // @Pattern(regexp = "^(https?://.*)?$", message = "올바른 URL 형식이 아닙니다")
    private String videoUrl;
}