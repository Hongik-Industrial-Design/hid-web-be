package com.hid_web.be.controller;

import com.hid_web.be.controller.request.CreateExhibitRequest;
import com.hid_web.be.controller.response.ExhibitPreviewResponse;
import com.hid_web.be.controller.response.ExhibitResponse;
import com.hid_web.be.domain.exhibit.ExhibitEntity;
import com.hid_web.be.service.ExhibitService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exhibits")
public class ExhibitController {
    private final ExhibitService exhibitService;

    @GetMapping("/previews")
    public ResponseEntity<Result<List<ExhibitPreviewResponse>>> findAllExhibitPreview() {
        List<ExhibitEntity> exhibitEntityList = exhibitService.findAllExhibit();
        List<ExhibitPreviewResponse> exhibitPreviewResponseList = exhibitEntityList.stream()
                .map(e -> ExhibitPreviewResponse.of(e))
                .collect(Collectors.toList());

        // Result 객체로 리스트를 감싸서 반환
        return ResponseEntity.ok().body(new Result<>(exhibitPreviewResponseList));
    }

    @GetMapping("/{exhibitId}")
    public ResponseEntity<ExhibitResponse> findExhibitById(@PathVariable Long exhibitId) {

        ExhibitEntity exhibitEntity = exhibitService.findExhibitByExhibitId(exhibitId);

        return ResponseEntity.ok().body(ExhibitResponse.of(exhibitEntity));
    }


    @PostMapping
    public ResponseEntity<ExhibitResponse> createExhibit(@ModelAttribute CreateExhibitRequest createExhibitRequest) {
        try {
            ExhibitEntity exhibitEntity = exhibitService.createExhibit(
                    createExhibitRequest.getMainThumbnailImageFile(),
                    createExhibitRequest.getAdditionalThumbnailImageFiles(),
                    createExhibitRequest.getDetailImageFiles(),
                    createExhibitRequest.toExhibitDetail(),
                    createExhibitRequest.toExhibitArtistList()
            );

            return ResponseEntity.ok(ExhibitResponse.of(exhibitEntity));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{exhibitId}")
    public ResponseEntity<Void> deleteExhibit(@PathVariable Long exhibitId) {
        exhibitService.deleteExhibit(exhibitId);
        return ResponseEntity.noContent().build();
    }

    @Data
    @AllArgsConstructor
    public class Result<T> {
        private T data;
    }
}
