package com.hid_web.be.service;

import com.hid_web.be.domain.exhibit.ExhibitAdditionalThumbnailEntity;
import com.hid_web.be.domain.exhibit.ExhibitArtistEntity;
import com.hid_web.be.domain.exhibit.ExhibitDetailImageEntity;
import com.hid_web.be.domain.exhibit.ExhibitEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExhibitExtractor {

    public List<String> extractArtistNamesFromUrls(List<String> profileImageUrls) {
        List<String> artistNames = new ArrayList<>();

        for (String url : profileImageUrls) {
            String fileName = url.substring(url.lastIndexOf('/') + 1);
            String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
            String artistName = fileNameWithoutExtension.replace("_", " ");
            artistNames.add(artistName);
        }

        return artistNames;
    }

    public Map<Integer, String> extractUrlMapWithPosition(List<String> imageUrls) {
        Map<Integer, String> urlMap = new HashMap<>();

        for (String url : imageUrls) {
            int position = extractPositionFromFilename(url);
            urlMap.put(position, url);
        }

        return urlMap;
    }

    public Map<Integer, String> extractUrlMapWithPositionV2(List<String> imageUrls, List<Integer> positions) {
        Map<Integer, String> urlMap = new HashMap<>();

        for (int i = 0; i < imageUrls.size(); i++) {
            int position = positions.get(i);
            String url = imageUrls.get(i);
            urlMap.put(position, url);
        }

        return urlMap;
    }


    public int extractPositionFromFilename(String fileName) {
        String positionStr = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf('.'));
        int position = Integer.parseInt(positionStr);

        return position;
    }

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


