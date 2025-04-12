package com.hid_web.be.controller.exhibit;

import com.hid_web.be.controller.exhibit.request.CreateExhibitRequest;
import com.hid_web.be.controller.exhibit.request.UpdateExhibitRequest;
import com.hid_web.be.controller.exhibit.response.ExhibitPreviewResponse;
import com.hid_web.be.controller.exhibit.response.ExhibitResponse;
import com.hid_web.be.domain.exhibit.ExhibitService;
import com.hid_web.be.domain.exhibit.ExhibitType;
import com.hid_web.be.domain.exhibit.SearchType;
import com.hid_web.be.storage.exhibit.ExhibitEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    /*
    @GetMapping("/previews") // findAllExhibitPreview -> findExhibitPreviewsByYearAndMajor, findExhibitPreviewsByYearAndClub 임시
    public ResponseEntity<Result<List<ExhibitPreviewResponse>>> findAllExhibitPreview() {
        List<ExhibitEntity> exhibitEntityList = exhibitService.findAllExhibit();
        List<ExhibitPreviewResponse> exhibitPreviewResponseList = exhibitEntityList.stream()
                .map(e -> ExhibitPreviewResponse.of(e))
                .collect(Collectors.toList());

        // Result 객체로 리스트를 감싸서 반환
        return ResponseEntity.ok().body(new Result<>(exhibitPreviewResponseList));
    }
    */

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
    public ResponseEntity<ExhibitResponse> findExhibitByExhibitId(@PathVariable Long exhibitId) {

        ExhibitEntity exhibitEntity = exhibitService.findExhibitByExhibitId(exhibitId);

        return ResponseEntity.ok().body(ExhibitResponse.of(exhibitEntity));
    }

    /*
    @Valid 어노테이션이 있으면 Spring이 자동으로 Validation을 수행하고, 실패 시 MethodArgumentNotValidException을 발생시키며, 이를 @ExceptionHandler가 처리한다.
     */
    @PostMapping(value = "/admin", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ExhibitResponse> createExhibit(@Valid @ModelAttribute CreateExhibitRequest createExhibitRequest) {
        try {
            ExhibitEntity exhibitEntity = exhibitService.createExhibit(
                    createExhibitRequest.getMainImgFile(),
                    createExhibitRequest.toDetailImgs(),
                    createExhibitRequest.toDetails(),
                    createExhibitRequest.toArtists()
            );

            return ResponseEntity.ok(ExhibitResponse.of(exhibitEntity));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/admin/{exhibitId}")
    public ResponseEntity<Void> deleteExhibit(@PathVariable Long exhibitId) {
        exhibitService.deleteExhibit(exhibitId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/{exhibitId}")
    public ResponseEntity<ExhibitResponse> updateExhibit(@Valid @PathVariable Long exhibitId, @ModelAttribute UpdateExhibitRequest updateExhibitRequest) {
        try {
            ExhibitEntity updatedExhibit = exhibitService.updateExhibit(
                    exhibitId,
                    updateExhibitRequest.getMainImgFile(),
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
    public static class Result<T> {
        private T data;
    }
}