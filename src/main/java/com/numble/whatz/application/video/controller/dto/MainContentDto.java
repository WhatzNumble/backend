package com.numble.whatz.application.video.controller.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainContentDto {

    private Long id;
    private String videoThumbnail;
    private String title;
    private String nickName;

    @Builder
    public MainContentDto(Long id, String videoThumbnail, String title, String nickName) {
        this.id = id;
        this.videoThumbnail = videoThumbnail;
        this.title = title;
        this.nickName = nickName;
    }
}
