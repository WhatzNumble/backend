package com.numble.whatz.application.member.service;

import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.core.oauth.OAuth2Model;

public interface CrudMemberService {
    void signUp(OAuth2Model oAuth2Model);
    boolean isMember(String snsId);
}
