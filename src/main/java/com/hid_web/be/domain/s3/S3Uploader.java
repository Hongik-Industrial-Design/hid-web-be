package com.hid_web.be.domain.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class S3Uploader {
    private final S3Client s3Client;

    //@Value("${spring.cloud.aws.s3.bucket}")
    @Value("test")
    private String bucketName;

    //@Value("${spring.cloud.aws.s3.bucket}")  // CloudFront 도메인 환경변수로 설정
    @Value("test")
    private String cloudFrontDomain;

    // 단일 파일 업로드 (빈 파일 검증 추가)
    public String uploadFile(MultipartFile file, String folderPath) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFileName = sanitizeFileName(file.getOriginalFilename());
        String objectKey = folderPath + "/" + originalFileName;

        // S3 업로드
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        return objectKey;
    }

    public Map<String, String> uploadFiles(List<MultipartFile> files, String folderPath) throws IOException {
        Map<String, String> fileMap = new HashMap<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String objectKey = uploadFile(file, folderPath);
                fileMap.put(objectKey, file.getOriginalFilename());
            }
        }
        return fileMap;
    }

    // 단일 파일 삭제 (S3)
    public void deleteFile(String fileUrl) {
        String objectKey = extractObjectKeyFromUrl(fileUrl);

        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build()
        );
    }

    // 다중 파일 삭제
    public void deleteFiles(List<String> fileUrls) {
        if (fileUrls == null || fileUrls.isEmpty()) return;

        for (String fileUrl : fileUrls) {
            String key = fileUrl.replace("https://" + bucketName + ".s3.amazonaws.com/", "");
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
        }
    }

    private String extractObjectKeyFromUrl(String fileUrl) {
        try {
            // 직접 URI를 만들지 않고, S3 URL에서 Key 부분만 안전하게 추출
            String objectKey = fileUrl.replace("https://" + bucketName + ".s3.amazonaws.com/", "");

            // URL 디코딩 적용 (공백 등 변환)
            return URLDecoder.decode(objectKey, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("파일 경로 디코딩 오류", e);
        }
    }

    // 안전한 파일명 변환 (특수 문자 제거)
    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[\\\\/:*?\"<>|]", "_").replaceAll(" ", "_");
    }

    public String generateCloudFrontUrl(String fileKey) {
        return "https://" + cloudFrontDomain + "/" + fileKey;
    }

    public String extractOriginalFileName(String fileKey) {
        return fileKey.substring(fileKey.lastIndexOf("/") + 1);
    }

}