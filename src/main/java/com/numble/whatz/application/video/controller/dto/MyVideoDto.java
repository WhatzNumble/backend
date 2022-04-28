package com.numble.whatz.application.video.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyVideoDto {

    private Long videoId;
    private String videoThumbnail;
    private long views;

    public MyVideoDto(Long videoId, String videoThumbnail, long views) {
        this.videoId = videoId;
        this.videoThumbnail = videoThumbnail;
        this.views = views;
    }
}
