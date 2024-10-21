package com.hid_web.be.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitDetailRequest {
    private String titleKo;
    private String titleEn;
    private String subtitleKo;
    private String subtitleEn;
    private String textKo;
    private String textEn;
    private String videoUrl;
}

