package com.hid_web.be.controller.community.request;

import com.hid_web.be.domain.community.NoticeAuthorType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CreateNoticeRequest {
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;
    private String content;
    @NotBlank(message = "작성자는 필수 입력 값입니다.")
    private String author;
    private Boolean isImportant;
    private List<MultipartFile> images;
    private List<MultipartFile> attachments;

    public NoticeAuthorType getAuthorEnum() {
        try {
            return NoticeAuthorType.valueOf(this.author.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 작성자(author) 값입니다. [가능한 값: TA, COUNCIL]");
        }
    }

    public boolean getIsImportant() {
        return Boolean.TRUE.equals(this.isImportant);
    }
}
