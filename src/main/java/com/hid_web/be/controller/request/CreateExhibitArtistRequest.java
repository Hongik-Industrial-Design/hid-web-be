package com.hid_web.be.controller.request;

import com.hid_web.be.domain.exhibit.ExhibitImageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateExhibitArtistRequest {
    private ExhibitImageType type;
    private MultipartFile profileImageFile;
    private String artistNameKo;
    private String artistNameEn;
    private String role;
    private String email;
    private String instagramUrl;
    private String behanceUrl;
    private String linkedinUrl;
}
