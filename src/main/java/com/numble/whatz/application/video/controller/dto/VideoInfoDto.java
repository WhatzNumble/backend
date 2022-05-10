package com.numble.whatz.application.video.controller.dto;

import lombok.*;

import java.time.LocalDateTime;

// api/home 에 쓰이는 DTO
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoInfoDto {

    private String nickname;
    private String profile;
    private Integer videoLike;
    private String videoTitle;
    private String videoContent;
    private LocalDateTime videoCreationDate;
    private Long videoViews;
    private String directDir;
    private String embedLink;

    @Builder
    public VideoInfoDto(String nickname, String profile, int likes, String title, String content, LocalDateTime videoDate, long views, String directDir, String embedLink) {
        this.nickname = nickname;
        this.profile = profile;
        this.videoLike = likes;
        this.videoTitle = title;
        this.videoContent = content;
        this.videoCreationDate = videoDate;
        this.videoViews = views;
        this.directDir = directDir;
        this.embedLink = embedLink;
    }
}
