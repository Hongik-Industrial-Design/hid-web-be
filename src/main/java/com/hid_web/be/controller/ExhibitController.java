package com.hid_web.be.controller;

import com.hid_web.be.controller.response.ExhibitResponse;
import com.hid_web.be.domain.exhibit.ExhibitEntity;
import com.hid_web.be.service.ExhibitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/exhibit")
@RequiredArgsConstructor
@RestController
public class ExhibitController {

    private final ExhibitService exhibitService;

    public void findAllExhibitPreview() {
    }

    @GetMapping("/{exhibitId}")
    public ResponseEntity<ExhibitResponse> findExhibitById(@PathVariable Long exhibitId) {
        // ResponseEntity<ExhibitDetailResponse>
        ExhibitEntity exhibitEntity = exhibitService.findExhibitByExhibitId(exhibitId);
        return ResponseEntity.ok().body(ExhibitResponse.of(exhibitEntity));
    }
}