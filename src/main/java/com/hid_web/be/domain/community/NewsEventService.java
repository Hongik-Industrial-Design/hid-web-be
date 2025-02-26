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
import java.util.List;
import java.util.UUID;
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

        // S3 업로드
        String thumbnailUrl = request.getThumbnail() != null ?
                s3Uploader.uploadFile(request.getThumbnail(), "newsEvent/" + newsEventUUID + "/thumbnails") : null;

        List<String> imageUrls = request.getImages() != null ?
                s3Uploader.uploadFiles(request.getImages(), "newsEvent/" + newsEventUUID + "/images") : null;

        List<String> attachmentUrls = request.getAttachments() != null ?
                s3Uploader.uploadFiles(request.getAttachments(), "newsEvent/" + newsEventUUID + "/attachments") : null;

        // DB 저장
        NewsEventEntity newsEvent = newsEventRepository.save(NewsEventEntity.builder()
                .uuid(newsEventUUID)
                .title(request.getTitle())
                .category(request.getCategory())
                .createdDate(LocalDate.now())
                .thumbnailUrl(thumbnailUrl)
                .imageUrls(imageUrls)
                .attachmentUrls(attachmentUrls)
                .content(request.getContent())
                .views(0)
                .build());

        return new NewsEventDetailResponse(newsEvent);
    }

    @Transactional
    public NewsEventDetailResponse updateNewsEvent(Long id, UpdateNewsEventRequest request) throws IOException, URISyntaxException {
        NewsEventEntity newsEvent = newsEventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("뉴스이벤트를 찾을 수 없습니다."));

        // S3 업로드 (기존 파일 삭제 후 업로드)
        if (request.getThumbnail() != null) {
            s3Uploader.deleteFile(newsEvent.getThumbnailUrl());
            newsEvent.setThumbnailUrl(s3Uploader.uploadFile(request.getThumbnail(), "newsEvent/" + newsEvent.getUuid() + "/thumbnails"));
        }

        if (request.getImages() != null) {
            s3Uploader.deleteFiles(newsEvent.getImageUrls());
            newsEvent.setImageUrls(s3Uploader.uploadFiles(request.getImages(), "newsEvent/" + newsEvent.getUuid() + "/images"));
        }

        if (request.getAttachments() != null) {
            s3Uploader.deleteFiles(newsEvent.getAttachmentUrls());
            newsEvent.setAttachmentUrls(s3Uploader.uploadFiles(request.getAttachments(), "newsEvent/" + newsEvent.getUuid() + "/attachments"));
        }

        // 정보 업데이트
        newsEvent.setTitle(request.getTitle());
        newsEvent.setCategory(request.getCategory());
        newsEvent.setContent(request.getContent());

        return new NewsEventDetailResponse(newsEvent);
    }

    public void deleteNewsEvent(Long id) {
        NewsEventEntity newsEvent = newsEventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("뉴스이벤트를 찾을 수 없습니다."));

        // S3 첨부파일 및 이미지 삭제
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
