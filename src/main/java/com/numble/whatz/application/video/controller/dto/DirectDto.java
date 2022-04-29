package com.numble.whatz.application.video.controller.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter @Getter
public class DirectDto {

    private MultipartFile file;
    private String videoThumbnail;
    private String title;
    private String content;

    public DirectDto(MultipartFile file, String videoThumbnail, String title, String content) {
        this.file = file;
        this.videoThumbnail = videoThumbnail;
        this.title = title;
        this.content = content;
    }
}
