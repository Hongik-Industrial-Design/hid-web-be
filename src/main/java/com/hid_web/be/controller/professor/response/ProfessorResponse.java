package com.hid_web.be.controller.professor.response;

import com.hid_web.be.domain.s3.S3UrlConverter;
import com.hid_web.be.storage.professor.ProfessorEntity;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfessorResponse {

    private String uuid;
    private String name;
    private String position;
    private String department;
    private String detailInfo;
    private String email;
    private String major;
    private String imgUrl;

    private List<BiographyEntryResponse> biographyEntries;
    private List<AwardResponse> awards;
    private List<WorkResponse> works;

    public static ProfessorResponse of(ProfessorEntity professor) {
        return ProfessorResponse.builder()
                .uuid(professor.getUuid())
                .name(professor.getName())
                .position(professor.getPosition())
                .department(professor.getDepartment())
                .detailInfo(professor.getDetailInfo())
                .email(professor.getEmail())
                .major(professor.getMajor())
                .imgUrl(S3UrlConverter.convertCloudfrontUrlFromObjectKey(professor.getImgObjectKey()))
                .biographyEntries(professor.getBiographyEntries().stream()
                        .map(BiographyEntryResponse::of)
                        .toList())
                .awards(professor.getAwards().stream()
                        .map(AwardResponse::of)
                        .toList())
                .works(professor.getWorks().stream()
                        .map(WorkResponse::of)
                        .toList())
                .build();
    }
}
