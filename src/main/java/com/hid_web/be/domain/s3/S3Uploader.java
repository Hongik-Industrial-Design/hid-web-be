package com.hid_web.be.domain.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Uploader {
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    // 단일 파일 업로드 (빈 파일 검증 추가)
    public String uploadFile(MultipartFile file, String folderPath) throws IOException {
        if (file == null || file.isEmpty()) {  // 빈 파일이면 업로드하지 않음
            return null;
        }

        String uuid = UUID.randomUUID().toString();
        String objectKey = folderPath + "/" + uuid + "_" + file.getOriginalFilename();
        return uploadToS3(file, objectKey);
    }

    // 다중 파일 업로드 (빈 파일 검증 추가)
    public List<String> uploadFiles(List<MultipartFile> files, String folderPath) throws IOException {
        List<String> urls = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {  // 빈 파일이면 건너뜀
                    urls.add(uploadFile(file, folderPath));
                }
            }
        }
        return urls;
    }

    // S3 업로드 로직
    private String uploadToS3(MultipartFile file, String objectKey) throws IOException {
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        return "https://" + bucketName + ".s3.amazonaws.com/" + objectKey;
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

    // 다중 파일 삭제 (S3)
    public void deleteFiles(List<String> fileUrls) {
        if (fileUrls != null && !fileUrls.isEmpty()) {
            for (String fileUrl : fileUrls) {
                deleteFile(fileUrl);
            }
        }
    }

    // S3 URL에서 Object Key 추출
    private String extractObjectKeyFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.indexOf(".com/") + 5);
        // 예: https://bucket-name.s3.amazonaws.com/folder/uuid_file.jpg → folder/uuid_file.jpg
    }

}