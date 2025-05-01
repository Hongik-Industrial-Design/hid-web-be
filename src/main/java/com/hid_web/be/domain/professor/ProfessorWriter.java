package com.hid_web.be.domain.professor;

import com.hid_web.be.controller.professor.request.CreateProfessorRequest;
import com.hid_web.be.controller.professor.request.UpdateProfessorRequest;
import com.hid_web.be.storage.professor.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProfessorWriter {
    private final ProfessorRepository professorRepository;

    public ProfessorEntity createProfessor(String professorUUID, String imgObjectKey, CreateProfessorRequest request, int sortOrder) {
        ProfessorEntity professorEntity = new ProfessorEntity();
        professorEntity.setUuid(professorUUID);
        professorEntity.setName(request.getName());
        professorEntity.setPosition(request.getPosition());
        professorEntity.setDepartment(request.getDepartment());
        professorEntity.setDetailInfo(request.getDetailInfo());
        professorEntity.setEmail(request.getEmail());
        professorEntity.setMajor(request.getMajor());
        professorEntity.setImgObjectKey(imgObjectKey);
        professorEntity.setSortOrder(sortOrder);
        // Biography 변환 및 저장
        List<BiographyEntryEntity> biographyEntities = request.toBiographyList().stream().map(bio -> {
            BiographyEntryEntity entity = new BiographyEntryEntity();
            entity.setProfessor(professorEntity);
            entity.setYear(bio.getYear());
            entity.setDescription(bio.getDescription());
            return entity;
        }).collect(Collectors.toList());
        professorEntity.setBiographyEntries(biographyEntities);

        // Award 변환 및 저장
        List<AwardEntity> awardEntities = request.toAwardList().stream().map(award -> {
            AwardEntity entity = new AwardEntity();
            entity.setProfessor(professorEntity);
            entity.setYear(award.getYear());
            entity.setTitle(award.getTitle());
            return entity;
        }).collect(Collectors.toList());
        professorEntity.setAwards(awardEntities);

        // Work 변환 및 저장
        List<WorkEntity> workEntities = request.toWorkList().stream().map(work -> {
            WorkEntity entity = new WorkEntity();
            entity.setProfessor(professorEntity);
            entity.setYear(work.getYear());
            entity.setTitle(work.getTitle());
            return entity;
        }).collect(Collectors.toList());
        professorEntity.setWorks(workEntities);

        return professorRepository.save(professorEntity);
    }

    public ProfessorEntity updateProfessor(String professorUUID, String imgObjectKey, UpdateProfessorRequest request, int sortOrder) {
        ProfessorEntity professorEntity = new ProfessorEntity();
        professorEntity.setUuid(professorUUID);
        professorEntity.setName(request.getName());
        professorEntity.setPosition(request.getPosition());
        professorEntity.setDepartment(request.getDepartment());
        professorEntity.setDetailInfo(request.getDetailInfo());
        professorEntity.setEmail(request.getEmail());
        professorEntity.setMajor(request.getMajor());
        professorEntity.setImgObjectKey(imgObjectKey);
        professorEntity.setSortOrder(sortOrder);
        // Biography 변환 및 저장
        List<BiographyEntryEntity> biographyEntities = request.toBiographyList().stream().map(bio -> {
            BiographyEntryEntity entity = new BiographyEntryEntity();
            entity.setProfessor(professorEntity);
            entity.setYear(bio.getYear());
            entity.setDescription(bio.getDescription());
            return entity;
        }).collect(Collectors.toList());
        professorEntity.setBiographyEntries(biographyEntities);

        // Award 변환 및 저장
        List<AwardEntity> awardEntities = request.toAwardList().stream().map(award -> {
            AwardEntity entity = new AwardEntity();
            entity.setProfessor(professorEntity);
            entity.setYear(award.getYear());
            entity.setTitle(award.getTitle());
            return entity;
        }).collect(Collectors.toList());
        professorEntity.setAwards(awardEntities);

        // Work 변환 및 저장
        List<WorkEntity> workEntities = request.toWorkList().stream().map(work -> {
            WorkEntity entity = new WorkEntity();
            entity.setProfessor(professorEntity);
            entity.setYear(work.getYear());
            entity.setTitle(work.getTitle());
            return entity;
        }).collect(Collectors.toList());
        professorEntity.setWorks(workEntities);

        return professorRepository.save(professorEntity);
    }
}
