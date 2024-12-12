package com.hid_web.be.domain.exhibit;

import com.hid_web.be.storage.ExhibitEntity;
import com.hid_web.be.storage.ExhibitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
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