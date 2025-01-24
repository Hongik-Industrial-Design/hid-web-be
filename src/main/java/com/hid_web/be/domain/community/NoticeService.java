package com.hid_web.be.domain.community;

import com.hid_web.be.controller.community.response.NoticeDetailResponse;
import com.hid_web.be.controller.community.response.NoticeResponse;
import com.hid_web.be.storage.community.NoticeEntity;
import com.hid_web.be.storage.community.NoticeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public Page<NoticeResponse> getAllNotices(Pageable pageable) {
        Page<NoticeEntity> noticeEntities = noticeRepository.findAllByOrderByIsImportantDescCreatedDateDesc(pageable);

        return noticeEntities.map(NoticeResponse::new);
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
