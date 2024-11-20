package com.hid_web.be.domain.exhibit;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitArtist {
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

