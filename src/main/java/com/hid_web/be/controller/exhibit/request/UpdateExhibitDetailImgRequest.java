package com.hid_web.be.controller.exhibit.request;

import com.hid_web.be.domain.exhibit.ExhibitImgType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateExhibitDetailImgRequest {
    private MultipartFile file;
    private String url;

    @NotNull
    @Min(value = 1, message = "위치는 1 이상이어야 합니다")
    private Integer position;

    @NotNull(message = "새로운 이미지 업로드 목적인 FILE, 기존 이미지 유지 목적인 URL 둘 중 하나의 이미지 타입이 필요합니다.")
    private ExhibitImgType type;

    @AssertTrue(message = "새로운 이미지 업로드 목적으로 FILE 타입인 경우 상세 이미지 FILE이 필수이며, URL은 입력할 수 없습니다")
    private boolean isValidFileType() {
        if (ExhibitImgType.FILE.equals(type)) {
            return (file != null && !file.isEmpty()) && (url == null);
        }
        return true;
    }

    @AssertTrue(message = "기존 이미지 유지 목적으로 URL 타입인 경우 상세 이미지 URL이 필수이며, FILE은 입력할 수 없습니다")
    private boolean isValidUrlType() {
        if (ExhibitImgType.URL.equals(type)) {
            return (url != null && !url.trim().isEmpty()) && (file == null);
        }
        return true;
    }
}