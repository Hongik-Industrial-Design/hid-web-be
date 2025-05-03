package com.hid_web.be.controller.exhibit.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateExhibitDetailImgRequest {
    @NotNull(message = "상세 이미지 파일이 필요합니다")
    private MultipartFile file;

    @NotNull
    @Min(value = 1, message = "위치는 1 이상이어야 합니다")
    private Integer position;
}