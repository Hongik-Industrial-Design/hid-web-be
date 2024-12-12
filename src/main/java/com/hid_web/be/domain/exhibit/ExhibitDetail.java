package com.hid_web.be.domain.exhibit;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitDetail {
    private String titleKo;
    private String titleEn;
    private String subTitleKo;
    private String subTitleEn;
    private String textKo;
    private String textEn;
    private String videoUrl;
}
