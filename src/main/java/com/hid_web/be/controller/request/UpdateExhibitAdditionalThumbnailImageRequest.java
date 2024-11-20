package com.hid_web.be.controller.request;

import com.hid_web.be.domain.exhibit.ExhibitImageType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExhibitAdditionalThumbnailImageRequest {
    private MultipartFile file;
    private String url;
    private int position;
    private ExhibitImageType type;
}

