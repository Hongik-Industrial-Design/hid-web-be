package com.hid_web.be.domain.professor;

import com.hid_web.be.controller.professor.request.CreateProfessorRequest;
import com.hid_web.be.domain.s3.S3Writer;
import com.hid_web.be.storage.professor.ProfessorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorWriter professorWriter;
    private final S3Writer s3Writer;

    @Transactional
    public ProfessorEntity createProfessor(CreateProfessorRequest createProfessorRequest) throws IOException {
        // 교수 UUID 생성
        UUID professorUUID = UUID.randomUUID();

        // S3에 프로필 이미지 업로드
        s3Writer.writeFile(createProfessorRequest.getImage(), professorUUID + "/profile-image");

        // 교수 기본 정보 저장
        return professorWriter.createProfessor(professorUUID, createProfessorRequest);
    }
}
