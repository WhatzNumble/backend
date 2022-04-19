package com.numble.whatz.web.user.controller;

import com.numble.whatz.domain.User.service.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final HttpSession session;

    @GetMapping("login-success")
    public String kakaoLogin() {
        System.out.println("success");
        return "ok";
    }

    @GetMapping("/login/oauth2/kakao")
    public String kakaoTest(Model model) {
        System.out.println("s");

        SessionUser user = (SessionUser) session.getAttribute("user");
        if (user != null) {
            log.info("user.getEmail={}", user.getEmail());
            log.info("user.getName={}", user.getName());
            log.info("user.getPicture={}", user.getPicture());
        }


        return "ok";
    }
}
