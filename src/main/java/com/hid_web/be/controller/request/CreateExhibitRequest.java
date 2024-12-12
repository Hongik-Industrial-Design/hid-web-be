package com.hid_web.be.controller.request;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;
import lombok.*;
import com.hid_web.be.domain.exhibit.ExhibitSubImg;
import com.hid_web.be.domain.exhibit.ExhibitArtist;
import com.hid_web.be.domain.exhibit.ExhibitDetail;
import com.hid_web.be.domain.exhibit.ExhibitDetailImg;

@Getter
@Setter
public class CreateExhibitRequest {
    private MultipartFile mainImgFile;
    private List<CreateExhibitSubImgRequest> subImgs;
    private List<CreateExhibitDetailImgRequest> detailImgs;
    private CreateExhibitDetailRequest details;
    private List<CreateExhibitArtistRequest> artists;

    public List<ExhibitSubImg> toSubImgs() {
        if (subImgs == null) {
            return null;
        }

        return subImgs.stream()
                .map(request -> new ExhibitSubImg(
                        request.getFile(),
                        request.getPosition()
                ))
                .collect(Collectors.toList());
    }

    public List<ExhibitDetailImg> toDetailImgs() {
        if (detailImgs == null) {
            return null;
        }

        return detailImgs.stream()
                .map(request -> new ExhibitDetailImg(
                        request.getFile(),
                        request.getUrl(),
                        request.getPosition()
                ))
                .collect(Collectors.toList());
    }

    public ExhibitDetail toDetails() {
        return new ExhibitDetail(
                details.getTitleKo(),
                details.getTitleEn(),
                details.getSubTitleKo(),
                details.getSubTitleEn(),
                details.getTextKo(),
                details.getTextEn(),
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
