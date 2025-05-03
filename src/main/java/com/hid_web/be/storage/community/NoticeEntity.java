package com.hid_web.be.storage.community;

import com.hid_web.be.domain.community.NoticeAuthorType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 36) // UUID 저장
    private String uuid;

    @Column(nullable = false, length = 255)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "author", nullable = false)
    private NoticeAuthorType author;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(nullable = false)
    private int views;

    @ElementCollection
    @CollectionTable(name = "notice_attachments", joinColumns = @JoinColumn(name = "notice_id"))
    @Column(name = "attachment_url", length = 500)
    private List<String> attachmentUrls = new ArrayList<>(); // 복수 첨부파일 저장

    @ElementCollection
    @CollectionTable(name = "notice_images", joinColumns = @JoinColumn(name = "notice_id"))
    @Column(name = "image_url", length = 500)
    private List<String> imageUrls = new ArrayList<>(); // 복수 이미지 저장

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_important", nullable = false)
    private boolean isImportant;

}
