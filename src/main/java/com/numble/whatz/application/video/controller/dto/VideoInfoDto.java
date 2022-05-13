package com.numble.whatz.application.video.controller.dto;

import com.numble.whatz.application.video.domain.DirectVideo;
import com.numble.whatz.application.video.domain.EmbedVideo;
import com.numble.whatz.application.video.domain.Videos;
import lombok.*;

import java.time.LocalDateTime;

// api/home 에 쓰이는 DTO
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoInfoDto {

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

    @Builder
    public VideoInfoDto(Long videoId, String nickname, String profile, int likes, String title, String content, LocalDateTime videoDate, long views, String directDir, String embedLink) {
        this.videoId = videoId;
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

    public VideoInfoDto(Long videoId, String nickname, String profile, Integer videoLike, String videoTitle, String videoContent, LocalDateTime videoCreationDate, Long videoViews, Videos DorE) {
        this.videoId = videoId;
        this.nickname = nickname;
        this.profile = profile;
        this.videoLike = videoLike;
        this.videoTitle = videoTitle;
        this.videoContent = videoContent;
        this.videoCreationDate = videoCreationDate;
        this.videoViews = videoViews;
        if (DorE instanceof DirectVideo) this.directDir = ((DirectVideo) DorE).getDirectDir();
        else this.embedLink = ((EmbedVideo) DorE).getLink();
    }
}
