package com.hid_web.be.domain.exhibit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitDetailImage {
    private MultipartFile file;
    private String url;
    private int position;
    private ExhibitImageType type;
}

