package com.hid_web.be.domain.exhibit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class ExhibitArtist {
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

