package com.hid_web.be.service;

import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Writer {
    private final S3Operations s3Operations;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public String writeFile(MultipartFile file, String folderPath) throws IOException {
        return uploadSingleFile(file, folderPath);
    }

    public List<String> writeFiles(List<MultipartFile> files, String folderPath) throws IOException {
        List<String> urls = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                urls.add(uploadSingleFile(file, folderPath));
            }
        }
        return urls;
    }

    public String uploadSingleFile(MultipartFile file, String folderPath) throws IOException {
        String objectKey = folderPath + "/" + file.getOriginalFilename();

        try (InputStream inputStream = file.getInputStream()) {
            s3Operations.upload(bucketName, objectKey, inputStream);
        }

        return generateFileUrl(objectKey);
    }

    public String generateFileUrl(String objectKey) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + objectKey;
    }

    public void deleteFolder(String folderPath) {
        List<S3Resource> objects = s3Operations.listObjects(bucketName, folderPath);

        for (S3Resource object : objects) {
            s3Operations.deleteObject(bucketName, object.getLocation().getObject());
        }
    }
}
