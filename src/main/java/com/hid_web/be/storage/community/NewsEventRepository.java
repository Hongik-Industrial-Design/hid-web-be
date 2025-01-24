package com.hid_web.be.storage.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsEventRepository extends JpaRepository<NewsEventEntity, Long> {

}
