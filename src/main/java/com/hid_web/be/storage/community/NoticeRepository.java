package com.hid_web.be.storage.community;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

    // Notice 조회
    List<NoticeEntity> findTop3ByIsImportantTrueOrderByCreatedDateDescIdDesc();

    // 일반 공지 조회
    Page<NoticeEntity> findByIsImportantFalseOrderByCreatedDateDescIdDesc(Pageable pageable);

    // 오래된 중요 공지 조회
    List<NoticeEntity> findByIsImportantTrueOrderByCreatedDateAscIdAsc();

    long countByIsImportantFalse();


    // Community 조회
    List<NoticeEntity> findTop1ByIsImportantTrueOrderByCreatedDateDesc();

    List<NoticeEntity> findTop4ByIsImportantFalseOrderByCreatedDateDescIdDesc();

}
