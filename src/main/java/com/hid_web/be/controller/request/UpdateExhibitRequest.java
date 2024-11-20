package com.hid_web.be.controller.request;

import com.hid_web.be.domain.exhibit.ExhibitArtist;
import com.hid_web.be.domain.exhibit.ExhibitDetail;
import com.hid_web.be.domain.exhibit.ExhibitAdditionalThumbnailImage;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExhibitRequest {
    private MultipartFile mainThumbnailImageFile;
    private List<UpdateExhibitAdditionalThumbnailImageRequest> additionalThumbnailImageRequests;
    private UpdateExhibitDetailRequest updateExhibitDetailRequest;
    private List<UpdateExhibitArtistRequest> updateExhibitArtistRequests;

    public List<ExhibitAdditionalThumbnailImage> toAdditionalThumbnailImages() {
        if (additionalThumbnailImageRequests == null) {
            return null;
        }

        return additionalThumbnailImageRequests.stream()
                .map(request -> new ExhibitAdditionalThumbnailImage(
                        request.getFile(),
                        request.getUrl(),
                        request.getPosition(),
                        request.getType()
                ))
                .collect(Collectors.toList());
    }

    public List<ExhibitAdditionalThumbnailImage> toDetailImages() {
        if (additionalThumbnailImageRequests == null) {
            return null;
        }

        return additionalThumbnailImageRequests.stream()
                .map(request -> new ExhibitAdditionalThumbnailImage(
                        request.getFile(),
                        request.getUrl(),
                        request.getPosition(),
                        request.getType()
                ))
                .collect(Collectors.toList());
    }

    public ExhibitDetail toExhibitDetail() {
        if (updateExhibitDetailRequest == null) {
            return null;
        }

        return new ExhibitDetail(
                updateExhibitDetailRequest.getTitleKo(),
                updateExhibitDetailRequest.getTitleEn(),
                updateExhibitDetailRequest.getSubtitleKo(),
                updateExhibitDetailRequest.getSubtitleEn(),
                updateExhibitDetailRequest.getTextKo(),
                updateExhibitDetailRequest.getTextEn(),
                updateExhibitDetailRequest.getVideoUrl()
        );
    }

    public List<ExhibitArtist> toExhibitArtistList() {
        if (updateExhibitArtistRequests == null) {
            return null;
        }

        return updateExhibitArtistRequests.stream()
                .map(updateExhibitArtistRequest -> new ExhibitArtist(
                        updateExhibitArtistRequest.getProfileImageFile(),
                        null,
                        updateExhibitArtistRequest.getArtistNameKo(),
                        updateExhibitArtistRequest.getArtistNameEn(),
                        updateExhibitArtistRequest.getRole(),
                        updateExhibitArtistRequest.getEmail(),
                        updateExhibitArtistRequest.getInstagramUrl(),
                        updateExhibitArtistRequest.getBehanceUrl(),
                        updateExhibitArtistRequest.getLinkedinUrl()
                ))
                .collect(Collectors.toList());
    }
}
