package com.numble.whatz.application.member.service;

import com.numble.whatz.application.member.domain.Member;

public interface MemberService {
    Member getMemberBySnsId(String snsId);
}
