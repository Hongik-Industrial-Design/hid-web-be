package com.hid_web.be.controller.request;

import com.hid_web.be.domain.exhibit.ExhibitImgType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExhibitDetailImgRequest {
    private MultipartFile file;
    private String url;
    private int position;
    private ExhibitImgType type;
}