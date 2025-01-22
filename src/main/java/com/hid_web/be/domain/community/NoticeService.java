package com.hid_web.be.domain.community;

import com.hid_web.be.controller.community.response.NoticeDetailResponse;
import com.hid_web.be.controller.community.response.NoticeResponse;
import com.hid_web.be.storage.community.NoticeEntity;
import com.hid_web.be.storage.community.NoticeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public List<NoticeResponse> getAllNotices() {

        List<NoticeEntity> noticeEntities = noticeRepository.findAll();

        return noticeEntities.stream()
                .map(notice -> new NoticeResponse(
                        notice.getId(),
                        notice.getTitle(),
                        notice.getAuthor(),
                        notice.getCreatedDate(),
                        notice.getAttachmentUrl()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public NoticeDetailResponse getNoticeDetail(Long id) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found"));

        // 조회수 증가
        notice.setViews(notice.getViews() + 1);

        return new NoticeDetailResponse(notice);
    }
}
