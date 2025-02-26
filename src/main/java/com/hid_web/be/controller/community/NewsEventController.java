package com.hid_web.be.controller.community;

import com.hid_web.be.controller.community.request.CreateNewsEventRequest;
import com.hid_web.be.controller.community.request.UpdateNewsEventRequest;
import com.hid_web.be.controller.community.response.CustomPageResponse;
import com.hid_web.be.controller.community.response.NewsEventDetailResponse;
import com.hid_web.be.controller.community.response.NewsEventResponse;
import com.hid_web.be.domain.community.NewsEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/newsEvent")
@RequiredArgsConstructor
public class NewsEventController {
    private final NewsEventService newsEventService;

    @GetMapping
    public CustomPageResponse<NewsEventResponse> getAllNewsEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return newsEventService.getAllNewsEvents(pageable);
    }

    @GetMapping("/{newsEventId}")
    public NewsEventDetailResponse getNewsEventDetail(@PathVariable Long newsEventId) {
        return newsEventService.getNewsEventDetail(newsEventId);
    }

    @PostMapping
    public ResponseEntity<NewsEventDetailResponse> createNewsEvent(@ModelAttribute @Valid CreateNewsEventRequest request) throws IOException {
        return ResponseEntity.ok(newsEventService.createNewsEvent(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsEventDetailResponse> updateNewsEvent(
            @PathVariable Long id,
            @ModelAttribute @Valid UpdateNewsEventRequest request
    ) throws IOException, URISyntaxException {
        return ResponseEntity.ok(newsEventService.updateNewsEvent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewsEvent(@PathVariable Long id) {
        newsEventService.deleteNewsEvent(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNewsEvents(@RequestParam List<Long> newsEventIds) {
        newsEventService.deleteNewsEvents(newsEventIds);
        return ResponseEntity.noContent().build();
    }

}
