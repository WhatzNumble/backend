package com.numble.whatz.application.video.controller.dto;

import com.numble.whatz.application.video.domain.DirectVideo;
import com.numble.whatz.application.video.domain.EmbedVideo;
import com.numble.whatz.application.video.domain.Videos;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainContentDetailDto {

    private Long videoId;
    private Long showId;
    private String nickname;
    private Long userId;
    private String userEmail;
    private Integer videoLike;
    private String videoTitle;
    private String videoContent;
    private LocalDateTime videoCreationDate;
    private Long videoViews;
    private String videoUrl;
    private String videoThumbnail;
    private String type;

    @Builder
    public MainContentDetailDto(Long showId, Long userId, String userEmail, Long videoId, String nickname, Integer videoLike, String videoTitle, String videoContent, LocalDateTime videoCreationDate, Long videoViews, String videoThumbnail) {
        this.showId = showId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.videoId = videoId;
        this.nickname = nickname;
        this.videoLike = videoLike;
        this.videoTitle = videoTitle;
        this.videoContent = videoContent;
        this.videoCreationDate = videoCreationDate;
        this.videoViews = videoViews;
        this.videoThumbnail = videoThumbnail;
    }

    public void addVideoUrl(Videos videos) {
        if (videos instanceof DirectVideo) {
            this.videoUrl = ((DirectVideo)videos).getDirectDir();
            this.type = "self";
        } else {
            this.videoUrl = ((EmbedVideo)videos).getLink();
            this.type = "embed";
        }
    }
}
