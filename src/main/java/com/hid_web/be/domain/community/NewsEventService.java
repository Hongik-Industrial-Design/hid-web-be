package com.hid_web.be.domain.community;

import com.hid_web.be.controller.community.response.NewsEventDetailResponse;
import com.hid_web.be.controller.community.response.NewsEventResponse;
import com.hid_web.be.domain.s3.S3Uploader;
import com.hid_web.be.storage.community.NewsEventEntity;
import com.hid_web.be.storage.community.NewsEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsEventService {
    private final NewsEventRepository newsEventRepository;
    private final S3Uploader s3Uploader;

    public List<NewsEventResponse> getAllNewsEvents(Pageable pageable) {
        return newsEventRepository.findTop12ByOrderByCreatedDateDescIdDesc(pageable)
                .stream()
                .map(NewsEventResponse::new)
                .collect(Collectors.toList());
    }

//    public Page<NewsEventResponse> getAllNewsEvents(Pageable pageable) {
//        return newsEventRepository.findTop12ByOrderByCreatedDateDescIdAsc()
//                .stream()
//                .map(NewsEventResponse::new)
//                .collect(Collectors.toList());
//    }

    public NewsEventDetailResponse getNewsEventDetail(Long newsEventId) {
        NewsEventEntity newsEvent = newsEventRepository.findById(newsEventId)
                .orElseThrow(() -> new RuntimeException("뉴스 & 이벤트가 존재하지 않습니다. ID: " + newsEventId));

        newsEvent.setViews(newsEvent.getViews() + 1);

        return new NewsEventDetailResponse(newsEvent);
    }

    @Transactional
    public NewsEventEntity createNewsEvent(String title, String category, MultipartFile thumbnail,
                                           List<MultipartFile> images, List<MultipartFile> attachments,
                                           String content) throws IOException {

        String newsEventUUID = UUID.randomUUID().toString();  // UUID 생성

        // S3 업로드
        String thumbnailUrl = thumbnail != null ? s3Uploader.uploadFile(thumbnail, "newsEvent/" + newsEventUUID + "/thumbnails") : null;
        List<String> imageUrls = images != null ? s3Uploader.uploadFiles(images, "newsEvent/" + newsEventUUID + "/images") : null;
        List<String> attachmentUrls = attachments != null ? s3Uploader.uploadFiles(attachments, "newsEvent/" + newsEventUUID + "/attachments") : null;

        // DB 저장
        NewsEventEntity newsEvent = NewsEventEntity.builder()
                .uuid(newsEventUUID)
                .title(title)
                .category(NewsEventCategory.valueOf(category))
                .createdDate(LocalDate.now())
                .thumbnailUrl(thumbnailUrl)
                .imageUrls(imageUrls)
                .attachmentUrls(attachmentUrls)
                .content(content)
                .views(0)
                .build();

        return newsEventRepository.save(newsEvent);
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

}
