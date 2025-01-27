package com.hid_web.be.storage.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsEventRepository extends JpaRepository<NewsEventEntity, Long> {
    List<NewsEventEntity> findTop8ByOrderByCreatedDateDesc();

}
