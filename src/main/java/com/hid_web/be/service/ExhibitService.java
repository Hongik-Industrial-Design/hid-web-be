package com.hid_web.be.service;

import com.hid_web.be.domain.exhibit.ExhibitEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExhibitService {
    private final S3Writer s3Writer;
    private final ExhibitWriter exhibitWriter;
    private final ExhibitReader exhibitReader;
    private final ExhibitExtractor exhibitExtractor;

    public List<ExhibitEntity> findAllExhibit() {
        return exhibitReader.findAllExhibit();
    }

    public ExhibitEntity findExhibitByExhibitId(Long exhibitId) {
        return exhibitReader.findExhibitById(exhibitId);
    }

    public ExhibitEntity createExhibit(MultipartFile mainThumbnailImageFile,
                                       List<MultipartFile> additionalThumbnailImageFiles,
                                       List<MultipartFile> detailImageFiles,
                                       List<MultipartFile> profileImageFiles, String titleKo, String titleEn, String subtitleKo, String subtitleEn, String textKo, String textEn, String videoUrl) throws IOException {

        String mainThumbnailImageUrl = s3Writer.writeFile(mainThumbnailImageFile, "main-thumbnail-image");
        List<String> additionalThumbnailImageUrls = s3Writer.writeFiles(additionalThumbnailImageFiles, "additional-thumbnails-images");
        List<String> detailImageUrls = s3Writer.writeFiles(detailImageFiles, "detail-images");
        List<String> profileImageUrls = s3Writer.writeFiles(profileImageFiles, "profile-images");

        Map<Integer, String> additionalThumbnailImageMap = exhibitExtractor.extractUrlMapWithPosition(additionalThumbnailImageUrls);
        Map<Integer, String> detailImageMap = exhibitExtractor.extractUrlMapWithPosition(detailImageUrls);

        List<String> artistNames = exhibitExtractor.extractArtistNamesFromUrls(profileImageUrls);

        ExhibitEntity exhibitEntity = exhibitWriter.createExhibit(
                mainThumbnailImageUrl,
                additionalThumbnailImageMap, detailImageMap,
                titleKo,
                titleEn,
                subtitleKo,
                subtitleEn,
                textKo,
                textEn,
                videoUrl,
                profileImageUrls, artistNames);

        return exhibitEntity;
    }
}


