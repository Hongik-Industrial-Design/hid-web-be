package com.hid_web.be.controller;

import com.hid_web.be.controller.response.ExhibitPreviewResponse;
import com.hid_web.be.controller.response.ExhibitResponse;
import com.hid_web.be.domain.exhibit.ExhibitEntity;
import com.hid_web.be.service.ExhibitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/exhibits")
@RequiredArgsConstructor
@RestController
public class ExhibitController {

    private final ExhibitService exhibitService;

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
}