package com.numble.whatz.application.video.controller.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoDetailDto {

    private Long videoId;
    private String nickname;
    private String profile;
    private Integer videoLike;
    private String videoTitle;
    private String videoContent;
    private LocalDateTime videoCreationDate;
    private Long videoViews;
    private String directDir;
    private String embedLink;
    private String videoThumbnail;
    private String category;

    @Builder
    public VideoDetailDto(Long videoId, String nickname, String profile, Integer videoLike, String videoTitle, String videoContent, LocalDateTime videoCreationDate, Long videoViews, String directDir, String embedLink, String videoThumbnail) {
        this.videoId = videoId;
        this.nickname = nickname;
        this.profile = profile;
        this.videoLike = videoLike;
        this.videoTitle = videoTitle;
        this.videoContent = videoContent;
        this.videoCreationDate = videoCreationDate;
        this.videoViews = videoViews;
        this.directDir = directDir;
        this.embedLink = embedLink;
        this.videoThumbnail = videoThumbnail;
    }
}
