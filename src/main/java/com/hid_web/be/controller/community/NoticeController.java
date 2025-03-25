package com.hid_web.be.controller.community;

import com.hid_web.be.controller.community.request.CreateNoticeRequest;
import com.hid_web.be.controller.community.request.UpdateNoticeRequest;
import com.hid_web.be.controller.community.response.CustomPageResponse;
import com.hid_web.be.controller.community.response.NoticeDetailResponse;
import com.hid_web.be.controller.community.response.NoticeResponse;
import com.hid_web.be.domain.community.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public CustomPageResponse<NoticeResponse> getAllNotices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdDate").descending());
        return noticeService.getAllNotices(pageable);
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponse> getNoticeDetail(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getNoticeDetail(noticeId));
    }

    // 공지사항 첨부파일 개별 다운로드 API 추가
    @GetMapping("/{noticeId}/attachment-urls")
    public ResponseEntity<Map<String, String>> getAttachmentUrls(@PathVariable Long noticeId) throws FileNotFoundException, URISyntaxException {
        return noticeService.getAllAttachmentUrls(noticeId);
    }

    @PostMapping
    public ResponseEntity<NoticeDetailResponse> createNotice(@Valid @ModelAttribute CreateNoticeRequest request) throws IOException {
        return ResponseEntity.ok(noticeService.createNotice(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticeDetailResponse> updateNotice(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateNoticeRequest request
    ) throws IOException {
        return ResponseEntity.ok(noticeService.updateNotice(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }

    // 복수개 공지사항 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteNotices(@RequestParam List<Long> noticeIds) {
        noticeService.deleteNotices(noticeIds);
        return ResponseEntity.noContent().build();
    }

}
