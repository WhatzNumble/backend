package com.numble.whatz.application.video.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainContentDto {

    private Long id;
    private String videoThumbnail;
    private String title;
    private String nickName;

    public MainContentDto(Long id, String videoThumbnail, String title, String nickName) {
        this.id = id;
        this.videoThumbnail = videoThumbnail;
        this.title = title;
        this.nickName = nickName;
    }
}
