package com.hid_web.be.controller.community;

import com.hid_web.be.controller.community.response.NoticeDetailResponse;
import com.hid_web.be.controller.community.response.NoticeResponse;
import com.hid_web.be.domain.community.NoticeService;
import com.hid_web.be.storage.community.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping
    public Page<NoticeResponse> getAllNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return noticeService.getAllNotices(pageable);
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponse> getNoticeDetail(@PathVariable Long noticeId) {
        NoticeDetailResponse response = noticeService.getNoticeDetail(noticeId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<NoticeEntity> createNotice(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments,
            @RequestParam("content") String content,
            @RequestParam(value = "isImportant", defaultValue = "false") boolean isImportant
    ) throws IOException {
        // 빈 파일 필터링
        images = images != null ? images.stream().filter(file -> !file.isEmpty()).toList() : null;
        attachments = attachments != null ? attachments.stream().filter(file -> !file.isEmpty()).toList() : null;

        return ResponseEntity.ok(noticeService.createNotice(title, author, images, attachments, content, isImportant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }

}
