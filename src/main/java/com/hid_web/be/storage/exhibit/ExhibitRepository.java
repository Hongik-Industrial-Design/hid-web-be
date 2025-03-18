package com.hid_web.be.storage.exhibit;

import com.hid_web.be.domain.exhibit.ExhibitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExhibitRepository extends JpaRepository<ExhibitEntity, Long> {
    List<ExhibitEntity> findAll();
    ExhibitEntity findExhibitByExhibitId(Long exhibitId);

    @Query("SELECT DISTINCT e FROM ExhibitEntity e " +
            "JOIN e.artistEntities a " +
            "WHERE (LOWER(a.nameKo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(a.nameEn) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND e.type = :type " +
            "AND e.year = :year")
    List<ExhibitEntity> searchByArtistName(
            @Param("searchTerm") String searchTerm,
            @Param("type") ExhibitType type,
            @Param("year") String year);

    @Query("SELECT e FROM ExhibitEntity e " +
            "WHERE (LOWER(e.titleKo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(e.titleEn) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND e.type = :type " +
            "AND e.year = :year")
    List<ExhibitEntity> searchByTitle(
            @Param("searchTerm") String searchTerm,
            @Param("type") ExhibitType type,
            @Param("year") String year);
}