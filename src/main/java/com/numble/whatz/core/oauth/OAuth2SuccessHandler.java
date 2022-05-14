package com.numble.whatz.core.oauth;

import com.numble.whatz.application.Role;
import com.numble.whatz.application.member.service.CrudMemberService;
import com.numble.whatz.core.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final CrudMemberService crudMemberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        OAuth2Model oAuth2Model = toOAuth2Model(principal);

        if(!crudMemberService.isMember(oAuth2Model.getId())) {
            crudMemberService.signUp(oAuth2Model);
            generateAccessToken(response, oAuth2Model, true);
        } else {
            generateAccessToken(response, oAuth2Model, false);
        }
    }

    private void generateAccessToken(HttpServletResponse response, OAuth2Model oAuth2Model, boolean condition) throws IOException {
        String token = jwtTokenProvider.createToken(
                new TokenDataModel(Long.parseLong(oAuth2Model.getId()), oAuth2Model.getEmail(), Role.MEMBER));
        Cookie cookie = new Cookie("access-token", token); // 쿠키 이름을 name으로 생성
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect("https://whatz.kro.kr/oauth/redirect?signup=" + condition);
    }

    private OAuth2Model toOAuth2Model(OAuth2User principal) {
        return new OAuth2Model(
                principal.getAttribute("id"),
                principal.getAttribute("email"),
                principal.getAttribute("nickName"),
                principal.getAttribute("thumbnailUrl"));
    }
}
