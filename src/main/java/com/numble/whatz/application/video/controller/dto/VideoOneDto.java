package com.numble.whatz.application.video.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class VideoOneDto {

    private int likes;
    private String title;
    private LocalDateTime videoDate;
    private long views;
    private String videoThumbnail;
    private String linkOrPath;
    private String content;

    public VideoOneDto(int likes, String title, LocalDateTime videoDate, long views, String videoThumbnail, String linkOrPath, String content) {
        this.likes = likes;
        this.title = title;
        this.videoDate = videoDate;
        this.views = views;
        this.videoThumbnail = videoThumbnail;
        this.linkOrPath = linkOrPath;
        this.content = content;
    }
}
