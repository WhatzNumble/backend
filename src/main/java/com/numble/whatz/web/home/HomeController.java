package com.numble.whatz.web.home;

import lombok.extern.slf4j.Slf4j;
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
    public HomeDto home(@PageableDefault(size = 5) Pageable pageable) {

        // ======== 서비스가 생기면 여기는 지우는 부분 ========
        List<Long> likeList = new ArrayList<>();
        likeList.add(1L);
        likeList.add(5L);

        VideoInfoDto homeDto1 = new VideoInfoDto("user1", "사진경로1",
                3, "title1", LocalDateTime.now(), 10L, "영상썸네일1");
        VideoInfoDto homeDto2 = new VideoInfoDto("user2", "사진경로2",
                4, "title2", LocalDateTime.now(), 20L, "영상썸네일2");
        List<VideoInfoDto> videoInfoDtos = new ArrayList<>();
        videoInfoDtos.add(homeDto1);
        videoInfoDtos.add(homeDto2);

        HomeDto homeDto = new HomeDto(videoInfoDtos, likeList);
        // ======== 서비스가 생기면 여기는 지우는 부분 ========

        return homeDto;
    }
}
