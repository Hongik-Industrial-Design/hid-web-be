package com.hid_web.be.storage.community;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "notice")
public class NoticeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "has_attachment")
    private Boolean hasAttachment;
}
