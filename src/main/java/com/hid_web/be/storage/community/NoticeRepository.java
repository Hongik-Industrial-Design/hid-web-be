package com.hid_web.be.storage.community;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

    List<NoticeEntity> findTop3ByIsImportantTrueOrderByCreatedDateDesc();

    List<NoticeEntity> findByIsImportantFalseOrderByCreatedDateDesc(Pageable pageable);

}
