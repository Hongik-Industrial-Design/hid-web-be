package com.hid_web.be.domain.community;

import com.hid_web.be.controller.community.response.NewsEventDetailResponse;
import com.hid_web.be.controller.community.response.NewsEventResponse;
import com.hid_web.be.storage.community.NewsEventEntity;
import com.hid_web.be.storage.community.NewsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsEventService {
    private final NewsEventRepository newsEventRepository;

    public Page<NewsEventResponse> getAllNewsEvents(Pageable pageable) {
        return newsEventRepository.findAll(pageable)
                .map(NewsEventResponse::new);
    }

    public NewsEventDetailResponse getNewsEventDetail(Long newsEventId) {
        NewsEventEntity newsEvent = newsEventRepository.findById(newsEventId)
                .orElseThrow(() -> new RuntimeException("뉴스 & 이벤트가 존재하지 않습니다. ID: " + newsEventId));

        newsEvent.setViews(newsEvent.getViews() + 1);

        return new NewsEventDetailResponse(newsEvent);
    }

}
