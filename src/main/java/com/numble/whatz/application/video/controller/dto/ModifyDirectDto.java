package com.numble.whatz.application.video.controller.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class ModifyDirectDto {

    private Long id;
    private MultipartFile file;
    private String videoThumbnail;
    private String title;
    private String content;

    public ModifyDirectDto(Long id, MultipartFile file, String videoThumbnail, String title, String content) {
        this.id = id;
        this.file = file;
        this.videoThumbnail = videoThumbnail;
        this.title = title;
        this.content = content;
    }
}
