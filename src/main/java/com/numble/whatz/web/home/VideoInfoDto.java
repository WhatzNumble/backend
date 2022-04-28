package com.numble.whatz.web.home;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class VideoInfoDto {

    private String nickname;
    private String profile;
    private int likes;
    private String title;
    private LocalDateTime videoDate;
    private long views;
    private String videoThumbnail;

    public VideoInfoDto(String nickname, String profile, int likes, String title, LocalDateTime videoDate, long views, String videoThumbnail) {
        this.nickname = nickname;
        this.profile = profile;
        this.likes = likes;
        this.title = title;
        this.videoDate = videoDate;
        this.views = views;
        this.videoThumbnail = videoThumbnail;
    }
}
