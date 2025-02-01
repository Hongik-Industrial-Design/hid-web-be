package com.hid_web.be.storage.community;

import com.hid_web.be.domain.community.NewsEventCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 36)
    private String uuid;

    @Column(name = "thumbnail_url", nullable = false, length = 500)
    private String thumbnailUrl;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(nullable = false, length = 255)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NewsEventCategory category;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ElementCollection
    @CollectionTable(name = "news_event_attachments", joinColumns = @JoinColumn(name = "news_event_id"))
    @Column(name = "attachment_url", length = 500)
    private List<String> attachmentUrls = new ArrayList<>(); // 복수 첨부파일 저장

    @ElementCollection
    @CollectionTable(name = "news_event_images", joinColumns = @JoinColumn(name = "news_event_id"))
    @Column(name = "image_url", length = 500)
    private List<String> imageUrls = new ArrayList<>(); // 복수 이미지 저장

    @Column(nullable = false)
    private int views;
}
