package com.numble.whatz.application.member.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AdminDto {
    String nickName;
    String password;
    String snsId;

    public AdminDto(String nickName, String password, String snsId) {
        this.nickName = nickName;
        this.password = password;
        this.snsId = snsId;
    }
}
