package com.hid_web.be.storage.professor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "professors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorEntity {

    /*
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name= "uuid", strategy = "uuid2") //uuid4 자동 생성
    @Column(columnDefinition = "BINARY(16)")
     */
    @Id
    private String uuid;

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

    private String imgObjectKey;

    @Column
    private int sortOrder;  //정렬 방식

    // 관계 설정
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BiographyEntryEntity> biographyEntries;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AwardEntity> awards;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkEntity> works;
}
