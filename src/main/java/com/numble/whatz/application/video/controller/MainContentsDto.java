package com.numble.whatz.application.video.controller;

import com.numble.whatz.application.video.controller.dto.MainContentDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MainContentsDto {

    private List<MainContentDto> contents;

    public MainContentsDto(List<MainContentDto> contents) {
        this.contents = contents;
    }
}
