package com.numble.whatz.application.member.service;

import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public Member getMemberBySnsId(String snsId) {
        Optional<Member> findMember = memberRepository.findBySnsId(snsId);
        if (findMember.isEmpty()) throw new IllegalStateException("해당 유저가 존재하지 않습니다.");
        return findMember.get();
    }
}
