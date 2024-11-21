package com.hid_web.be.controller.request;

import com.hid_web.be.domain.exhibit.ExhibitImageType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExhibitArtistRequest {
    private ExhibitImageType type;
    private String artistUUID;
    private MultipartFile profileImageFile;
    private String profileImageFileUrl;
    private String artistNameKo;
    private String artistNameEn;
    private String role;
    private String email;
    private String instagramUrl;
    private String behanceUrl;
    private String linkedinUrl;
}

