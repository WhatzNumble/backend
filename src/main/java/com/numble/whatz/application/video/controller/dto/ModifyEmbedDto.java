package com.numble.whatz.application.video.controller.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class ModifyEmbedDto {

    private Long id;
    private String link;
    private MultipartFile videoThumbnail;
    private String title;
    private String content;

    public ModifyEmbedDto(String link, MultipartFile videoThumbnail, String title, String content) {
        this.link = link;
        this.videoThumbnail = videoThumbnail;
        this.title = title;
        this.content = content;
    }
}