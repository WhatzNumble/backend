package com.numble.whatz.api.video.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LinkDto {

    private String link;

    public LinkDto(String link) {
        this.link = link;
    }
}
