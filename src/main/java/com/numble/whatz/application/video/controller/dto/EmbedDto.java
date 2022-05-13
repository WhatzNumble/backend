package com.numble.whatz.application.video.controller.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class EmbedDto {

    private String link;
    private MultipartFile videoThumbnail;
    private String title;
    private String content;
    private String category;

    public EmbedDto(String link, MultipartFile videoThumbnail, String title, String content, String category) {
        this.link = link;
        this.videoThumbnail = videoThumbnail;
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
