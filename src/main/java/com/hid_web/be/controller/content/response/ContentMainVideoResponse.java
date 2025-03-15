package com.hid_web.be.controller.content.response;

import com.hid_web.be.domain.s3.S3UrlConverter;
import com.hid_web.be.storage.content.ContentMainVideoEntity;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA, Jackson 라이브러리에서 기본 생성자 필요
@AllArgsConstructor(access = AccessLevel.PROTECTED) // 빌더 패턴에서 필요
public class ContentMainVideoResponse {
    private Long year;
    private String videoUrl;

    public static ContentMainVideoResponse of(ContentMainVideoEntity contentMainVideoEntity) {
        return ContentMainVideoResponse.builder()
                .year(contentMainVideoEntity.getYear())
                .videoUrl(S3UrlConverter.convertCloudfrontUrlFromObjectKey(contentMainVideoEntity.getS3ObjectKey()))
                .build();
    }
}
