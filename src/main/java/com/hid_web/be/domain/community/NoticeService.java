package com.hid_web.be.domain.community;

import com.hid_web.be.controller.community.request.CreateNoticeRequest;
import com.hid_web.be.controller.community.request.UpdateNoticeRequest;
import com.hid_web.be.controller.community.response.CustomPageResponse;
import com.hid_web.be.controller.community.response.NoticeDetailResponse;
import com.hid_web.be.controller.community.response.NoticeResponse;
import com.hid_web.be.domain.s3.S3Uploader;
import com.hid_web.be.storage.community.NoticeEntity;
import com.hid_web.be.storage.community.NoticeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final S3Uploader s3Uploader;

    // 중요 공지 3개 유지 및 DB 업데이트
    public void maintainImportantNotices() {
        // 최신 중요 공지 3개 유지
        List<NoticeEntity> importantNotices = noticeRepository.findTop3ByIsImportantTrueOrderByCreatedDateDescIdDesc();

        // 모든 중요 공지 가져오기 (정렬: 오래된 순으로)
        List<NoticeEntity> allImportantNotices = noticeRepository.findByIsImportantTrueOrderByCreatedDateAscIdAsc();

        // 유지할 공지를 제외한 나머지를 일반 공지로 전환
        List<Long> importantNoticeIds = importantNotices.stream()
                .map(NoticeEntity::getId)
                .toList();

        for (NoticeEntity notice : allImportantNotices) {
            if (!importantNoticeIds.contains(notice.getId())) {
                notice.setImportant(false); // 오래된 중요 공지를 일반 공지로 전환
                noticeRepository.save(notice);
            }
        }
    }

    // 공지사항 전체 목록 조회
    public CustomPageResponse<NoticeResponse> getAllNotices(Pageable pageable) {
        // 중요 공지 상태 업데이트
        maintainImportantNotices();

        List<NoticeResponse> allNotices = new ArrayList<>();
        long totalElements = noticeRepository.count();

        // 중요 공지 최대 3개 조회
        List<NoticeEntity> importantNotices = noticeRepository.findTop3ByIsImportantTrueOrderByCreatedDateDescIdDesc();
        importantNotices.forEach(notice -> allNotices.add(new NoticeResponse(notice)));

        // 중요 공지 수
        int importantNoticeCount = importantNotices.size();

        // 일반 공지의 총 개수 계산
        long generalNoticeCount = noticeRepository.countByIsImportantFalse();

        // 한 페이지에서 일반 공지가 표시될 수 있는 최대 개수
        int generalNoticesPerPage = pageable.getPageSize() - importantNoticeCount;

        // 중요 공지를 제외한 일반 공지 페이징 처리
        Pageable generalNoticePageable = PageRequest.of(
                pageable.getPageNumber(), // 페이지 번호 그대로 사용
                generalNoticesPerPage, // 페이지 크기는 남은 공간만큼
                Sort.by("createdDate").descending().and(Sort.by("id").descending())
        );

        // 일반 공지 조회 (중요 공지 제외)
        Page<NoticeEntity> generalNotices = noticeRepository.findByIsImportantFalseOrderByCreatedDateDescIdDesc(generalNoticePageable);
        generalNotices.forEach(notice -> allNotices.add(new NoticeResponse(notice)));

        // 전체 페이지 수 계산 (일반 공지 기준으로 계산)
        int totalPages = (int) Math.ceil((double) generalNoticeCount / generalNoticesPerPage);

        // 페이지 정보 생성
        CustomPageResponse.PageInfo pageInfo = new CustomPageResponse.PageInfo(
                pageable.getPageNumber() + 1,
                totalPages,
                totalElements,
                pageable.getPageNumber() == 0,
                (pageable.getPageNumber() + 1) >= totalPages
        );

        return new CustomPageResponse<>(allNotices, pageInfo);
    }

    @Transactional(readOnly = true)
    public NoticeDetailResponse getNoticeDetail(Long noticeId) {
        NoticeEntity notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("Notice not found"));

        // 조회수 증가
        notice.setViews(notice.getViews() + 1);

        return new NoticeDetailResponse(notice);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, String>> getAllAttachmentUrls(Long noticeId) throws FileNotFoundException {

        NoticeEntity notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("공지사항을 찾을 수 없습니다."));

        List<String> attachmentKeys = notice.getAttachmentUrls();

        if (attachmentKeys == null || attachmentKeys.isEmpty()) {
            throw new FileNotFoundException("첨부파일이 존재하지 않습니다.");
        }

        Map<String, String> fileUrlMap = new HashMap<>();

        for (String fileKey : attachmentKeys) {
            String originalFileName = s3Uploader.extractOriginalFileName(fileKey);
            String cloudFrontUrl = s3Uploader.generateCloudFrontUrl(fileKey);

            fileUrlMap.put(cloudFrontUrl, originalFileName);

        }

        return ResponseEntity.ok(fileUrlMap);
    }

    @Transactional
    public NoticeDetailResponse createNotice(CreateNoticeRequest request) throws IOException {
        if (request.getIsImportant()) {
            maintainImportantNotices();
        }

        validateRequiredFields(request.getTitle(), request.getAuthor());

        String noticeUUID = UUID.randomUUID().toString();  // UUID 생성

        // S3 업로드 (이미지 & 첨부파일)
        Map<String, String> imageFiles = request.getImages() != null
                ? s3Uploader.uploadFiles(request.getImages(), "notice/" + noticeUUID + "/images")
                : new HashMap<>();

        Map<String, String> attachmentFiles = request.getAttachments() != null
                ? s3Uploader.uploadFiles(request.getAttachments(), "notice/" + noticeUUID + "/attachments")
                : new HashMap<>();

        NoticeEntity notice = new NoticeEntity();
        notice.setUuid(noticeUUID);
        notice.setTitle(request.getTitle());
        notice.setAuthor(request.getAuthorEnum());
        notice.setCreatedDate(LocalDate.now());
        notice.setImageUrls(new ArrayList<>(imageFiles.keySet()));
        notice.setAttachmentUrls(new ArrayList<>(attachmentFiles.keySet()));
        notice.setContent(request.getContent());
        notice.setImportant(request.getIsImportant());
        notice.setViews(0);

        notice = noticeRepository.save(notice);

        return new NoticeDetailResponse(notice);

    }


    @Transactional
    public NoticeDetailResponse updateNotice(Long id, UpdateNoticeRequest request) throws IOException {

        validateRequiredFields(request.getTitle(), request.getAuthor());

        // 기존 공지사항 조회
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));

        // 기존 S3 이미지 및 첨부파일 삭제 (새로운 파일이 들어올 경우만)
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            s3Uploader.deleteFiles(notice.getImageUrls()); // 기존 이미지 삭제
            notice.setImageUrls(new ArrayList<>()); // 기존 URL 제거
        }
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            s3Uploader.deleteFiles(notice.getAttachmentUrls()); // 기존 첨부파일 삭제
            notice.setAttachmentUrls(new ArrayList<>()); // 기존 URL 제거
        }

        // 새 이미지 & 첨부파일 S3 업로드 (UUID 기반 파일명 사용)
        Map<String, String> newImageFiles = request.getImages() != null
                ? s3Uploader.uploadFiles(request.getImages(), "notice/" + notice.getUuid() + "/images")
                : new HashMap<>();

        Map<String, String> newAttachmentFiles = request.getAttachments() != null
                ? s3Uploader.uploadFiles(request.getAttachments(), "notice/" + notice.getUuid() + "/attachments")
                : new HashMap<>();

        // 공지사항 정보 업데이트
        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
        notice.setAuthor(request.getAuthorEnum());
        notice.setImageUrls(new ArrayList<>(newImageFiles.keySet())); // S3 URL 저장
        notice.setAttachmentUrls(new ArrayList<>(newAttachmentFiles.keySet())); // S3 URL 저장
        notice.setImportant(request.getIsImportant());

        notice = noticeRepository.save(notice);

        return new NoticeDetailResponse(notice);
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

    private void validateRequiredFields(String title, String author) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수 입력 값입니다.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("작성자는 필수 입력 값입니다.");
        }
    }

}
