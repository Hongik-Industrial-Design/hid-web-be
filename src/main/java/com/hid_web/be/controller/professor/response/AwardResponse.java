package com.hid_web.be.controller.professor.response;

import com.hid_web.be.storage.professor.AwardEntity;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AwardResponse {
    private String year;
    private String title;

    public static AwardResponse of(AwardEntity entity) {
        return AwardResponse.builder()
                .year(entity.getYear())
                .title(entity.getTitle())
                .build();
    }
}
