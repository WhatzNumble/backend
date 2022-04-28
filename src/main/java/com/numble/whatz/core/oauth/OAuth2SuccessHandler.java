package com.numble.whatz.core.oauth;

import com.numble.whatz.application.Role;
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

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        OAuth2Model oAuth2Model = toOAuth2Model(principal);

        //TODO
        //회원이면 accessToken 반환
        //아니면 회원가입 accessToken 반환

        String token = jwtTokenProvider.createToken(new TokenDataModel(Long.parseLong(oAuth2Model.getId()), oAuth2Model.getEmail(), Role.ROLE_MEMBER));
        Cookie cookie = new Cookie("access-token", token); // 쿠키 이름을 name으로 생성
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect("/oauth/redirect");
    }

    private OAuth2Model toOAuth2Model(OAuth2User principal) {
        return new OAuth2Model(
                principal.getAttribute("id"),
                principal.getAttribute("email"),
                (Sns) principal.getAttribute("type"));
    }
}
