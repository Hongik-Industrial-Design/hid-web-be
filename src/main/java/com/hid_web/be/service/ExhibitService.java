package com.hid_web.be.service;

import com.hid_web.be.domain.exhibit.ExhibitEntity;
import com.hid_web.be.repository.ExhibitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExhibitService {
    private final ExhibitRepository exhibitRepository;

    public List<ExhibitEntity> findAllExhibit() {
        return exhibitRepository.findAll();
    }

    public ExhibitEntity findExhibitByExhibitId(Long exhibitId) {
        return exhibitRepository.findExhibitByExhibitId(exhibitId);
    }
}