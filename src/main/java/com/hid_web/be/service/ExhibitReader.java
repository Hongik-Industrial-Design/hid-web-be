package com.hid_web.be.service;

import com.hid_web.be.domain.exhibit.ExhibitEntity;
import com.hid_web.be.repository.ExhibitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExhibitReader {
    private final ExhibitRepository exhibitRepository;

    public List<ExhibitEntity> findAllExhibit() {
        return exhibitRepository.findAll();
    }

    public ExhibitEntity findExhibitById(Long exhibitId) {
        return exhibitRepository.findExhibitByExhibitId(exhibitId);
    }
}
