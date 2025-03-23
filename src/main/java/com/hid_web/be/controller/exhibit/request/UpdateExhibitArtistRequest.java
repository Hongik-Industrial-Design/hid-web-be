package com.hid_web.be.controller.exhibit.request;

import com.hid_web.be.domain.exhibit.ExhibitImgType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UUID;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateExhibitArtistRequest {
    @NotBlank
    @UUID(message = "학생 UUID만 허용됩니다.")
    private String artistUUID;

    private MultipartFile profileImgFile;
    private String profileImgUrl;

    @NotNull
    private ExhibitImgType type;

    /**
     * 학생 이름 국문, 영문 검증 필요할 시 추가
     */
    private String nameKo;
    private String nameEn;
    private String role;

    @Email(message = "올바른 이메일 형식이어야 합니다")
    private String email;

    @URL(host = "www.instagram.com", message = "Instagram URL만 허용됩니다")
    private String instagramUrl;

    @URL(host = "www.behance.net", message = "Behance URL만 허용됩니다")
    private String behanceUrl;

    @URL(host = "www.linkedin.com", message = "LinkedIn URL만 허용됩니다")
    private String linkedinUrl;

    @AssertTrue(message = "새로운 이미지 업로드 목적으로 FILE 타입인 경우 프로필 이미지 FILE이 필수이며, URL은 입력할 수 없습니다")
    private boolean isValidFileType() {
        if (ExhibitImgType.FILE.equals(type)) {
            return (profileImgFile != null && !profileImgFile.isEmpty()) && (profileImgUrl == null);
        }
        return true;
    }

    @AssertTrue(message = "기존 이미지 유지 목적으로 URL 타입인 경우 프로필 이미지 URL이 필수이며, FILE은 입력할 수 없습니다")
    private boolean isValidUrlType() {
        if (ExhibitImgType.URL.equals(type)) {
            return (profileImgUrl != null && !profileImgUrl.trim().isEmpty()) && (profileImgFile == null);
        }
        return true;
    }
}

