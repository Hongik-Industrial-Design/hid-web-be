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
    private Long exhibitId;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="exhibit_id")
    private List<ExhibitArtistEntity> exhibitArtistEntityList = new ArrayList<>();

    private String mainThumbnailImageUrl;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "exhibit_id")
    private List<ExhibitAdditionalThumbnailEntity> additionalThumbnails = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "exhibit_id")
    private List<ExhibitDetailImageEntity> detailImages = new ArrayList<>();

    private String title;
    private String subtitle;
    private String text;
    private String imageUrl;
    private String videoUrl;
}
