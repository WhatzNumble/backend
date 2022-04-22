package com.numble.whatz.core.oauth;

import java.util.HashMap;
import java.util.Map;

public class OAuth2Attribute {
    private String id;
    private String email;

    public OAuth2Attribute(String id, String email) {
        this.id = id;
        this.email = email;
    }

    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("email", email);

        return map;
    }

    public static OAuth2Attribute of(String provier, Map<String, Object> attributes) {
        if (provier.equals("kakao"))
            return kakao(attributes);

        throw new RuntimeException();
    }

    private static OAuth2Attribute kakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        return new OAuth2Attribute(
                 attributes.get("id").toString(),
                (String) kakaoAccount.get("email"));
    }

}
