package com.hid_web.be.service;

import com.hid_web.be.controller.response.ExhibitResponse;
import com.hid_web.be.domain.exhibit.ExhibitEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExhibitService {

    private final S3Writer s3Writer;
    private final ExhibitWriter exhibitWriter;
    private final ExhibitReader exhibitReader;

    public List<ExhibitEntity> findAllExhibit() {
        return exhibitReader.findAllExhibit();
    }

    public ExhibitEntity findExhibitByExhibitId(Long exhibitId) {
        return exhibitReader.findExhibitById(exhibitId);
    }

    public ExhibitEntity createExhibit(MultipartFile mainThumbnailImageFile,
                                         List<MultipartFile> additionalThumbnailImageFiles,
                                         List<MultipartFile> detailImageFiles) throws IOException {

        String mainThumbnailImageUrl = s3Writer.writeFile(mainThumbnailImageFile, "main-thumbnail");
        List<String> additionalThumbnailImageUrls = s3Writer.writeFiles(additionalThumbnailImageFiles, "additional-thumbnails");
        List<String> detailImageUrls = s3Writer.writeFiles(detailImageFiles, "detail-images");

        List<String> artistNames = List.of("테스트 디자이너 A", "테스트 디자이너 B");

        ExhibitEntity exhibitEntity = exhibitWriter.createExhibit(
                mainThumbnailImageUrl,
                "테스트 제목", "테스트 부제",
                "테스트 설명", "테스트 이미지",
                "테스트 영상", additionalThumbnailImageUrls,
                detailImageUrls, artistNames
        );

        return exhibitEntity;
    }
}


