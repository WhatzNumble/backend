package com.numble.whatz.application.video.controller.dto;

import com.numble.whatz.application.video.domain.DirectVideo;
import com.numble.whatz.application.video.domain.EmbedVideo;
import com.numble.whatz.application.video.domain.Videos;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UserVideoDto {

    private Long id;
    private String type;
    private String title;
    private String description;
    private String thumbnail;
    private String videoUri;

    public UserVideoDto(Long id, String title, String description, String thumbnail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
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
