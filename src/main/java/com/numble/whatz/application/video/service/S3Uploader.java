package com.numble.whatz.application.video.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.numble.whatz.core.advice.dto.VideoStoreExceptionMessage;
import com.numble.whatz.core.exception.video.CustomVideoStoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ListIterator;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public String upload(File uploadFile, String dirName) throws CustomVideoStoreException {
        String uploadImageUrl = putS3(uploadFile, dirName); // s3로 업로드
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // S3 파일 수정 - 사용 안함
    public String modify(File uploadFile, String dirName, String beforeName) throws CustomVideoStoreException {
        deleteFolderS3("/WhatzDev/" + beforeName);
        return upload(uploadFile, dirName);
    }

    // S3로 업로드
    private String putS3(File uploadFile, String dirName) throws CustomVideoStoreException {
        try {
            ObjectMetadata objectMetadata = getObjectMetadata(uploadFile);
            InputStream inputStream = new FileInputStream(uploadFile);

            amazonS3Client.putObject(bucket + dirName, uploadFile.getName(), inputStream, objectMetadata);
            return amazonS3Client.getUrl(bucket + dirName, uploadFile.getName()).toString();
        } catch (IOException e) {
            throw new CustomVideoStoreException(VideoStoreExceptionMessage.S3_UPLOAD_EX, e);
        }
    }

    // S3 폴더 삭제
    public void deleteFolderS3(String folderName) {
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request()
                .withBucketName(bucket).withPrefix(folderName.substring(1));
        ListObjectsV2Result listObjectsV2Result = amazonS3Client.listObjectsV2(listObjectsV2Request);
        ListIterator<S3ObjectSummary> listIterator = listObjectsV2Result.getObjectSummaries().listIterator();

        while (listIterator.hasNext()) {
            S3ObjectSummary objectSummary = listIterator.next();
            DeleteObjectRequest request = new DeleteObjectRequest(bucket, objectSummary.getKey());
            amazonS3Client.deleteObject(request);
        }
    }

    private ObjectMetadata getObjectMetadata(File uploadFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        int pos = uploadFile.getName().lastIndexOf(".");
        String ext = uploadFile.getName().substring(pos + 1);

        objectMetadata.setContentLength(uploadFile.length());
        if (ext.equals("m3u8")) objectMetadata.setContentType("application/x-mpegURL");
        else if (ext.equals("ts")) objectMetadata.setContentType("video/MP2T");
        return objectMetadata;
    }

    // 로컬에 저장된 파일 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            return;
        }
        log.info("File delete fail");
    }

    public void uploadThumbnail(MultipartFile file, String dir, String contentType) throws CustomVideoStoreException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(contentType);

        System.out.println("file.getName() = " + file.getName());
        System.out.println("bucket + dir = " + bucket + dir);

        try {
            InputStream inputStream = file.getInputStream();
            amazonS3Client.putObject(bucket + dir, file.getName(), inputStream, objectMetadata);
        } catch (IOException e) {
            throw new CustomVideoStoreException(VideoStoreExceptionMessage.S3_UPLOAD_EX, e);
        }

    }
}
