package com.numble.whatz.web.video;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmbedDto {

    private String link;
    private String videoThumbnail;
    private String title;
    private String content;

    public EmbedDto(String link, String videoThumbnail, String title, String content) {
        this.link = link;
        this.videoThumbnail = videoThumbnail;
        this.title = title;
        this.content = content;
    }
}
