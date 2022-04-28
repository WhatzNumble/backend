package com.numble.whatz.core.oauth;

import com.numble.whatz.application.Role;
import com.numble.whatz.application.member.domain.Member;
import lombok.Getter;

@Getter
public class OAuth2Model {
    private String id;
    private String email;
    private Sns snsType;

    public OAuth2Model(String id, String email, Sns snsType) {
        this.id = id;
        this.email = email;
        this.snsType = snsType;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .nickName(null)
                .snsId(id)
                .role(Role.ROLE_MEMBER)
                .build();
    }
}
