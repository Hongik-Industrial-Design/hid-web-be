package com.hid_web.be.controller.professor.response;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfessorPreviewResponse {
    private String uuid;

    private String name;

    private String department;

    private String imgUrl;


}
