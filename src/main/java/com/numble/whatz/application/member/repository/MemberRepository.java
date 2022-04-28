package com.numble.whatz.application.member.repository;

import com.numble.whatz.application.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySnsId(String snsId);
}
