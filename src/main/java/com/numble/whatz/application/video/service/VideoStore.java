package com.numble.whatz.application.video.service;

import com.numble.whatz.core.advice.dto.VideoStoreExceptionMessage;
import com.numble.whatz.core.exception.video.VideoStoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static com.numble.whatz.application.video.service.PathUtil.getFullPathMp4File;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoStore {

    private final S3Uploader s3Uploader;
    private final FFmpegConverter fFmpegConverter;

    @Value("${file.dir}")
    private String fileDir;

    private String s3dir = "/WhatzDev/";

    public String storeVideo(MultipartFile multipartFile) throws VideoStoreException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String storeFilename = createStoreFilename(uuid, originalFilename); //UUID.mp4
        String executeFilename = createStoreExecuteFilename(uuid); // UUID.m3u8

        File mp4uuidFile = new File(getFullPathMp4File(storeFilename));

        try {
            multipartFile.transferTo(mp4uuidFile);
        } catch (IOException e) {
            throw new VideoStoreException(VideoStoreExceptionMessage.MULTIPART_EX, e);
        }
        fFmpegConverter.convert(storeFilename, executeFilename);

        s3Processor(mp4uuidFile, uuid);

        return s3dir + uuid + "/" + executeFilename;
        // /WhatzDev/uuid/uuid.m3u8
    }

    public String modifyVideo(MultipartFile multipartFile, String beforeFile) throws VideoStoreException {
        deleteVideo(beforeFile);
        String createFile = storeVideo(multipartFile);
        return createFile;
    }

    public void deleteVideo(String fileName) {
        String folderName = fileName.substring(0, fileName.lastIndexOf("/"));
        s3Uploader.deleteFolderS3(folderName);
    }

    private void s3Processor(File mp4uuidFile, String uuid) throws VideoStoreException {
        File executeFile = new File(fileDir + "executeFile/");
        File[] executeFiles = executeFile.listFiles();

        for (File file : executeFiles) {
            s3Uploader.upload(file, s3dir + uuid);
        }
        deleteMp4File(mp4uuidFile);
    }

    private void deleteMp4File(File mp4uuidFile) throws VideoStoreException{
        if (mp4uuidFile.delete())
            return ;
        throw new VideoStoreException(VideoStoreExceptionMessage.LOCAL_DELETE_EX, new IOException());
    }

    private String createStoreFilename(String uuid, String originalFilename) {
        String ext = extractedExt(originalFilename);
        return uuid + "." + ext;
    }

    private String createStoreExecuteFilename(String uuid) {
        return uuid + ".m3u8";
    }

    private String extractedExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
