package com.hid_web.be.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public int extractPositionFromFilename(String fileName) {
        String positionStr = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf('.'));
        int position = Integer.parseInt(positionStr);

        return position;
    }
}


