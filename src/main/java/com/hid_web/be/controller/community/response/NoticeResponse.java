package com.hid_web.be.controller.community.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeResponse {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdDate;
    private Boolean hasAttachment;

    public NoticeResponse(Long id, String title, String author, LocalDateTime createdDate, Boolean hasAttachment) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.createdDate = createdDate;
        this.hasAttachment = hasAttachment;
    }

}
