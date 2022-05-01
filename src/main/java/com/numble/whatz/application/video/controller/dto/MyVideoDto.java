package com.numble.whatz.application.video.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyVideoDto {

    private Long videoId;
    private String videoThumbnail;
    private String directDir;
    private String embedLink;
    private String title;
    private String content;
    private long views;

    public MyVideoDto(Long videoId, String videoThumbnail, String direct_dir, String youtube_link, String title, String content, long views) {
        this.videoId = videoId;
        this.videoThumbnail = videoThumbnail;
        this.directDir = direct_dir;
        this.embedLink = youtube_link;
        this.title = title;
        this.content = content;
        this.views = views;
    }
}
