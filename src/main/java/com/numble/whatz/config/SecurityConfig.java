package com.numble.whatz.config;

import com.numble.whatz.domain.User.service.CustomOAuth2UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().mvcMatchers("/login/**"); // 이곳에 있는 파일들은 시큐리티 적용 무시
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .anyRequest()
                .authenticated()
            .and()
                .logout()
                .logoutSuccessUrl("/") // 로그아웃 이후
            .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService); // 로그인 성공하면 customOAuth2UserService에서 처리를 하겠다.
    }
}
