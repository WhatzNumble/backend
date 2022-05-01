package com.numble.whatz.application.video.controller;

import com.numble.whatz.application.home.controller.dto.VideoInfoDto;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.application.video.controller.dto.*;
import com.numble.whatz.application.video.service.VideoService;
import com.numble.whatz.application.video.service.VideoServiceImpl;
import com.numble.whatz.application.video.service.VideoStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping("api/video/add/direct")
    public ResponseEntity uploadVideoDirect(DirectDto video, Principal principal) throws Exception {
        videoService.saveDirect(video, principal);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("api/video/add/embed")
    public ResponseEntity uploadVideoEmbed(EmbedDto video, Principal principal) {
        videoService.saveEmbed(video, principal);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("api/video")
    public MyVideosDto myVideos(@PageableDefault(size = 5)Pageable pageable, Principal principal) {

        // ======== 서비스가 생기면 여기는 지우는 부분 ========


        List<MyVideoDto> videos = new ArrayList<>();

        MyVideoDto video1 = new MyVideoDto(1L, "VideoThumbnail1");
        MyVideoDto video2 = new MyVideoDto(2L, "VideoThumbnail1");
        MyVideoDto video3 = new MyVideoDto(3L, "VideoThumbnail1");
        MyVideoDto video4 = new MyVideoDto(4L, "VideoThumbnail1");

        videos.add(video1);
        videos.add(video2);
        videos.add(video3);
        videos.add(video4);

        MyVideosDto myVideosDto = new MyVideosDto(videos);
        // ======== 서비스가 생기면 여기는 지우는 부분 ========

        return myVideosDto;
    }

    @GetMapping("api/video/{id}")
    public VideoInfoDto myVideo(@PathVariable Long id) {

        log.info("id={}", id);

        // ======== 서비스가 생기면 여기는 지우는 부분 ========
        if (id.equals(1L)) return new VideoInfoDto("user1", "profile1", 5, "title1", "content1",
                LocalDateTime.now(), 20L, "38b571b8-c9e3-4b8e-b9a7-7f48dfd7dd5b.m3u8", null);
        if (id.equals(2L)) return new VideoInfoDto("user2", "profile2", 10, "title2", "content2",
                LocalDateTime.now(), 30L, "93210b1d-7c54-4208-84a3-c4bc97b02c64.m3u8", null);
        if (id.equals(3L)) return new VideoInfoDto("user1", "profile1", 15, "title3", "content3",
                LocalDateTime.now(), 40L, "5dc04aa2-6297-4cce-aab6-a43ab761da65.m3u8", null);
        if (id.equals(4L)) return new VideoInfoDto("user3", "profile3", 5, "title4", "content4",
                LocalDateTime.now(), 50L, null, "https://youtube.com/shorts/E4BR0sAM3-8?feature=share");
        // ======== 서비스가 생기면 여기는 지우는 부분 ========

        return null;
    }

    //-------------No Test yet--------------

    @PostMapping("api/video/modify/direct")
    public String modifyDirectVideo(ModifyDirectDto video) {

        return "success";
    }

    @PostMapping("api/video/modify/embed")
    public ResponseEntity modifyEmbedVideo(EmbedDto video) {
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("api/video/delete")
    public ResponseEntity deleteVideo(String id, Principal principal) {
        videoService.removeVideo(id, principal);
        return new ResponseEntity(HttpStatus.OK);
    }

    //-----------------------------------

}
