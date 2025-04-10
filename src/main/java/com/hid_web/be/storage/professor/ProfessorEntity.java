package com.hid_web.be.storage.professor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "professors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorEntity {

    @Id
    private UUID uuid;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 50)
    private String position;

    @Column(length = 50)
    private String department;

    @Column(columnDefinition = "TEXT")
    private String detailInfo;

    @Column(length = 100)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String major;

    // 관계 설정
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BiographyEntryEntity> biographyEntries;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AwardEntity> awards;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkEntity> works;
}
