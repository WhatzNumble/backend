package com.numble.whatz.application.member.controller;

import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.application.member.service.CrudMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final CrudMemberService crudMemberService;

    @GetMapping("/profile")
    public ResponseEntity<MemberDto> getMemberInfo(Principal principal) {
        MemberDto memberDto = crudMemberService.getProfile(principal.getName());

        return ResponseEntity.ok(memberDto);
    }
}
