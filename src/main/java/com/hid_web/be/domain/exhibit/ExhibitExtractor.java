package com.hid_web.be.domain.exhibit;

import com.hid_web.be.storage.exhibit.ExhibitArtistEntity;
import com.hid_web.be.storage.exhibit.ExhibitDetailImgEntity;
import com.hid_web.be.storage.exhibit.ExhibitEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExhibitExtractor {
    public Map<String, ExhibitDetailImgEntity> extractDetailImgMapByObjectKey(ExhibitEntity exhibitEntity) {
        Map<String, ExhibitDetailImgEntity> entityMap = exhibitEntity.getDetailImgEntities()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getDetailImgObjectKey(), // 키: Object Key
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


