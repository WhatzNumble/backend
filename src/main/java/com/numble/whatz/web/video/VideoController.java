package com.numble.whatz.web.video;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class VideoController {

    @PostMapping("video/add/direct")
    public String uploadVideoDirect(MultipartFile video) throws Exception {

        // 파일 저장

        return "success";
    }

    @PostMapping("video/add/embed")
    public String uploadVideoEmbed(String link) throws Exception {

        // 링크 저장

        return "success";
    }

    @GetMapping("video")
    public MyVideosDto myVideos(@PageableDefault(size = 5)Pageable pageable) {

        // ======== 서비스가 생기면 여기는 지우는 부분 ========
        MyVideoDto video1 = new MyVideoDto(1L, "비디오 썸네일1", 123);
        MyVideoDto video2 = new MyVideoDto(2L, "비디오 썸네일2", 223);

        List<MyVideoDto> videos = new ArrayList<>();

        videos.add(video1);
        videos.add(video2);

        MyVideosDto myVideosDto = new MyVideosDto(videos);
        // ======== 서비스가 생기면 여기는 지우는 부분 ========

        return myVideosDto;
    }
}
