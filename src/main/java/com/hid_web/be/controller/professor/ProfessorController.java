package com.hid_web.be.controller.professor;

import com.hid_web.be.controller.professor.request.CreateProfessorRequest;
import com.hid_web.be.controller.professor.request.UpdateProfessorRequest;
import com.hid_web.be.controller.professor.response.ProfessorDetailResponse;
import com.hid_web.be.controller.professor.response.ProfessorPreviewResponse;
import com.hid_web.be.controller.professor.response.ProfessorResponse;
import com.hid_web.be.domain.professor.ProfessorService;
import com.hid_web.be.storage.professor.ProfessorEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/professors")
@RequiredArgsConstructor
public class ProfessorController {


    private static final Logger logger = LoggerFactory.getLogger(ProfessorController.class);

    private final ProfessorService professorService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)    //삽입
    public ResponseEntity<ProfessorResponse> createProfessor(
            @Valid @ModelAttribute CreateProfessorRequest createProfessorRequest
    ) {
        try {
            ProfessorEntity professorEntity = professorService.createProfessor(createProfessorRequest);
            ProfessorResponse response = ProfessorResponse.of(professorEntity);
            URI location = URI.create("/professors/" + professorEntity.getUuid());
            return ResponseEntity.created(location).body(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
    @PutMapping("/admin/{uuid}")    //수정
    public ResponseEntity<ProfessorResponse> updateProfessor(
            @Valid @PathVariable String uuid, @Valid @ModelAttribute UpdateProfessorRequest updateProfessorRequest
    ) {
        try {
            ProfessorEntity professorEntity = professorService.updateProfessor(uuid, updateProfessorRequest);

            ProfessorResponse response = ProfessorResponse.of(professorEntity);

            URI location = URI.create("/professors/" + uuid);


            return ResponseEntity.created(location).body(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @PostMapping("/test")
    public ResponseEntity<ProfessorResponse> a(
            @Valid @ModelAttribute CreateProfessorRequest createProfessorRequest
    ) {
        try {
            logger.debug("aaaaaaaaaaa"+"try");

            ProfessorEntity professorEntity = professorService.test(createProfessorRequest);
            ProfessorResponse response = ProfessorResponse.of(professorEntity);
            URI location = URI.create("/professors/" + professorEntity.getUuid());
            return ResponseEntity.created(location).body(response);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
    @GetMapping("/detail/{uuid}")   //교수 상세정보
    public ResponseEntity<ProfessorDetailResponse> getProfessorDetail(@PathVariable String uuid){
        try{
            ProfessorDetailResponse professorDetailResponse = professorService.getProfessorDetail(uuid);
            return ResponseEntity.ok(professorDetailResponse);
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @GetMapping("/{department}")    //학과별 교수진
    public ResponseEntity<List<ProfessorPreviewResponse>> getProfessors(@PathVariable String department){

        try {
            List<ProfessorPreviewResponse> professorPreviewList=professorService.getProfessorByDepartment(department);

            return ResponseEntity.ok(professorPreviewList);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @DeleteMapping("/admin/{uuid}") //삭제
    public ResponseEntity<Void> deleteProfessor(@PathVariable String uuid){

        try{
            professorService.deleteProfessor(uuid);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
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
