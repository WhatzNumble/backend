package com.numble.whatz.application.member.controller;

import com.numble.whatz.application.member.service.CrudMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final CrudMemberService crudMemberService;

    @PostMapping("/member/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("access-token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/member/delete")
    public ResponseEntity delete(Principal principal) {
        crudMemberService.delete(principal.getName());

        return ResponseEntity.noContent().build();
    }
}
