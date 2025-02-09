package com.hid_web.be.domain.community;

import com.hid_web.be.controller.community.request.CreateNoticeRequest;
import com.hid_web.be.controller.community.request.UpdateNoticeRequest;
import com.hid_web.be.controller.community.response.NoticeDetailResponse;
import com.hid_web.be.controller.community.response.NoticeResponse;
import com.hid_web.be.domain.s3.S3Uploader;
import com.hid_web.be.storage.community.NoticeEntity;
import com.hid_web.be.storage.community.NoticeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final S3Uploader s3Uploader;

    public Page<NoticeResponse> getAllNotices(Pageable pageable) {
        List<NoticeResponse> allNotices = new ArrayList<>();
        long totalElements = noticeRepository.count(); // 전체 데이터 개수

        if (pageable.getPageNumber() == 0) { // 첫 번째 페이지
            // 중요 공지 최대 3개 조회
            List<NoticeEntity> importantNotices = noticeRepository.findTop3ByIsImportantTrueOrderByCreatedDateDescIdDesc();
            importantNotices.forEach(notice -> allNotices.add(new NoticeResponse(notice)));

            // 중요 공지 ID 수집
            Set<Long> importantNoticeIds = importantNotices.stream()
                    .map(NoticeEntity::getId)
                    .collect(Collectors.toSet());

            // 나머지 공지 조회 (중복 제거 없이 우선 전체 데이터 가져오기)
            Pageable remainingPageable = PageRequest.of(0, pageable.getPageSize());
            Page<NoticeEntity> remainingNotices = noticeRepository.findAllByOrderByCreatedDateDescIdDesc(remainingPageable);

            // 문제 해결: 중복 제거 로직 수정
            for (NoticeEntity notice : remainingNotices) {
                if (!importantNoticeIds.contains(notice.getId()) || importantNoticeIds.isEmpty()) {
                    allNotices.add(new NoticeResponse(notice));
                }
            }

        } else { // 두 번째 페이지부터는 중요/일반 구분 없이 표시
            Page<NoticeEntity> notices = noticeRepository.findAllByOrderByCreatedDateDescIdDesc(pageable);
            notices.forEach(notice -> allNotices.add(new NoticeResponse(notice)));
        }

        return new PageImpl<>(allNotices, pageable, totalElements);

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
    public NoticeEntity createNotice(CreateNoticeRequest request) throws IOException {

        // 필수값 검증
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수 입력 값입니다.");
        }

        if (request.getAuthor() == null || request.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("작성자는 필수 입력 값입니다.");
        }

        String noticeUUID = UUID.randomUUID().toString();  // UUID 생성

        // S3 업로드 (이미지 & 첨부파일)
        List<String> imageUrls = request.getImages() != null
                ? s3Uploader.uploadFiles(request.getImages(), "notice/" + noticeUUID + "/images")
                : null;

        List<String> attachmentUrls = request.getAttachments() != null
                ? s3Uploader.uploadFiles(request.getAttachments(), "notice/" + noticeUUID + "/attachments")
                : null;

        // DB 저장
        NoticeEntity notice = new NoticeEntity();
        notice.setUuid(noticeUUID);
        notice.setTitle(request.getTitle());
        notice.setAuthor(request.getAuthorEnum());
        notice.setCreatedDate(LocalDate.now());
        notice.setImageUrls(imageUrls);
        notice.setAttachmentUrls(attachmentUrls);
        notice.setContent(request.getContent());
        notice.setImportant(request.getIsImportant());
        notice.setViews(0);

        return noticeRepository.save(notice);
    }


    @Transactional
    public NoticeEntity updateNotice(Long id, UpdateNoticeRequest request) throws IOException {
        // 기존 공지사항 조회
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));

        // 필수값 검증
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수 입력 값입니다.");
        }

        if (request.getAuthor() == null || request.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("작성자는 필수 입력 값입니다.");
        }

        // 기존 S3 이미지 및 첨부파일 삭제 (새로운 파일이 들어올 경우만)
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            s3Uploader.deleteFiles(notice.getImageUrls()); // 기존 이미지 삭제
        }
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            s3Uploader.deleteFiles(notice.getAttachmentUrls()); // 기존 첨부파일 삭제
        }

        // 새 이미지 & 첨부파일 S3 업로드
        List<String> newImageUrls = request.getImages() != null ?
                s3Uploader.uploadFiles(request.getImages(), "notice/" + notice.getUuid() + "/images") : notice.getImageUrls();

        List<String> newAttachmentUrls = request.getAttachments() != null ?
                s3Uploader.uploadFiles(request.getAttachments(), "notice/" + notice.getUuid() + "/attachments") : notice.getAttachmentUrls();

        // 공지사항 정보 업데이트
        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
        notice.setAuthor(request.getAuthorEnum());
        notice.setImageUrls(newImageUrls);
        notice.setAttachmentUrls(newAttachmentUrls);
        notice.setImportant(request.getIsImportant());

        return noticeRepository.save(notice);
    }

    @Transactional
    public void deleteNotice(Long id) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("공지사항을 찾을 수 없습니다."));

        // S3에서 이미지 및 첨부파일 삭제
        if (notice.getAttachmentUrls() != null) {
            s3Uploader.deleteFiles(notice.getAttachmentUrls());
        }
        if (notice.getImageUrls() != null) {
            s3Uploader.deleteFiles(notice.getImageUrls());
        }

        // DB에서 공지사항 삭제
        noticeRepository.deleteById(id);
    }

    // 복수개 공지사항 삭제
    @Transactional
    public void deleteNotices(List<Long> noticeIds) {
        List<NoticeEntity> notices = noticeRepository.findAllById(noticeIds);
        for (NoticeEntity notice : notices) {
            if (notice.getAttachmentUrls() != null) {
                s3Uploader.deleteFiles(notice.getAttachmentUrls());
            }
            if (notice.getImageUrls() != null) {
                s3Uploader.deleteFiles(notice.getImageUrls());
            }
        }
        noticeRepository.deleteAllById(noticeIds);
    }

}
