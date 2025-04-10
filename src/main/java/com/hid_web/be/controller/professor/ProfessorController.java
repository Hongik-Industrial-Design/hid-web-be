package com.hid_web.be.controller.professor;

import com.hid_web.be.controller.professor.request.CreateProfessorRequest;
import com.hid_web.be.domain.professor.ProfessorService;
import com.hid_web.be.storage.professor.ProfessorEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/professors")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProfessor(
            @Valid @ModelAttribute CreateProfessorRequest createProfessorRequest
    ) {
        try {
            ProfessorEntity professorEntity = professorService.createProfessor(createProfessorRequest);
            URI location = URI.create("/professors/" + professorEntity.getUuid());
            return ResponseEntity.created(location).body(professorEntity);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("알 수 없는 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
