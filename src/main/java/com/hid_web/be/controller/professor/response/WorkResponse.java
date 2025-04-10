package com.hid_web.be.controller.professor.response;

import com.hid_web.be.storage.professor.WorkEntity;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkResponse {
    private String year;
    private String title;

    public static WorkResponse of(WorkEntity entity) {
        return WorkResponse.builder()
                .year(entity.getYear())
                .title(entity.getTitle())
                .build();
    }
}
