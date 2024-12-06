package com.hid_web.be.controller.request;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;
import lombok.*;
import com.hid_web.be.domain.exhibit.ExhibitAdditionalThumbnailImage;
import com.hid_web.be.domain.exhibit.ExhibitArtist;
import com.hid_web.be.domain.exhibit.ExhibitDetail;
import com.hid_web.be.domain.exhibit.ExhibitDetailImage;

@Getter
@Setter
public class CreateExhibitRequest {
    private MultipartFile mainThumbnailImageFile;
    private List<CreateExhibitAdditionalThumbnailImageRequest> createAdditionalThumbnailImageRequests;
    private List<CreateExhibitDetailImageRequest> createDetailImageRequests;
    private CreateExhibitDetailRequest createExhibitDetailRequest;
    private List<CreateExhibitArtistRequest> createExhibitArtistRequestList;

    public List<ExhibitAdditionalThumbnailImage> toAdditionalThumbnailImages() {
        if (createAdditionalThumbnailImageRequests == null) {
            return null;
        }

        return createAdditionalThumbnailImageRequests.stream()
                .map(request -> new ExhibitAdditionalThumbnailImage(
                        request.getFile(),
                        request.getUrl(),
                        request.getPosition()
                ))
                .collect(Collectors.toList());
    }

    public List<ExhibitDetailImage> toDetailImages() {
        if (createDetailImageRequests == null) {
            return null;
        }

        return createDetailImageRequests.stream()
                .map(request -> new ExhibitDetailImage(
                        request.getFile(),
                        request.getUrl(),
                        request.getPosition()
                ))
                .collect(Collectors.toList());
    }

    public ExhibitDetail toExhibitDetail() {
        return new ExhibitDetail(
                createExhibitDetailRequest.getTitleKo(),
                createExhibitDetailRequest.getTitleEn(),
                createExhibitDetailRequest.getSubtitleKo(),
                createExhibitDetailRequest.getSubtitleEn(),
                createExhibitDetailRequest.getTextKo(),
                createExhibitDetailRequest.getTextEn(),
                createExhibitDetailRequest.getVideoUrl()
        );
    }

    public List<ExhibitArtist> toExhibitArtistList() {
        return createExhibitArtistRequestList.stream()
                .map(createExhibitArtistRequest -> new ExhibitArtist(
                        null,
                        null,
                        createExhibitArtistRequest.getProfileImageFile(),
                        null,
                        createExhibitArtistRequest.getArtistNameKo(),
                        createExhibitArtistRequest.getArtistNameEn(),
                        createExhibitArtistRequest.getRole(),
                        createExhibitArtistRequest.getEmail(),
                        createExhibitArtistRequest.getInstagramUrl(),
                        createExhibitArtistRequest.getBehanceUrl(),
                        createExhibitArtistRequest.getLinkedinUrl()
                ))
                .collect(Collectors.toList());
    }
}
