package com.hid_web.be.controller.community;

import com.hid_web.be.controller.community.response.NoticeResponse;
import com.hid_web.be.domain.community.NoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping
    public ResponseEntity<List<NoticeResponse>> getAllNotices() {
        List<NoticeResponse> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }
}
