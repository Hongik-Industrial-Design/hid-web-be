package com.hid_web.be.domain.s3;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.*;

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

        // 파일명 안전하게 인코딩 (공백 처리 포함)
        String encodedOriginalFileName = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        String randomFileName = UUID.randomUUID().toString();
        String objectKey = folderPath + "/" + randomFileName + "_" + encodedOriginalFileName;

        // S3 업로드
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        // 업로드된 파일 URL 반환
        return "https://" + bucketName + ".s3.amazonaws.com/" + objectKey;
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
    private String uploadToS3(MultipartFile file, String folderPath) throws IOException {
        // 파일명 인코딩 및 공백 처리
        String encodedFileName = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8.toString())
                .replaceAll("\\+", "%20"); // 공백을 %20으로 변환

        // 무작위 UUID로 안전한 파일명 생성 (보안 강화)
        String randomFileName = UUID.randomUUID().toString();

        // S3에 저장될 object key 생성
        String objectKey = folderPath + "/" + randomFileName;

        // S3에 파일 업로드
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .contentType(file.getContentType()) // 파일 형식 설정
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        // 업로드된 파일 URL 반환
        return "https://" + bucketName + ".s3.amazonaws.com/" + objectKey;
    }

    // 여러 개의 S3 파일을 ZIP으로 묶어 다운로드
    public byte[] downloadAllAsZip(List<String> fileUrls) throws IOException, URISyntaxException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // 한글 파일명을 위한 UTF-8 인코딩 설정
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream, Charset.forName("UTF-8"))) {
            for (String fileUrl : fileUrls) {
                String originalFileName = extractOriginalFileNameFromUrl(fileUrl);
                byte[] fileData = downloadFileFromS3(fileUrl);

                ZipEntry zipEntry = new ZipEntry(originalFileName);
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(fileData);
                zipOutputStream.closeEntry();
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    // S3에서 개별 파일 다운로드
    private byte[] downloadFileFromS3(String fileUrl) throws IOException, URISyntaxException {
        String objectKey = extractObjectKeyFromUrl(fileUrl);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest)) {
            return IOUtils.toByteArray(s3Object); // 파일 데이터 읽기
        }
    }

//    // 단일 파일 삭제 (S3)
//    public void deleteFile(String fileUrl) {
//        String objectKey = extractObjectKeyFromUrl(fileUrl);
//
//        s3Client.deleteObject(
//                DeleteObjectRequest.builder()
//                        .bucket(bucketName)
//                        .key(objectKey)
//                        .build()
//        );
//    }

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

//    // S3 URL에서 Object Key 추출
//    private String extractObjectKeyFromUrl(String fileUrl) {
//        try {
//            URI uri = new URI(fileUrl);
//            return uri.getPath().substring(1); // 앞의 '/' 제거
//        } catch (URISyntaxException e) {
//            throw new IllegalArgumentException("잘못된 파일 URL 형식입니다: " + fileUrl, e);
//        }
//    }

    private String extractObjectKeyFromUrl(String fileUrl) throws URISyntaxException {
        URI uri = new URI(fileUrl);
        String path = uri.getPath(); // 예: "/notice/uuid/attachments/파일명"

        // 첫 번째 "/" 제거 후 반환
        return path.startsWith("/") ? path.substring(1) : path;
    }


    private String extractFileNameFromUrl(String fileUrl) {
        try {
            // URL에서 파일명 부분만 추출
            URI uri = new URI(fileUrl);
            String path = uri.getPath(); // "/bucket-name/folder/filename.ext"

            return path.substring(path.lastIndexOf("/") + 1);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("잘못된 파일 URL 형식입니다: " + fileUrl, e);
        }
    }

    private String extractOriginalFileNameFromUrl(String fileUrl) {
        String encodedFileName = fileUrl.substring(fileUrl.lastIndexOf("_") + 1);
        try {
            return URLDecoder.decode(encodedFileName, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("파일명 디코딩 오류", e);
        }
    }

    // 한글 파일명을 올바르게 변환하는 메서드
    private String encodeFileName(String fileName) {
        try {
            return URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20"); // 공백 처리
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("파일명 인코딩 오류", e);
        }
    }


}