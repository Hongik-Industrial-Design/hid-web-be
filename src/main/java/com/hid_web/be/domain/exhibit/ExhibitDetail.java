package com.hid_web.be.domain.exhibit;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitDetail {
    private String titleKo;
    private String titleEn;
    private String subtitleKo;
    private String subtitleEn;
    private String textKo;
    private String textEn;
    private String videoUrl;
}
