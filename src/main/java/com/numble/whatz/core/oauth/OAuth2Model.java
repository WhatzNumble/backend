package com.numble.whatz.core.oauth;

import com.numble.whatz.application.Role;
import com.numble.whatz.application.member.domain.Member;
import lombok.Getter;

@Getter
public class OAuth2Model {
    private String id;
    private String email;
    private String nickName;
    private String thumbnailUrl;

    public OAuth2Model(String id, String email, String nickName, String thumbnailUrl) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .nickName(nickName)
                .thumbnailUrl(thumbnailUrl)
                .snsId(id)
                .role(Role.ROLE_MEMBER)
                .build();
    }
}
