package com.hid_web.be.domain.content;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ContentMainVideo {
    private Long id;
    private String title;
    private String s3ObjectKey;
    private int year;
}
