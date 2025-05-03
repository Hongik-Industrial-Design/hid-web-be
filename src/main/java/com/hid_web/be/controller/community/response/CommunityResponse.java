package com.hid_web.be.controller.community.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityResponse {
    private List<NoticeResponse> notices;
    private List<NewsEventResponse> newsEvents;
}
