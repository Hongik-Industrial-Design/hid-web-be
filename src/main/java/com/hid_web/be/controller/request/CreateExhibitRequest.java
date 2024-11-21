package com.hid_web.be.controller.request;

import com.hid_web.be.domain.exhibit.ExhibitArtist;
import com.hid_web.be.domain.exhibit.ExhibitDetail;
import com.hid_web.be.domain.exhibit.ExhibitImageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateExhibitRequest {
    private MultipartFile mainThumbnailImageFile;
    private List<MultipartFile> additionalThumbnailImageFiles;
    private List<MultipartFile> detailImageFiles;
    private CreateExhibitDetailRequest createExhibitDetailRequest;
    private List<CreateExhibitArtistRequest> createExhibitArtistRequestList;

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
