package com.hid_web.be.storage.community;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    @Query("SELECT n FROM NoticeEntity n ORDER BY n.isImportant DESC, n.createdDate DESC")
    Page<NoticeEntity> findAllByOrderByIsImportantDescCreatedDateDesc(Pageable pageable);

}
