package com.numble.whatz.web.home;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class HomeDto {

    private String nickname;
    private String profile;
    private boolean isLike;
    private int likes;
    private String title;
    private LocalDateTime videoDate;
    private long views;
    private String videoThumbnail;

    public HomeDto(String nickname, String profile, boolean isLike, int likes, String title, LocalDateTime videoDate, long views, String videoThumbnail) {
        this.nickname = nickname;
        this.profile = profile;
        this.isLike = isLike;
        this.likes = likes;
        this.title = title;
        this.videoDate = videoDate;
        this.views = views;
        this.videoThumbnail = videoThumbnail;
    }
}
