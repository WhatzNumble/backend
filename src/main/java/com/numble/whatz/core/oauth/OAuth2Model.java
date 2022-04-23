package com.numble.whatz.core.oauth;

import lombok.Getter;

@Getter
public class OAuth2Model {
    private String id;
    private String email;
    private SnsType snsType;

    public OAuth2Model(String id, String email, SnsType snsType) {
        this.id = id;
        this.email = email;
        this.snsType = snsType;
    }
}
