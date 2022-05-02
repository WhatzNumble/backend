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
public class CrudMemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(OAuth2Model oAuth2Model) {
        memberRepository.save(oAuth2Model.toEntity());

    }

    public boolean isMember(String snsId) {
        Optional<Member> member = memberRepository.findBySnsId(snsId);
        return member.isPresent();
    }

    @Transactional
    public void delete(String snsId) {
        memberRepository.deleteBySnsId(snsId);
    }

    public MemberDto getProfile(String snsId) {
        Member member = memberRepository.findBySnsId(snsId)
                .orElseThrow(RuntimeException::new);

        return new MemberDto(member.getEmail(), member.getNickName(), member.getThumbnailUrl());
    }
}
