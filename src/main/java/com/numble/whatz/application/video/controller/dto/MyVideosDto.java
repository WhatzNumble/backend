package com.numble.whatz.application.video.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MyVideosDto {

    private List<MyVideoDto> videos;

    public MyVideosDto(List<MyVideoDto> videos) {
        this.videos = videos;
    }
}
