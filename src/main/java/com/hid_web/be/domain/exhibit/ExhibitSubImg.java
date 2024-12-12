package com.hid_web.be.domain.exhibit;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitSubImg {
    private MultipartFile file;
    private String url;
    private int position;
    private ExhibitImgType type;

    public ExhibitSubImg(MultipartFile file, int position) {
        this.file = file;
        this.position = position;
    }
}
