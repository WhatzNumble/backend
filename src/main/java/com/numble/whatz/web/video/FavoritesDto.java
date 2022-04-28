package com.numble.whatz.web.video;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FavoritesDto {

    private Long videoId;
    private String videoThumbnail;
    private Long views;

    public FavoritesDto(Long videoId, String videoThumbnail, Long views) {
        this.videoId = videoId;
        this.videoThumbnail = videoThumbnail;
        this.views = views;
    }
}
