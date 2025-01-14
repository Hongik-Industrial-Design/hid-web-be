package com.hid_web.be.controller.request;

import com.hid_web.be.domain.exhibit.ExhibitImgType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExhibitArtistRequest {
    private ExhibitImgType type;
    private String artistUUID;
    private MultipartFile profileImgFile;
    private String profileImgUrl;
    private String nameKo;
    private String nameEn;
    private String role;
    private String email;
    private String instagramUrl;
    private String behanceUrl;
    private String linkedinUrl;
}

