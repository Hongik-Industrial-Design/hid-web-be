package com.hid_web.be.service;

import com.hid_web.be.domain.exhibit.ExhibitArtist;
import com.hid_web.be.domain.exhibit.ExhibitDetail;
import com.hid_web.be.domain.exhibit.ExhibitEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
                                       ExhibitDetail exhibitDetail,

                                       List<ExhibitArtist> exhibitArtistList) throws IOException {

        String exhibitUUID = UUID.randomUUID().toString();

        String mainThumbnailImageUrl = s3Writer.writeFile(mainThumbnailImageFile, exhibitUUID + "/main-thumbnail-image");

        List<String> additionalThumbnailImageUrls = s3Writer.writeFiles(additionalThumbnailImageFiles, exhibitUUID + "/additional-thumbnails-images");
        List<String> detailImageUrls = s3Writer.writeFiles(detailImageFiles, exhibitUUID + "/detail-images");

        Map<Integer, String> additionalThumbnailImageMap = exhibitExtractor.extractUrlMapWithPosition(additionalThumbnailImageUrls);
        Map<Integer, String> detailImageMap = exhibitExtractor.extractUrlMapWithPosition(detailImageUrls);

        for (ExhibitArtist artist : exhibitArtistList) {
                String profileImageUrl = s3Writer.writeFile(artist.getProfileImageFile(), exhibitUUID + "/profile-images");
                artist.setProfileImageFileUrl(profileImageUrl);
        }

        ExhibitEntity exhibitEntity = exhibitWriter.createExhibit(
                exhibitUUID,
                mainThumbnailImageUrl,
                additionalThumbnailImageMap,
                detailImageMap,
                exhibitDetail,
                exhibitArtistList
        );

        return exhibitEntity;
    }

    public void deleteExhibit(Long exhibitId) {
        ExhibitEntity exhibitEntity = exhibitReader.findExhibitById(exhibitId);

        s3Writer.deleteFolder(exhibitEntity.getExhibitUUID());
        exhibitWriter.deleteExhibit(exhibitId);
    }
}

