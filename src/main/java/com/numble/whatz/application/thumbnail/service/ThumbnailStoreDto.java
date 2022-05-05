package com.numble.whatz.application.thumbnail.service;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ThumbnailStoreDto {

    private String executeName;
    private String cutName;

    public ThumbnailStoreDto(String executeName, String cutName) {
        this.executeName = executeName;
        this.cutName = cutName;
    }
}
