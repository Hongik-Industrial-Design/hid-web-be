package com.hid_web.be.controller.request;

import org.springframework.web.multipart.MultipartFile;
import lombok.*;

@Getter
@Setter
public class CreateExhibitDetailImgRequest {
    private MultipartFile file;
    private String url;
    private int position;
}