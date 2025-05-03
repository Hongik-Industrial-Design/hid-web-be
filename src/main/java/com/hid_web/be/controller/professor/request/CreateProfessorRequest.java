package com.hid_web.be.controller.professor.request;

import com.hid_web.be.domain.professor.Award;
import com.hid_web.be.domain.professor.Biography;
import com.hid_web.be.domain.professor.Work;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CreateProfessorRequest {

    @NotNull(message = "교수 이미지는 필수입니다.")
    private MultipartFile image;

    @NotEmpty(message = "이름은 필수입니다.")
    private String name;

    private String position;
    private String department;
    private String detailInfo;
    private String email;
    private String major;

    @Valid
    private List<CreateBiographyRequest> biographyEntries;

    @Valid
    private List<CreateAwardRequest> awards;

    @Valid
    private List<CreateWorkRequest> works;

    // 엔티티 변환 메서드들
    public List<Biography> toBiographyList() {
        return biographyEntries.stream()
                .map(dto -> new Biography(null, dto.getYear(), dto.getDescription()))
                .collect(Collectors.toList());
    }

    public List<Award> toAwardList() {
        return awards.stream()
                .map(dto -> new Award(null, dto.getYear(), dto.getTitle()))
                .collect(Collectors.toList());
    }

    public List<Work> toWorkList() {
        return works.stream()
                .map(dto -> new Work(null, dto.getYear(), dto.getTitle()))
                .collect(Collectors.toList());
    }
}
