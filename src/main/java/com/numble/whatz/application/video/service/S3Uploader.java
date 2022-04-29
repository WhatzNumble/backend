package com.numble.whatz.application.video.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public String upload(File uploadFile, String dirName) throws IOException {
        String uploadImageUrl = putS3(uploadFile, dirName); // s3로 업로드
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String dirName) throws IOException {
        ObjectMetadata objectMetadata = getObjectMetadata(uploadFile);
        InputStream inputStream = new FileInputStream(uploadFile);

        amazonS3Client.putObject(bucket + dirName, uploadFile.getName(), inputStream, objectMetadata);
        return amazonS3Client.getUrl(bucket + dirName, uploadFile.getName()).toString();
    }

    private ObjectMetadata getObjectMetadata(File uploadFile) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        int pos = uploadFile.getName().lastIndexOf(".");
        String ext = uploadFile.getName().substring(pos + 1);

        objectMetadata.setContentLength(uploadFile.length());
        if (ext.equals("m3u8")) objectMetadata.setContentType("application/x-mpegURL");
        else if (ext.equals("ts")) objectMetadata.setContentType("video/MP2T");
        return objectMetadata;
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            return;
        }
        log.info("File delete fail");
    }
}
