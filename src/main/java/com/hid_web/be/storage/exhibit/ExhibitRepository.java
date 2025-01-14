package com.hid_web.be.storage.exhibit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExhibitRepository extends JpaRepository<ExhibitEntity, Long> {
    List<ExhibitEntity> findAll();
    ExhibitEntity findExhibitByExhibitId(Long exhibitId);
}