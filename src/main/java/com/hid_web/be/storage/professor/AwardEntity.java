package com.hid_web.be.storage.professor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "awards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AwardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String year;

    @Column(columnDefinition = "TEXT")
    private String title;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private ProfessorEntity professor;
}
