package com.hid_web.be.controller.exhibit;

import com.hid_web.be.controller.exhibit.request.CreateExhibitRequest;
import com.hid_web.be.controller.exhibit.request.UpdateExhibitRequest;
import com.hid_web.be.controller.exhibit.response.ExhibitPreviewResponse;
import com.hid_web.be.controller.exhibit.response.ExhibitResponse;
import com.hid_web.be.domain.exhibit.ExhibitType;
import com.hid_web.be.domain.exhibit.SearchType;
import com.hid_web.be.storage.exhibit.ExhibitEntity;
import com.hid_web.be.domain.exhibit.ExhibitService;
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
    public ResponseEntity<Result<List<ExhibitPreviewResponse>>> findExhibitPreviewsByTypeYearAndTerm(
            @RequestParam ExhibitType exhibitType,
            @RequestParam(defaultValue = "2024") String year,
            @RequestParam(defaultValue = "ALL") String term) {
        List<ExhibitEntity> exhibitEntities = exhibitService.findExhibitsByTypeYearAndTerm(exhibitType, year, term);

        List<ExhibitPreviewResponse> exhibitPreviewResponses = exhibitEntities.stream()
                .map(ExhibitPreviewResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new Result<>(exhibitPreviewResponses));
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
                    createExhibitRequest.getMainImgFile(),
                    createExhibitRequest.toSubImgs(),
                    createExhibitRequest.toDetailImgs(),
                    createExhibitRequest.toDetails(),
                    createExhibitRequest.toArtists()
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

    @PutMapping("/{exhibitId}")
    public ResponseEntity<ExhibitResponse> updateExhibit(@PathVariable Long exhibitId, @ModelAttribute UpdateExhibitRequest updateExhibitRequest) {
        try {
            ExhibitEntity updatedExhibit = exhibitService.updateExhibit(
                    exhibitId,
                    updateExhibitRequest.getMainImgFile(),
                    updateExhibitRequest.toSubImgs(),
                    updateExhibitRequest.toDetailImgs(),
                    updateExhibitRequest.toDetails(),
                    updateExhibitRequest.toArtists());


            return ResponseEntity.ok(ExhibitResponse.of(updatedExhibit));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ExhibitPreviewResponse>> searchExhibits(
            @RequestParam ExhibitType exhibitType,
            @RequestParam String year,
            @RequestParam SearchType searchType,
            @RequestParam String searchTerm
    ) {
        List<ExhibitEntity> results = exhibitService.searchExhibits(
                searchTerm, exhibitType, year, searchType
        );

        List<ExhibitPreviewResponse> previews = results.stream()
                .map(ExhibitPreviewResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(previews);
    }

    @Data
    @AllArgsConstructor
    public class Result<T> {
        private T data;
    }
}
