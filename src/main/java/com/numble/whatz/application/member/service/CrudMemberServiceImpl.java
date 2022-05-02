package com.numble.whatz.application.member.service;

import com.numble.whatz.application.member.controller.MemberDto;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.core.oauth.OAuth2Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CrudMemberServiceImpl implements CrudMemberService{

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void signUp(OAuth2Model oAuth2Model) {
        memberRepository.save(oAuth2Model.toEntity());

    }

    @Override
    public boolean isMember(String snsId) {
        Optional<Member> member = memberRepository.findBySnsId(snsId);
        return member.isPresent();
    }
}
