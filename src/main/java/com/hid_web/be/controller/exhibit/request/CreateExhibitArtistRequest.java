package com.hid_web.be.controller.exhibit.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateExhibitArtistRequest {
    @NotNull(message = "프로필 이미지는 필수입니다")
    private MultipartFile profileImgFile;

    @NotBlank(message = "한글 이름은 필수입니다")
    @Size(max = 30, message = "한글 이름은 30자를 초과할 수 없습니다")
    private String nameKo;

    @NotBlank(message = "영문 이름은 필수입니다")
    @Size(max = 30, message = "영문 이름은 30자를 초과할 수 없습니다")
    private String nameEn;

    @NotBlank(message = "역할은 필수입니다")
    @Size(max = 100, message = "역할은 100자를 초과할 수 없습니다")
    private String role;

    @Email(message = "올바른 이메일 형식이어야 합니다")
    @NotBlank(message = "이메일은 필수입니다")
    private String email;

    @URL(host = "www.instagram.com", message = "Instagram URL만 허용됩니다")
    private String instagramUrl;

    @URL(host = "www.behance.net", message = "Behance URL만 허용됩니다")
    private String behanceUrl;

    @URL(host = "www.linkedin.com", message = "LinkedIn URL만 허용됩니다")
    private String linkedinUrl;

    // MultipartFile 유효성 검사를 위한 커스텀 검증
    @AssertTrue(message = "이미지 파일만 업로드 가능합니다")
    private boolean isValidProfileImgFile() {
        if (profileImgFile == null || profileImgFile.isEmpty()) {
            return false;
        }

        String contentType = profileImgFile.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}
