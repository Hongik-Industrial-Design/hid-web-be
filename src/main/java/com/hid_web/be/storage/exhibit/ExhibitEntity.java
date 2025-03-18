package com.hid_web.be.storage.exhibit;

import com.hid_web.be.domain.exhibit.ExhibitType;
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

    @Column(name = "exhibit_uuid")
    private String exhibitUUID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExhibitType type;

    @Column(nullable = false)
    private int year;

    private String major;

    private String club;

    private String mainImgUrl;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "exhibit_id")
    private List<ExhibitSubImgEntity> subImgEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "exhibit_id")
    private List<ExhibitDetailImgEntity> detailImgEntities = new ArrayList<>();

    @Column(length = 100)
    private String titleKo;

    @Column(length = 100)
    private String titleEn;

    @Column(length = 200)
    private String subTitleKo;

    @Column(length = 200)
    private String subTitleEn;

    @Column(length = 300)
    private String textKo;

    @Column(length = 700)
    private String textEn;

    private String videoUrl;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="exhibit_id")
    private List<ExhibitArtistEntity> artistEntities = new ArrayList<>();
}

