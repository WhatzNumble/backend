package com.numble.whatz.application.member.controller;

import com.numble.whatz.application.Role;
import com.numble.whatz.application.member.service.CrudMemberService;
import com.numble.whatz.core.jwt.JwtTokenProvider;
import com.numble.whatz.core.oauth.OAuth2Model;
import com.numble.whatz.core.oauth.TokenDataModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CrudMemberService crudMemberService;

    @PostMapping("/member/admin")
    public void login(HttpServletResponse response, @RequestBody AdminDto admin) throws IOException {
        AdminDto member = crudMemberService.getMemberByNickName(admin);

        OAuth2Model oAuth2Model = new OAuth2Model(member.getSnsId(), "admin", member.getNickName(), null);
        Cookie cookie = new Cookie("access-token", jwtTokenProvider.createToken(
                new TokenDataModel(Long.parseLong(oAuth2Model.getId()), oAuth2Model.getEmail(), Role.ADMIN))); // 쿠키 이름을 name으로 생성
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect("http://localhost:3000/oauth/redirect");
    }

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
