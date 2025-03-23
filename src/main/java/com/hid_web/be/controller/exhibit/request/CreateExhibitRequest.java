package com.hid_web.be.controller.exhibit.request;

import com.hid_web.be.domain.exhibit.ExhibitArtist;
import com.hid_web.be.domain.exhibit.ExhibitDetail;
import com.hid_web.be.domain.exhibit.ExhibitDetailImg;
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
public class CreateExhibitRequest {
    /**
     * ConstraintValidator를 이용한 MultipartFile 검증 기능 고도화 필요
     */
    @NotNull(message = "메인 이미지가 필요합니다")
    private MultipartFile mainImgFile;

    @NotEmpty
    @Valid
    private List<CreateExhibitDetailImgRequest> detailImgs;

    @NotNull(message = "전시 상세 정보가 필요합니다") //  단일 객체이므로 Null이 아님을 검증한다.
    @Valid
    private CreateExhibitDetailRequest details;

    @NotEmpty(message = "참여 학생 정보가 필요합니다") // List이므로 Null이 아니면서 동시에 최소 1개 이상의 요소가 있어야 함을 검증한다.
    @Valid
    private List<CreateExhibitArtistRequest> artists;

    public List<ExhibitDetailImg> toDetailImgs() {
        /*
        if (detailImgs == null) {
            return null;
        }
        */

        return detailImgs.stream()
                .map(request -> new ExhibitDetailImg(
                        request.getFile(),
                        request.getPosition()
                ))
                .collect(Collectors.toList());
    }

    public ExhibitDetail toDetails() {
        return new ExhibitDetail(
                details.getType(),
                details.getYear(),
                details.getMajor(),
                details.getClub(),
                details.getBehanceUrl(),
                details.getInstagramUrl(),
                details.getTitleKo(),
                details.getTitleEn(),
                details.getSubTitleKo(),
                details.getSubTitleEn(),
                details.getDescriptionKo(),
                details.getDescriptionEn(),
                details.getVideoUrl()
        );
    }

    public List<ExhibitArtist> toArtists() {
        return artists.stream()
                .map(createExhibitArtistRequest -> new ExhibitArtist(
                        null,
                        null,
                        createExhibitArtistRequest.getProfileImgFile(),
                        null,
                        createExhibitArtistRequest.getNameKo(),
                        createExhibitArtistRequest.getNameEn(),
                        createExhibitArtistRequest.getRole(),
                        createExhibitArtistRequest.getEmail(),
                        createExhibitArtistRequest.getInstagramUrl(),
                        createExhibitArtistRequest.getBehanceUrl(),
                        createExhibitArtistRequest.getLinkedinUrl()
                ))
                .collect(Collectors.toList());
    }
}
