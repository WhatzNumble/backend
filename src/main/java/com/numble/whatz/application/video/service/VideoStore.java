package com.numble.whatz.application.video.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoStore {
    String storeVideo(MultipartFile multipartFile) throws IOException;
    String modifyVideo(MultipartFile multipartFile, String beforeFile) throws IOException;
    void deleteVideo(String fileName) throws IOException;
}
