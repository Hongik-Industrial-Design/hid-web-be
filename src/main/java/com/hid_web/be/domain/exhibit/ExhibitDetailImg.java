package com.hid_web.be.domain.exhibit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class ExhibitDetailImg {
    private MultipartFile file;
    private String url;
    private int position;
    private ExhibitImgType type;

    public ExhibitDetailImg(MultipartFile file, int position) {
        this.file = file;
        this.position = position;
    }
}

