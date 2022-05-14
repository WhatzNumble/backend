package com.numble.whatz.config;

import com.numble.whatz.core.jwt.JwtAuthenticationFilter;
import com.numble.whatz.core.oauth.CustomOAuth2UserService;
import com.numble.whatz.core.oauth.OAuth2SuccessHandler;
import com.numble.whatz.core.exception.CustomAccessDeniedHandler;
import com.numble.whatz.core.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.numble.whatz.application.Role.ADMIN;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
            .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers("/api/member/admin",
                        "/api/member/logout",
                        "/api/health-check",
                        "/api/member/admin")
                .permitAll()
                .antMatchers("/api/admin/**").hasRole(ADMIN.toString())
                .antMatchers("/api/docs/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
            .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
            .and()
                .successHandler(oAuth2SuccessHandler);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/h2-console/*"
        );
    }
}
