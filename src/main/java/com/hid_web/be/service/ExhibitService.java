package com.hid_web.be.service;

import com.hid_web.be.domain.exhibit.ExhibitEntity;
import com.hid_web.be.repository.ExhibitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExhibitService {
    private final ExhibitRepository exhibitRepository;

    public void findAllExhibitPreview() {
    }

    public ExhibitEntity findExhibitByExhibitId(Long exhibitId) {
        return exhibitRepository.findExhibitByExhibitId(exhibitId);
    }
}