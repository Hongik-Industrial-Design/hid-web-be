package com.hid_web.be.controller.request;

import org.springframework.web.multipart.MultipartFile;
import lombok.*;

@Getter
@Setter
public class CreateExhibitSubImgRequest {
    private MultipartFile file;
    private int position;
}
