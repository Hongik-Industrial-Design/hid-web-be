package com.hid_web.be.storage.professor;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfessorRepository extends JpaRepository<ProfessorEntity, UUID> {

    @Query("SELECT p "+
            "FROM ProfessorEntity p "+
            "WHERE department = :department "+
            "ORDER BY sortOrder ASC")
    List<ProfessorEntity> getByDepartment(@Param("department") String department);

    @Query("SELECT p "+
            "FROM ProfessorEntity p "+
            "ORDER BY sortOrder ASC")
    List<ProfessorEntity> getAll();


    ProfessorEntity findByUuid(String uuid);

    long count();

    @Query("SELECT p.sortOrder + 1 "+
            "FROM ProfessorEntity p "+
            "WHERE p.position = :position AND p.name <= :name "+
            "ORDER BY p.sortOrder DESC "+
            "FETCH FIRST 1 ROW ONLY")
    Optional<Integer> getSortOrderByPositionAndName(@Param("position") String position, //값이 삽입될 떄 들어갈 sortorder
                                              @Param("name") String name);
    @Query("SELECT MAX(p.sortOrder) " +
            "FROM ProfessorEntity p "+
            "WHERE position = :position")
    Optional<Integer> getMaxSortOrderByPositon(@Param("position") String position);

    @Modifying
    @Transactional
    @Query("UPDATE ProfessorEntity p "+
            "SET p.sortOrder=p.sortOrder + 1 "+
            "WHERE p.sortOrder >= :threshold")
    void updateSortOrder(@Param("threshold") int threshold);
    //List<ProfessorEntity>


}
