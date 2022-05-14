package com.numble.whatz.application.video.controller.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class ModifyDirectDto {

    private Long id;
    private MultipartFile file;
    private MultipartFile videoThumbnail;
    private String title;
    private String content;
    private String category;

    public ModifyDirectDto(Long id, MultipartFile file, MultipartFile videoThumbnail, String title, String content, String category) {
        this.id = id;
        this.file = file;
        this.videoThumbnail = videoThumbnail;
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
