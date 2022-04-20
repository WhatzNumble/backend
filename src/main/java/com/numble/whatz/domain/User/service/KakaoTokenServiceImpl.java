package com.numble.whatz.domain.User.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KakaoTokenServiceImpl implements KakaoTokenService {

    @Override
    public void createKakaoUser(String token) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("resposeBody = {}", result);

            // json 파싱을 위한 map
            Map<String, Object> map = new HashMap<>();
            ObjectMapper obm = new ObjectMapper();
            map = obm.readValue(result, new TypeReference<Map<String, Object>>() {});

            long id = Long.parseLong(String.valueOf(map.get("id")));
            //kakao_account는 json 2중이므로, map으로 받아줌.
            Map<String, Object> kakaoAccount = (Map<String, Object>) map.get("kakao_account");
            boolean hasEmail = ((Boolean)kakaoAccount.get("has_email")).booleanValue();

            System.out.println("hasEmail = " + hasEmail);
            String email = "";

            if (hasEmail) {
                email = String.valueOf(kakaoAccount.get("email"));
            }

            log.info("id ={}", id);
            log.info("email = {}", email);

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getAccessToken(String code) {
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
