package com.numble.whatz.core.oauth;

import com.numble.whatz.application.Role;
import lombok.Getter;

@Getter
public class TokenDataModel {
    private Long id;
    private String email;
    private Role role;

    public TokenDataModel(Long id, String email, Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

}
