package com.numble.whatz.application.member.controller;

import lombok.Getter;

@Getter
public class MemberDto {
    private String email;
    private String nickName;
    private String thumbnailUrl;

    public MemberDto(String email, String nickName, String thumbnailUrl) {
        this.email = email;
        this.nickName = nickName;
        this.thumbnailUrl = thumbnailUrl;
    }
}
