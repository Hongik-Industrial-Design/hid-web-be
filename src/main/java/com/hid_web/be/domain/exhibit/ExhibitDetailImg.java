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
public class ExhibitDetailImg {
    private MultipartFile file;
    private String url;
    private int position;
    private ExhibitImgType type;

    public ExhibitDetailImg(MultipartFile file, String url, int position) {
        this.file = file;
        this.url = url;
        this.position = position;
    }
}

