package com.numble.whatz.application.video.controller.dto;

import com.numble.whatz.application.video.domain.DirectVideo;
import com.numble.whatz.application.video.domain.EmbedVideo;
import com.numble.whatz.application.video.domain.Videos;
import lombok.*;

@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserVideoDto {

    private Long id;
    private String type;
    private String title;
    private String description;
    private String thumbnail;
    private String videoUri;

    @Builder
    public UserVideoDto(Videos videos) {
        this.id = videos.getId();
        this.title = videos.getVideoTitle();
        this.description = videos.getVideoContent();
        this.thumbnail = videos.getThumbnail().getCutFile();
        setType(videos);
    }

    public UserVideoDto(Long id, String type, String title, String description, String thumbnail, String videoUri) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.videoUri = videoUri;
    }

    public void setType(Videos videos) {
        if (videos instanceof DirectVideo) {
            this.type = "self";
            this.videoUri = ((DirectVideo) videos).getDirectDir();
        }
        else {
            this.type = "embed";
            this.videoUri = ((EmbedVideo) videos).getLink();
        }
    }
}
