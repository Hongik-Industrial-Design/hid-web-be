package com.hid_web.be.domain.exhibit;

import com.hid_web.be.storage.exhibit.ExhibitEntity;
import com.hid_web.be.storage.exhibit.ExhibitRepository;
import com.hid_web.be.support.error.ErrorCode;
import com.hid_web.be.support.error.ExhibitException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExhibitReader {
    private final ExhibitRepository exhibitRepository;

    public List<ExhibitEntity> findAll() {
        return exhibitRepository.findAll();
    }

    public List<ExhibitEntity> findByTypeAndYear(ExhibitType type, String year) {
        return exhibitRepository.findByTypeAndYear(type, year);
    }

    public List<ExhibitEntity> findByTypeAndYearAndClub(ExhibitType exhibitType, String year, String club) {
        return exhibitRepository.findByTypeAndYearAndClub(exhibitType, year, club);
    }

    public List<ExhibitEntity> findByTypeAndYearAndMajor(ExhibitType type, String year, String major) {
        return exhibitRepository.findByTypeAndYearAndMajor(type, year, major);
    }

    public ExhibitEntity findByExhibitId(Long exhibitId) {
        ExhibitEntity exhibitEntity = exhibitRepository.findByExhibitId(exhibitId);

        if (exhibitEntity == null) {
            throw new ExhibitException(ErrorCode.EXHIBIT_NOT_FOUND, "해당하는 전시 ID가 존재하지 않습니다.");
        }

        return exhibitEntity;
    }

    public List<ExhibitEntity> searchByArtistName(String searchTerm, ExhibitType type, String year) {
        return exhibitRepository.searchByArtistName(searchTerm, type, year);
    }

    public List<ExhibitEntity> searchByTitle(String searchTerm, ExhibitType type, String year) {
        return exhibitRepository.searchByTitle(searchTerm, type, year);
    }
}