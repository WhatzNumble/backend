package com.numble.whatz.application.like.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FavoritesDto {

    private Long videoId;
    private String videoThumbnail;

    public FavoritesDto(Long videoId, String videoThumbnail) {
        this.videoId = videoId;
        this.videoThumbnail = videoThumbnail;
    }
}
