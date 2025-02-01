package com.hid_web.be.storage.community;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsEventRepository extends JpaRepository<NewsEventEntity, Long> {

    // NewsEvent 조회
    List<NewsEventEntity> findTop12ByOrderByCreatedDateDescIdDesc(Pageable pageable);

    // Community 조회
    List<NewsEventEntity> findTop8ByOrderByCreatedDateDescIdDesc();

}
