package com.hid_web.be.controller.community;

import com.hid_web.be.controller.community.response.NewsEventResponse;
import com.hid_web.be.domain.community.NewsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/newsEvent")
@RequiredArgsConstructor
public class NewsEventController {
    private final NewsEventService newsEventService;

    @GetMapping
    public List<NewsEventResponse> getAllNewsEvents() {
        return newsEventService.getAllNewsEvents();
    }

}
