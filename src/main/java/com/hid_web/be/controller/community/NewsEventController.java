package com.hid_web.be.controller.community;

import com.hid_web.be.controller.community.response.NewsEventDetailResponse;
import com.hid_web.be.controller.community.response.NewsEventResponse;
import com.hid_web.be.domain.community.NewsEventService;
import com.hid_web.be.storage.community.NewsEventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/newsEvent")
@RequiredArgsConstructor
public class NewsEventController {
    private final NewsEventService newsEventService;

    @GetMapping
    public Page<NewsEventResponse> getAllNewsEvents(
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
    public ResponseEntity<NewsEventEntity> createNewsEvent(
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments,
            @RequestParam("content") String content
    ) throws IOException {
        // 빈 파일 검증 추가
        if (thumbnail != null && thumbnail.isEmpty()) {
            thumbnail = null;  // 빈 파일이면 null 처리
        }

        if (images != null) {
            images = images.stream()
                    .filter(file -> file != null && !file.isEmpty())  // 빈 파일 필터링
                    .collect(Collectors.toList());
        }

        if (attachments != null) {
            attachments = attachments.stream()
                    .filter(file -> file != null && !file.isEmpty())  // 빈 파일 필터링
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(newsEventService.createNewsEvent(title, category, thumbnail, images, attachments, content));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewsEvent(@PathVariable Long id) {
        newsEventService.deleteNewsEvent(id);
        return ResponseEntity.noContent().build();
    }

}
