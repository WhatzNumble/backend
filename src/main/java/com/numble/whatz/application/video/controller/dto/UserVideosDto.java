package com.numble.whatz.application.video.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class UserVideosDto {

    private Long userId;
    private String email;
    private String nickname;
//    private LocalDateTime date;
//    private LocalDateTime last;
    private List<UserVideoDto> videos;

    public UserVideosDto(Long userId, String email, String nickname, List<UserVideoDto> videos) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.videos = videos;
    }
}
