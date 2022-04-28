package com.numble.whatz.core.oauth;

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
}
