package com.hid_web.be.domain.exhibit;

import com.hid_web.be.storage.exhibit.ExhibitSubImgEntity;
import com.hid_web.be.storage.exhibit.ExhibitArtistEntity;
import com.hid_web.be.storage.exhibit.ExhibitDetailImgEntity;
import com.hid_web.be.storage.exhibit.ExhibitEntity;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExhibitExtractor {
    public Map<String, ExhibitSubImgEntity> extractSubImgMapByUrl(ExhibitEntity exhibitEntity) {
        Map<String, ExhibitSubImgEntity> entityMap = exhibitEntity.getSubImgEntities()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getSubImgUrl(), // 키: URL
                        e -> e // 값: 해당 엔티티 자체
                ));

        return entityMap;
    }

    public Map<String, ExhibitDetailImgEntity> extractDetailImgMapByUrl(ExhibitEntity exhibitEntity) {
        Map<String, ExhibitDetailImgEntity> entityMap = exhibitEntity.getDetailImgEntities()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getDetailImgUrl(), // 키: URL
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


