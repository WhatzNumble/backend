package com.numble.whatz.web.home;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class HomeController {

    @GetMapping("/home")
    public List<HomeDto> home(@PageableDefault(size = 5) Pageable pageable) {

        // ======== 서비스가 생기면 여기는 지우는 부분 ========
        HomeDto homeDto1 = new HomeDto("user1", "사진경로1", false,
                3, "title1", LocalDateTime.now(), 10L, "영상썸네일1");
        HomeDto homeDto2 = new HomeDto("user2", "사진경로2", false,
                4, "title2", LocalDateTime.now(), 20L, "영상썸네일2");
        List<HomeDto> homeDtos = new ArrayList<>();
        homeDtos.add(homeDto1);
        homeDtos.add(homeDto2);
        // ======== 서비스가 생기면 여기는 지우는 부분 ========

        return homeDtos;
    }
}
