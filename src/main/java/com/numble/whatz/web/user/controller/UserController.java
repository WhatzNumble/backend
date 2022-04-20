package com.numble.whatz.web.user.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.whatz.domain.User.service.KakaoTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final HttpSession session;
    private final KakaoTokenService kakaoTokenService;

    @GetMapping("login-success")
    public String kakaoLogin() {
        System.out.println("success");
        return "ok";
    }

    @GetMapping("/login/oauth2/kakao")
    public String kakaoTest(@RequestParam(value = "code", required = false) String code, Model model) {

        log.info("code={}", code);

        String accessToken = kakaoTokenService.getAccessToken(code);
        kakaoTokenService.createKakaoUser(accessToken);

        return "ok";
    }
}
