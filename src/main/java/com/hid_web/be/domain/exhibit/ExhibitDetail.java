package com.hid_web.be.domain.exhibit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExhibitDetail {
    private ExhibitType type;
    private String year;
    private String major;
    private String club;
    private String behanceUrl;
    private String instagramUrl;
    private String titleKo;
    private String titleEn;
    private String subTitleKo;
    private String subTitleEn;
    private String descriptionKo;
    private String descriptionEn;
    private String videoUrl;

    public ExhibitDetail(String year,  String behanceUrl, String instagramUrl, String titleKo, String titleEn, String subTitleKo, String subTitleEn, String descriptionKo, String descriptionEn, String videoUrl) {
        this.year = year;
        this.behanceUrl = behanceUrl;
        this.instagramUrl = instagramUrl;
        this.titleKo = titleKo;
        this.titleEn = titleEn;
        this.subTitleKo = subTitleKo;
        this.subTitleEn = subTitleEn;
        this.descriptionKo = descriptionKo;
        this.descriptionEn = descriptionEn;
        this.videoUrl = videoUrl;
    }
}
