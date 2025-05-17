package com.hid_web.be.domain.professor;

import com.hid_web.be.controller.professor.ProfessorController;
import com.hid_web.be.controller.professor.request.CreateProfessorRequest;
import com.hid_web.be.controller.professor.request.UpdateProfessorRequest;
import com.hid_web.be.controller.professor.response.ProfessorDetailResponse;
import com.hid_web.be.controller.professor.response.ProfessorPreviewResponse;
import com.hid_web.be.controller.professor.response.ProfessorResponse;
import com.hid_web.be.domain.s3.S3UrlConverter;
import com.hid_web.be.domain.s3.S3Writer;
import com.hid_web.be.storage.professor.ProfessorEntity;
import com.hid_web.be.storage.professor.ProfessorRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private static final Logger logger = LoggerFactory.getLogger(ProfessorController.class);

    private final ProfessorWriter professorWriter;
    private final S3Writer s3Writer;
    private final ProfessorRepository professorRepository;

    private static final List<String> SORT_POSITION=
            List.of("교수", "부교수", "조교수", "초빙교수", "겸임교수",
                    "외래교수", "강사");  //정렬 순서

    @Transactional
    public ProfessorEntity createProfessor(CreateProfessorRequest createProfessorRequest) throws IOException {
        // 교수 UUID 생성
        String professorUUID = UUID.randomUUID().toString();

        // S3에 프로필 이미지 업로드
        String imgObjectKey = s3Writer.writeFile(createProfessorRequest.getImage(), professorUUID + "/profile-image");

        int sortOrder=calculateSortOrder(createProfessorRequest.getName(), createProfessorRequest.getPosition());

        updateSortOrder(sortOrder);

        // 교수 기본 정보 저장
        return professorWriter.createProfessor(professorUUID, imgObjectKey, createProfessorRequest, sortOrder);
    }

    @Transactional
    public ProfessorEntity updateProfessor(String uuid, UpdateProfessorRequest updateProfessorRequest) throws IOException {

        // 교수 UUID 생성

        // S3에 프로필 이미지 업로드
        String imgObjectKey = s3Writer.writeFile(updateProfessorRequest.getImage(), uuid + "/profile-image");

        int sortOrder=calculateSortOrder(updateProfessorRequest.getName(), updateProfessorRequest.getPosition());

        updateSortOrder(sortOrder);

        // 교수 기본 정보 저장
        return professorWriter.updateProfessor(uuid, imgObjectKey, updateProfessorRequest, sortOrder);
    }

    @Transactional
    public ProfessorEntity test(CreateProfessorRequest createProfessorRequest) throws IOException {
        // 교수 UUID 생성
        String professorUUID = UUID.randomUUID().toString();

        // S3에 프로필 이미지 업로드
        String imgObjectKey = "TestUrl";

        int sortOrder=calculateSortOrder(createProfessorRequest.getName(), createProfessorRequest.getPosition());

        updateSortOrder(sortOrder);

        // 교수 기본 정보 저장
        return professorWriter.createProfessor(professorUUID, imgObjectKey, createProfessorRequest, sortOrder);
    }

    public List<ProfessorPreviewResponse> getProfessorByDepartment(String department){

        List <ProfessorEntity> professors;
        if("all".equals(department)){
            professors=professorRepository.getAll();
        }else{

            professors= professorRepository.getByDepartment(department);
        }
        return professors.stream()
                .map(professor -> ProfessorPreviewResponse.builder()
                        .uuid(professor.getUuid())
                        .name(professor.getName())
                        .department(professor.getDepartment())
                        .imgUrl(S3UrlConverter.convertCloudfrontUrlFromObjectKey(professor.getImgObjectKey()))
                        .build())
                .collect(Collectors.toList());
    }

    public ProfessorDetailResponse getProfessorDetail(String uuid){

        ProfessorDetailResponse professorDetailResponse= ProfessorDetailResponse.of(professorRepository.findByUuid(uuid));
        return professorDetailResponse;
    }

    @Transactional
    public void deleteProfessor(String uuid){

        ProfessorEntity professorEntity=professorRepository.findByUuid(uuid);
        if(professorEntity!=null){
            logger.debug("data found");
            professorRepository.delete(professorEntity);

            try{
                s3Writer.deleteObjects(professorEntity.getImgObjectKey());    //수정 필요
            }catch(RuntimeException e){

                throw e;
            }

        }else{  //삭제할 데이터가 없음

            logger.debug("no data found");

            throw new IllegalArgumentException("no data found");
        }

    }

    private int calculateSortOrder(String name, String position){

        Optional<Integer> result= professorRepository.getSortOrderByPositionAndName(position, name);

        if(result.isPresent()){    //예시 데이터 존재
            return result.get();
        }else {//예시 데이터 존재안함

            int i=SORT_POSITION.indexOf(position);
            if(i!=-1) { // SORT_POSITION 에 등록되어 있다면
                Optional<Integer> prevResult;
                for (i--; i >= 0; i--) {
                    prevResult = professorRepository.getMaxSortOrderByPositon(SORT_POSITION.get(i));
                    if (prevResult.isPresent()) {
                        return prevResult.get() + 1;    //다음 인덱스부터 시작
                    }
                }
                return 0;   //sortorder는 0부터 시작
            }else{  //등록되어 있지 않다면 마지막에 넣음
                int num= (int)professorRepository.count();//마지막 값 가져오기
                return num;//맨 뒤에 놓기
            }
        }
    }
    private void updateSortOrder(int sortOrder){    //sortorder보다 같거나 큰 값을 1올림
        professorRepository.updateSortOrder(sortOrder);
    }

}
