package com.hid_web.be.controller.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExhibitDetailRequest {
    private String titleKo;
    private String titleEn;
    private String subtitleKo;
    private String subtitleEn;
    private String textKo;
    private String textEn;
    private String videoUrl;
}