package com.hid_web.be.controller;

import com.hid_web.be.controller.response.ExhibitPreviewResponse;
import com.hid_web.be.controller.response.ExhibitResponse;
import com.hid_web.be.domain.exhibit.ExhibitEntity;
import com.hid_web.be.service.ExhibitService;
import com.hid_web.be.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exhibits")
public class ExhibitController {

    private final ExhibitService exhibitService;
    private final S3Service s3Service;

    @GetMapping("/previews")
    public ResponseEntity<List<ExhibitPreviewResponse>> findAllExhibitPreview() {
        List<ExhibitEntity> exhibitEntityList = exhibitService.findAllExhibit();
        List<ExhibitPreviewResponse> exhibitPreviewResponseList = exhibitEntityList.stream()
                .map(e -> ExhibitPreviewResponse.of(e))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(exhibitPreviewResponseList);
    }

    @GetMapping("/{exhibitId}")
    public ResponseEntity<ExhibitResponse> findExhibitById(@PathVariable Long exhibitId) {
        // ResponseEntity<ExhibitDetailResponse>
        ExhibitEntity exhibitEntity = exhibitService.findExhibitByExhibitId(exhibitId);
        return ResponseEntity.ok().body(ExhibitResponse.of(exhibitEntity));
    }

    @PostMapping
    public ResponseEntity<ExhibitResponse> createExhibitForTestOnlyThumbnail(@RequestParam("thumbnailImageFile") MultipartFile thumbnailImageFile) {
        try {
            String thumbnailUrl = s3Service.uploadThumbnail(thumbnailImageFile);

            ExhibitEntity exhibitEntity = exhibitService.createExhibit(thumbnailUrl);

            return ResponseEntity.ok().body(ExhibitResponse.of(exhibitEntity));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}