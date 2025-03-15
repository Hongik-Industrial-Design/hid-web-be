package com.hid_web.be.domain.s3;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class S3UrlConverter {
    public static String convertObjectKeyFromUrl(String url) {
        // CloudFront URL과 S3 URL 모두 처리
        int comIndex = url.indexOf(".com");
        int netIndex = url.indexOf(".net");

        if (comIndex != -1) {
            return url.substring(comIndex + 5);
        } else if (netIndex != -1) {
            return url.substring(netIndex + 5);
        } else {
            throw new IllegalArgumentException("URL 형식이 S3 또는 CloudFront 형식이 아닙니다: " + url);
        }
    }

    public static String convertCloudfrontUrlFromObjectKey(String objectKey) {
        String CLOUDFRONT_DEV_DOMAIN= "df4nh3dlx8zzx.cloudfront.net";
        String CLOUDFRONT_PRD_DOMAIN = "di00vgoc2ngki.cloudfront.net";
        return "https://" + CLOUDFRONT_PRD_DOMAIN + "/" + objectKey;
    }
}
