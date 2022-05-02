package com.numble.whatz.application.video.service;

import com.numble.whatz.core.advice.dto.VideoStoreExceptionMessage;
import com.numble.whatz.core.exception.video.CustomVideoStoreException;
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
public class VideoStoreImpl implements VideoStore{

    private final S3Uploader s3Uploader;
    private final FFmpegConverter fFmpegConverter;

    @Value("${file.dir}")
    private String fileDir;

    @Override
    public String storeVideo(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String storeFilename = createStoreFilename(uuid, originalFilename); //UUID.mp4
        String executeFilename = createStoreExecuteFilename(uuid); // UUID.m3u8

        System.out.println("executeFilename = " + executeFilename);
        System.out.println("storeFilename = " + storeFilename);


        File mp4uuidFile = new File(getFullPathMp4File(storeFilename));
        System.out.println("mp4uuidFile = " + mp4uuidFile.getAbsolutePath());
        try {
            multipartFile.transferTo(mp4uuidFile);
        } catch (IOException e) {
            throw new CustomVideoStoreException(VideoStoreExceptionMessage.MULTIPART_EX, e);
        }
        fFmpegConverter.convert(storeFilename, executeFilename);

        s3Processor(mp4uuidFile);

        return executeFilename;
    }

    @Override
    public String modifyVideo(MultipartFile multipartFile) throws IOException {
        return "modifyFilename";
    }

    private void s3Processor(File mp4uuidFile) throws CustomVideoStoreException {
        File executeFile = new File(fileDir + "executeFile/");
        File[] executeFiles = executeFile.listFiles();

        for (File file : executeFiles) {
            s3Uploader.upload(file, "/WhatzDev");
        }
        deleteMp4File(mp4uuidFile);
    }

    private void deleteMp4File(File mp4uuidFile) {
        if (mp4uuidFile.delete())
            return ;
        log.info("deleteMp4File fail");
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
