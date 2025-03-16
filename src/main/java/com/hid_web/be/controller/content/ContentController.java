package com.hid_web.be.controller.content;

import com.hid_web.be.controller.content.response.ContentMainVideoResponse;
import com.hid_web.be.domain.content.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contents")
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/main-video/{exhibitYear}")
    public ResponseEntity<ContentMainVideoResponse> findMainVideoByYear(@PathVariable("exhibitYear") Long year) {
        ContentMainVideoResponse video = contentService.findMainVideoByYear(year);
        return ResponseEntity.ok(video);
    }
}
