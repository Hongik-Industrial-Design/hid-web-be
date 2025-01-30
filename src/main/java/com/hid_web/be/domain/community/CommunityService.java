package com.hid_web.be.domain.community;

import com.hid_web.be.controller.community.response.CommunityResponse;
import com.hid_web.be.controller.community.response.NewsEventResponse;
import com.hid_web.be.controller.community.response.NoticeResponse;
import com.hid_web.be.storage.community.NewsEventEntity;
import com.hid_web.be.storage.community.NewsEventRepository;
import com.hid_web.be.storage.community.NoticeEntity;
import com.hid_web.be.storage.community.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final NoticeRepository noticeRepository;
    private final NewsEventRepository newsEventRepository;

    public CommunityResponse getCommunityData() {
        List<NoticeEntity> importantNotices = noticeRepository.findTop1ByIsImportantTrueOrderByCreatedDateDesc();

        List<NoticeEntity> generalNotices = noticeRepository.findTop4ByIsImportantFalseOrderByCreatedDateDesc();

        List<NoticeEntity> allNotices = new ArrayList<>();
        allNotices.addAll(importantNotices);
        allNotices.addAll(generalNotices);

        List<NoticeResponse> notices = allNotices.stream()
                .map(NoticeResponse::new)
                .collect(Collectors.toList());

        List<NewsEventEntity> newsEventEntities = newsEventRepository.findTop8ByOrderByCreatedDateDesc();

        List<NewsEventResponse> newsEvents = newsEventEntities.stream()
                .map(NewsEventResponse::new)
                .collect(Collectors.toList());

        return CommunityResponse.builder()
                .notices(notices)
                .newsEvents(newsEvents)
                .build();
    }
}
