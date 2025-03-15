package com.hid_web.be.storage.content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<ContentMainVideoEntity, Long> {
     ContentMainVideoEntity findByYear(Long year);
}
