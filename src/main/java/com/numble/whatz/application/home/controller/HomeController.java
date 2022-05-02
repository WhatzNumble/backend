package com.numble.whatz.application.home.controller;

import com.numble.whatz.application.home.controller.dto.HomeDto;
import com.numble.whatz.application.home.controller.dto.VideoInfoDto;
import com.numble.whatz.application.video.controller.dto.MyVideoDto;
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

    @GetMapping("api/home")
    public HomeDto home(@PageableDefault(size = 5) Pageable pageable) {

        // ======== 서비스가 생기면 여기는 지우는 부분 ========
        List<Long> likeList = new ArrayList<>();
        likeList.add(1L);
        likeList.add(5L);

        VideoInfoDto homeDto1 = new VideoInfoDto("user1", "profile1", 5, "title1", "content1",
                LocalDateTime.now(), 20L, "38b571b8-c9e3-4b8e-b9a7-7f48dfd7dd5b.m3u8", null);
        VideoInfoDto homeDto2 = new VideoInfoDto("user2", "profile2", 10, "title2", "content2",
                LocalDateTime.now(), 30L, "93210b1d-7c54-4208-84a3-c4bc97b02c64.m3u8", null);
        VideoInfoDto homeDto3 = new VideoInfoDto("user1", "profile1", 15, "title3", "content3",
                LocalDateTime.now(), 40L, "5dc04aa2-6297-4cce-aab6-a43ab761da65.m3u8", null);
        VideoInfoDto homeDto4 = new VideoInfoDto("user3", "profile3", 5, "title4", "content4",
                LocalDateTime.now(), 50L, null, "https://youtube.com/shorts/E4BR0sAM3-8?feature=share");

        List<VideoInfoDto> videoInfoDtos = new ArrayList<>();
        videoInfoDtos.add(homeDto1);
        videoInfoDtos.add(homeDto2);
        videoInfoDtos.add(homeDto3);
        videoInfoDtos.add(homeDto4);

        HomeDto homeDto = new HomeDto(videoInfoDtos, likeList);
        // ======== 서비스가 생기면 여기는 지우는 부분 ========

        return homeDto;
    }
}
