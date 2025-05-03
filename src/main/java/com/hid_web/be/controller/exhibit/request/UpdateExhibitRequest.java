package com.hid_web.be.controller.exhibit.request;

import com.hid_web.be.domain.exhibit.ExhibitArtist;
import com.hid_web.be.domain.exhibit.ExhibitDetail;
import com.hid_web.be.domain.exhibit.ExhibitDetailImg;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UpdateExhibitRequest {
    private MultipartFile mainImgFile;

    @Valid
    private List<UpdateExhibitDetailImgRequest> detailImgs;
    @Valid
    private UpdateExhibitDetailRequest details;
    @Valid
    private List<UpdateExhibitArtistRequest> artists;

    public List<ExhibitDetailImg> toDetailImgs() {
        if (detailImgs == null) {
            return null;
        }

        return detailImgs.stream()
                .map(request -> new ExhibitDetailImg(
                        request.getFile(),
                        request.getUrl(),
                        request.getPosition(),
                        request.getType()
                ))
                .collect(Collectors.toList());
    }

    public ExhibitDetail toDetails() {
        if (details == null) {
            return null;
        }

        return new ExhibitDetail(
                details.getYear(),
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
        if (artists == null) {
            return null;
        }

        return artists.stream()
                .map(updateExhibitArtistRequest -> new ExhibitArtist(
                        updateExhibitArtistRequest.getType(),
                        updateExhibitArtistRequest.getArtistUUID(),
                        updateExhibitArtistRequest.getProfileImgFile(),
                        updateExhibitArtistRequest.getProfileImgUrl(),
                        updateExhibitArtistRequest.getNameKo(),
                        updateExhibitArtistRequest.getNameEn(),
                        updateExhibitArtistRequest.getRole(),
                        updateExhibitArtistRequest.getEmail(),
                        updateExhibitArtistRequest.getInstagramUrl(),
                        updateExhibitArtistRequest.getBehanceUrl(),
                        updateExhibitArtistRequest.getLinkedinUrl()
                ))
                .collect(Collectors.toList());
    }
}
