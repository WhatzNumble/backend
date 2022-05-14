package com.numble.whatz.application.video.controller.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter @Getter
public class DirectDto {

    private MultipartFile file;
    private MultipartFile videoThumbnail;
    private String title;
    private String content;
    private String category;

    public DirectDto(MultipartFile file, MultipartFile videoThumbnail, String title, String content, String category) {
        this.file = file;
        this.videoThumbnail = videoThumbnail;
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
