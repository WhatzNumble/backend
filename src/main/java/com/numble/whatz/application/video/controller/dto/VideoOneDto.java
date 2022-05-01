package com.numble.whatz.application.video.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class VideoOneDto {

    private String nickname;
    private String profile;
    private int likes;
    private String title;
    private String content;
    private LocalDateTime videoDate;
    private long views;
    private String directDir;
    private String embedLink;

    public VideoOneDto(String nickname, String profile, int likes, String title, String content, LocalDateTime videoDate, long views, String directDir, String embedLink) {
        this.nickname = nickname;
        this.profile = profile;
        this.likes = likes;
        this.title = title;
        this.content = content;
        this.videoDate = videoDate;
        this.views = views;
        this.directDir = directDir;
        this.embedLink = embedLink;
    }
}
