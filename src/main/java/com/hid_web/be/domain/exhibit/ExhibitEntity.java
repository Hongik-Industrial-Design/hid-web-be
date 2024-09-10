package com.hid_web.be.domain.exhibit;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exhibitId; // 전시 번호

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="exhibit_id")
    private List<ExhibitArtistEntity> exhibitArtistEntityList = new ArrayList<>(); // 참여 학생 리스트

    private String text; // 텍스트
    private String imageUrl; // 이미지 URL
    private String videoUrl; // 영상 URL
}