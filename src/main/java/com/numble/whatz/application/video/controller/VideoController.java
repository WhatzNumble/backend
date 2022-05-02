package com.numble.whatz.application.video.controller;

import com.numble.whatz.application.video.controller.dto.HomeDto;
import com.numble.whatz.application.video.controller.dto.VideoInfoDto;
import com.numble.whatz.application.video.controller.dto.*;
import com.numble.whatz.application.video.service.VideoService;
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

    @GetMapping("api/home")
    public HomeDto home(@PageableDefault(size = 5) Pageable pageable, Principal principal) {
        HomeDto homeDto = videoService.findAll(pageable, principal);
        return homeDto;
    }

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
        MyVideosDto myVideosDto = videoService.getMyVideos(pageable, principal);
        return myVideosDto;
    }

    @GetMapping("api/video/{id}")
    public VideoInfoDto myVideo(@PathVariable Long id) {
        VideoInfoDto videoInfoDto = videoService.getOneVideo(id);
        return videoInfoDto;
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

    //-----------------------------------

    @PostMapping("api/video/delete")
    public ResponseEntity deleteVideo(String id, Principal principal) {
        videoService.removeVideo(id, principal);
        return new ResponseEntity(HttpStatus.OK);
    }
}
