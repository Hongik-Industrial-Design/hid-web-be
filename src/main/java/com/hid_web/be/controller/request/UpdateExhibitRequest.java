package com.hid_web.be.controller.request;

import com.hid_web.be.domain.exhibit.ExhibitSubImg;
import com.hid_web.be.domain.exhibit.ExhibitArtist;
import com.hid_web.be.domain.exhibit.ExhibitDetail;
import com.hid_web.be.domain.exhibit.ExhibitDetailImg;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExhibitRequest {
    private MultipartFile mainImgFile;
    private List<UpdateExhibitSubImgRequest> subImgs;
    private List<UpdateExhibitDetailImgRequest> detailImgs;
    private UpdateExhibitDetailRequest details;
    private List<UpdateExhibitArtistRequest> artists;

    public List<ExhibitSubImg> toSubImgs() {
        if (subImgs == null) {
            return null;
        }

        return subImgs.stream()
                .map(request -> new ExhibitSubImg(
                        request.getFile(),
                        request.getUrl(),
                        request.getPosition(),
                        request.getType()
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
                details.getExhibitType(),
                details.getYear(),
                details.getMajor(),
                details.getClub(),
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
