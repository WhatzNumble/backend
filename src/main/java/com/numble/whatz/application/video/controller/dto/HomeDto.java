package com.numble.whatz.application.video.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class HomeDto {

    private List<VideoInfoDto> videos;
    private List<Long> likeList;

    public HomeDto(List<VideoInfoDto> videos, List<Long> likeList) {
        this.videos = videos;
        this.likeList = likeList;
    }
}
