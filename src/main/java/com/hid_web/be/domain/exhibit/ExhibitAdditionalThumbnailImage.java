package com.hid_web.be.domain.exhibit;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitAdditionalThumbnailImage {
    private MultipartFile file;
    private String url;
    private int position;
    private ExhibitImageType type;
}
