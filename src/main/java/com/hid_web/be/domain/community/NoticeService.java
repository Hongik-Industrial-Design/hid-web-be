package com.hid_web.be.domain.community;

import com.hid_web.be.controller.community.response.NoticeDetailResponse;
import com.hid_web.be.controller.community.response.NoticeResponse;
import com.hid_web.be.storage.community.NoticeEntity;
import com.hid_web.be.storage.community.NoticeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public List<NoticeResponse> getAllNotices(Pageable pageable) {
        List<NoticeEntity> importantNotices = noticeRepository.findTop3ByIsImportantTrueOrderByCreatedDateDesc();

        int remainingCount = pageable.getPageSize() - importantNotices.size();
        Pageable generalPageable = PageRequest.of(pageable.getPageNumber(), remainingCount);
        List<NoticeEntity> generalNotices = noticeRepository.findByIsImportantFalseOrderByCreatedDateDesc(generalPageable);

        List<NoticeResponse> allNotices = new ArrayList<>();
        importantNotices.forEach(notice -> allNotices.add(new NoticeResponse(notice)));
        generalNotices.forEach(notice -> allNotices.add(new NoticeResponse(notice)));

        return allNotices;
    }

    @Transactional
    public NoticeDetailResponse getNoticeDetail(Long noticeId) {
        NoticeEntity notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("Notice not found"));

        // 조회수 증가
        notice.setViews(notice.getViews() + 1);

        return new NoticeDetailResponse(notice);
    }
}
