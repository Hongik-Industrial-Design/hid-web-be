package com.hid_web.be.controller.community;

import com.hid_web.be.controller.community.response.NewsEventDetailResponse;
import com.hid_web.be.controller.community.response.NewsEventResponse;
import com.hid_web.be.domain.community.NewsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/newsEvent")
@RequiredArgsConstructor
public class NewsEventController {
    private final NewsEventService newsEventService;

    @GetMapping
    public Page<NewsEventResponse> getAllNewsEvents(
            @PageableDefault(size = 12, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return newsEventService.getAllNewsEvents(pageable);
    }

    @GetMapping("/{newsEventId}")
    public NewsEventDetailResponse getNewsEventDetail(@PathVariable Long newsEventId) {
        return newsEventService.getNewsEventDetail(newsEventId);
    }
}
