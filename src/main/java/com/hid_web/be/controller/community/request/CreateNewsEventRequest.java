package com.hid_web.be.controller.community.request;

import com.hid_web.be.domain.community.NewsEventCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CreateNewsEventRequest {
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private NewsEventCategory category;

    private String content;

    private MultipartFile thumbnail;
    private List<MultipartFile> images;
    private List<MultipartFile> attachments;
}
