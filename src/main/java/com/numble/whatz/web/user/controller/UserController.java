package com.numble.whatz.web.user.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.numble.whatz.domain.User.service.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
    public String kakaoTest(@RequestParam(value = "code", required = false) String code, Model model) {

        log.info("code={}", code);

        String accessToken = getAccessToken(code);

        return "ok";
    }

    private String getAccessToken(String code) {
        String accessToken = "";
        String refreshToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=beda0c7896eef7e0cf3509bad14ce7dd");
            sb.append("&client_secret=TNZc4GoNexGRgomGBe4rLMY6l9iLcPgJ");
            sb.append("&redirect_uri=http://localhost:8080/login/oauth2/kakao");
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = connection.getResponseCode(); //200 success
            log.info("responseCode : {}", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response bode : {}", result);

            Map<String, String> map = new HashMap<>();
            ObjectMapper obm = new ObjectMapper();
            map = obm.readValue(result, new TypeReference<Map<String, String>>() {});
            accessToken = map.get("access_token");
            refreshToken = map.get("refresh_token");

            log.info("access_token : {}", accessToken);
            log.info("refresh_token : {}", refreshToken);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }
}
