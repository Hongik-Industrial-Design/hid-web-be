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

    private String exhibitUUID;

    private String mainThumbnailImageUrl;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "exhibit_id")
    private List<ExhibitAdditionalThumbnailEntity> exhibitAdditionalThumbnailImageEntityList= new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "exhibit_id")
    private List<ExhibitDetailImageEntity> exhibitDetailImageEntityList = new ArrayList<>();

    @Column(length = 100)
    private String titleKo;

    @Column(length = 100)
    private String titleEn;

    @Column(length = 200)
    private String subtitleKo;

    @Column(length = 200)
    private String subtitleEn;

    @Column(length = 300)
    private String textKo;

    @Column(length = 700)
    private String textEn;

    private String videoUrl;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="exhibit_id")
    private List<ExhibitArtistEntity> exhibitArtistEntityList = new ArrayList<>();
}

