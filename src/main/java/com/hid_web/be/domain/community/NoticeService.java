package com.hid_web.be.domain.community;

import com.hid_web.be.controller.community.response.NoticeDetailResponse;
import com.hid_web.be.controller.community.response.NoticeResponse;
import com.hid_web.be.domain.s3.S3Uploader;
import com.hid_web.be.storage.community.NoticeEntity;
import com.hid_web.be.storage.community.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final S3Uploader s3Uploader;

    public List<NoticeResponse> getAllNotices(Pageable pageable) {
        List<NoticeEntity> importantNotices = noticeRepository.findTop3ByIsImportantTrueOrderByCreatedDateDescIdDesc();

        int remainingCount = pageable.getPageSize() - importantNotices.size();
        Pageable generalPageable = PageRequest.of(pageable.getPageNumber(), remainingCount);
        List<NoticeEntity> generalNotices = noticeRepository.findByIsImportantFalseOrderByCreatedDateDescIdDesc(generalPageable);

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

    @Transactional
    public NoticeEntity createNotice(String title, String author,
                                     List<MultipartFile> images,
                                     List<MultipartFile> attachments,
                                     String content, boolean isImportant) throws IOException {

        String noticeUUID = UUID.randomUUID().toString();  // UUID 생성

        // S3 업로드 (이미지 & 첨부파일)
        List<String> imageUrls = images != null ? s3Uploader.uploadFiles(images, "notice/" + noticeUUID + "/images") : null;
        List<String> attachmentUrls = attachments != null ? s3Uploader.uploadFiles(attachments, "notice/" + noticeUUID + "/attachments") : null;

        // DB 저장
        NoticeEntity notice = NoticeEntity.builder()
                .uuid(noticeUUID)
                .title(title)
                .author(author)
                .createdDate(LocalDate.now())
                .imageUrls(imageUrls)
                .attachmentUrls(attachmentUrls)
                .content(content)
                .isImportant(isImportant)
                .views(0)
                .build();

        return noticeRepository.save(notice);
    }

    public void deleteNotice(Long id) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));

        // S3에 저장된 첨부파일 및 이미지 삭제
        s3Uploader.deleteFiles(notice.getAttachmentUrls());
        s3Uploader.deleteFiles(notice.getImageUrls());

        // DB에서 공지사항 삭제
        noticeRepository.deleteById(id);
    }

}
