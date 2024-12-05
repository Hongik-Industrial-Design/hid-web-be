package com.hid_web.be.service;

import com.hid_web.be.domain.exhibit.ExhibitAdditionalThumbnailEntity;
import com.hid_web.be.domain.exhibit.ExhibitArtistEntity;
import com.hid_web.be.domain.exhibit.ExhibitDetailImageEntity;
import com.hid_web.be.domain.exhibit.ExhibitEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExhibitExtractor {
    public Map<String, ExhibitAdditionalThumbnailEntity> extractAdditionalImageMapByUrl(ExhibitEntity exhibitEntity) {
        Map<String, ExhibitAdditionalThumbnailEntity> entityMap = exhibitEntity.getExhibitAdditionalThumbnailImageEntityList()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getAdditionalThumbnailImageUrl(), // 키: URL
                        e -> e // 값: 해당 엔티티 자체
                ));

        return entityMap;
    }

    public Map<String, ExhibitDetailImageEntity> extractDetailImageMapByUrl(ExhibitEntity exhibitEntity) {
        Map<String, ExhibitDetailImageEntity> entityMap = exhibitEntity.getExhibitDetailImageEntityList()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getDetailImageUrl(), // 키: URL
                        e -> e // 값: 해당 엔티티 자체
                ));

        return entityMap;
    }

    public Map<String, ExhibitArtistEntity> extractArtistMapByUUID(List<ExhibitArtistEntity> artistEntityList) {
        return artistEntityList.stream()
                .collect(Collectors.toMap(
                        e -> e.getArtistUUID(), // 키: UUID
                        artist -> artist // 값: 해당 엔티티 자체
                ));
    }
}


