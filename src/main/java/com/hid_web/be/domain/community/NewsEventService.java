package com.hid_web.be.domain.community;

import com.hid_web.be.controller.community.response.NewsEventResponse;
import com.hid_web.be.storage.community.NewsEventEntity;
import com.hid_web.be.storage.community.NewsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsEventService {
    private final NewsEventRepository newsEventRepository;

    public List<NewsEventResponse> getAllNewsEvents() {
        List<NewsEventEntity> newsEventEntities = newsEventRepository.findAll();

        return newsEventEntities.stream()
                .map(NewsEventResponse::new)
                .collect(Collectors.toList());
    }

}
