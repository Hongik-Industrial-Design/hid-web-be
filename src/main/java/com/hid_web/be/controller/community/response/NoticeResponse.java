package com.hid_web.be.controller.community.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeResponse {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdDate;
    private String attachmentUrl;

    public NoticeResponse(Long id, String title, String author, LocalDateTime createdDate, String attachmentUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.createdDate = createdDate;
        this.attachmentUrl = attachmentUrl;
    }

}
