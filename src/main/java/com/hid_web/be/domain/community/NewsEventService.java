package com.hid_web.be.domain.community;

import com.hid_web.be.controller.community.request.CreateNewsEventRequest;
import com.hid_web.be.controller.community.request.UpdateNewsEventRequest;
import com.hid_web.be.controller.community.response.CustomPageResponse;
import com.hid_web.be.controller.community.response.NewsEventDetailResponse;
import com.hid_web.be.controller.community.response.NewsEventResponse;
import com.hid_web.be.domain.s3.S3Uploader;
import com.hid_web.be.storage.community.NewsEventEntity;
import com.hid_web.be.storage.community.NewsEventRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsEventService {
    private final NewsEventRepository newsEventRepository;
    private final S3Uploader s3Uploader;

    public CustomPageResponse<NewsEventResponse> getAllNewsEvents(Pageable pageable) {
        Page<NewsEventEntity> newsEventPage = newsEventRepository.findTop12ByOrderByCreatedDateDescIdDesc(pageable);
        List<NewsEventResponse> content = newsEventPage.stream()
                .map(NewsEventResponse::new)
                .collect(Collectors.toList());

        return new CustomPageResponse<>(content, new CustomPageResponse.PageInfo(
                newsEventPage.getNumber() + 1,
                newsEventPage.getTotalPages(),
                newsEventPage.getTotalElements(),
                newsEventPage.isFirst(),
                newsEventPage.isLast()
        ));
    }

    public NewsEventDetailResponse getNewsEventDetail(Long newsEventId) {
        NewsEventEntity newsEvent = newsEventRepository.findById(newsEventId)
                .orElseThrow(() -> new RuntimeException("뉴스 & 이벤트가 존재하지 않습니다. ID: " + newsEventId));

        newsEvent.setViews(newsEvent.getViews() + 1);

        return new NewsEventDetailResponse(newsEvent);
    }

    @Transactional
    public NewsEventDetailResponse createNewsEvent(CreateNewsEventRequest request) throws IOException {
        String newsEventUUID = UUID.randomUUID().toString();

        // S3 업로드 (UUID 기반 저장)
        String thumbnailUrl = null;

        if (request.getThumbnail() != null) {
            thumbnailUrl = s3Uploader.uploadFile(request.getThumbnail(), "newsEvent/" + newsEventUUID + "/thumbnails");
        }

        Map<String, String> imageFiles = request.getImages() != null
                ? s3Uploader.uploadFiles(request.getImages(), "newsEvent/" + newsEventUUID + "/images")
                : new HashMap<>();

        Map<String, String> attachmentFiles = request.getAttachments() != null
                ? s3Uploader.uploadFiles(request.getAttachments(), "newsEvent/" + newsEventUUID + "/attachments")
                : new HashMap<>();

        NewsEventEntity newsEvent = NewsEventEntity.builder()
                .uuid(newsEventUUID)
                .thumbnailUrl(thumbnailUrl)
                .createdDate(LocalDate.now())
                .title(request.getTitle())
                .category(request.getCategory())
                .content(request.getContent())
                .attachmentUrls(new ArrayList<>(attachmentFiles.keySet())) // S3 URL 저장
                .imageUrls(new ArrayList<>(imageFiles.keySet())) // S3 URL 저장
                .views(0) // 초기 조회수
                .build();

        // DB 저장
        newsEvent = newsEventRepository.save(newsEvent);

        return new NewsEventDetailResponse(newsEvent);
    }

    @Transactional
    public NewsEventDetailResponse updateNewsEvent(Long id, UpdateNewsEventRequest request) throws IOException, URISyntaxException {
        NewsEventEntity newsEvent = newsEventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("뉴스이벤트를 찾을 수 없습니다."));

        // S3 썸네일 업데이트
        if (request.getThumbnail() != null) {
            s3Uploader.deleteFile(newsEvent.getThumbnailUrl());
            newsEvent.setThumbnailUrl(s3Uploader.uploadFile(request.getThumbnail(), "newsEvent/" + newsEvent.getUuid() + "/thumbnails"));
        }

        // S3 이미지 업데이트
        if (request.getImages() != null) {
            s3Uploader.deleteFiles(newsEvent.getImageUrls());
            newsEvent.setImageUrls(new ArrayList<>());
        }

        // S3 첨부파일 업데이트
        if (request.getAttachments() != null) {
            s3Uploader.deleteFiles(newsEvent.getAttachmentUrls());
            newsEvent.setAttachmentUrls(new ArrayList<>());
        }

        // 새 파일 업로드
        Map<String, String> newImageFiles = request.getImages() != null
                ? s3Uploader.uploadFiles(request.getImages(), "newsEvent/" + newsEvent.getUuid() + "/images")
                : new HashMap<>();

        Map<String, String> newAttachmentFiles = request.getAttachments() != null
                ? s3Uploader.uploadFiles(request.getAttachments(), "newsEvent/" + newsEvent.getUuid() + "/attachments")
                : new HashMap<>();

        newsEvent = NewsEventEntity.builder()
                .id(newsEvent.getId()) // 기존 ID 유지
                .uuid(newsEvent.getUuid()) // 기존 UUID 유지
                .thumbnailUrl(newsEvent.getThumbnailUrl())
                .createdDate(newsEvent.getCreatedDate())
                .title(request.getTitle())
                .category(request.getCategory())
                .content(request.getContent())
                .attachmentUrls(new ArrayList<>(newAttachmentFiles.keySet())) // S3 URL 저장
                .imageUrls(new ArrayList<>(newImageFiles.keySet())) // S3 URL 저장
                .views(newsEvent.getViews()) // 기존 조회수 유지
                .build();

        // DB 업데이트
        newsEvent = newsEventRepository.save(newsEvent);

        return new NewsEventDetailResponse(newsEvent);
    }

    public void deleteNewsEvent(Long id) {
        NewsEventEntity newsEvent = newsEventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("뉴스이벤트를 찾을 수 없습니다."));

        s3Uploader.deleteFile(newsEvent.getThumbnailUrl());
        s3Uploader.deleteFiles(newsEvent.getAttachmentUrls());
        s3Uploader.deleteFiles(newsEvent.getImageUrls());

        // DB에서 뉴스이벤트 삭제
        newsEventRepository.deleteById(id);
    }

    @Transactional
    public void deleteNewsEvents(List<Long> newsEventIds) {
        List<NewsEventEntity> newsEvents = newsEventRepository.findAllById(newsEventIds);
        for (NewsEventEntity newsEvent : newsEvents) {
            if (newsEvent.getAttachmentUrls() != null) {
                s3Uploader.deleteFiles(newsEvent.getAttachmentUrls());
            }
            if (newsEvent.getImageUrls() != null) {
                s3Uploader.deleteFiles(newsEvent.getImageUrls());
            }
        }
        newsEventRepository.deleteAllById(newsEventIds);
    }
}
