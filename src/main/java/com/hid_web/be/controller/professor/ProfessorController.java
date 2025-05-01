package com.hid_web.be.controller.professor;

import com.hid_web.be.controller.professor.request.CreateProfessorRequest;
import com.hid_web.be.controller.professor.response.ProfessorResponse;
import com.hid_web.be.domain.professor.ProfessorService;
import com.hid_web.be.storage.professor.ProfessorEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    public ResponseEntity<ProfessorResponse> createProfessor(
            @Valid @ModelAttribute CreateProfessorRequest createProfessorRequest
    ) {
        try {
            ProfessorEntity professorEntity = professorService.createProfessor(createProfessorRequest);
            ProfessorResponse response = ProfessorResponse.of(professorEntity);
            URI location = URI.create("/professors/" + professorEntity.getUuid());
            return ResponseEntity.created(location).body(response);
        } catch (Exception e) {
            1+1;//test
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    public static class Result<T> {
        private T data;
    }
}
