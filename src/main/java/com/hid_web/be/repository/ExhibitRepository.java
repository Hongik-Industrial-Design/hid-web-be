package com.hid_web.be.repository;

import com.hid_web.be.domain.exhibit.ExhibitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExhibitRepository extends JpaRepository<ExhibitEntity, Long> {
    List<ExhibitEntity> findAll();
    ExhibitEntity findExhibitByExhibitId(Long exhibitId);
}