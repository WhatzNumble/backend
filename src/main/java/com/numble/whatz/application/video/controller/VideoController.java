package com.numble.whatz.application.video.controller;

import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import com.numble.whatz.application.thumbnail.service.ThumbnailStore;
import com.numble.whatz.application.thumbnail.service.ThumbnailStoreDto;
import com.numble.whatz.application.video.controller.dto.HomeDto;
import com.numble.whatz.application.video.controller.dto.VideoInfoDto;
import com.numble.whatz.application.video.controller.dto.*;
import com.numble.whatz.application.video.service.VideoService;
import com.numble.whatz.application.video.service.VideoStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

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
    public ResponseEntity uploadVideoEmbed(EmbedDto video, Principal principal) throws IOException {
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

    @PostMapping("api/video/modify/direct")
    public ResponseEntity modifyDirectVideo(ModifyDirectDto video, Principal principal) throws Exception {
        videoService.modifyDirect(video, principal);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("api/video/modify/embed")
    public ResponseEntity modifyEmbedVideo(ModifyEmbedDto video, Principal principal) throws IOException {
        videoService.modifyEmbed(video, principal);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("api/video/delete")
    public ResponseEntity deleteVideo(String id, Principal principal) throws IOException {
        videoService.removeVideo(id, principal);
        return new ResponseEntity(HttpStatus.OK);
    }

    private final VideoStore videoStore;
    private final ThumbnailStore thumbnailStore;

    @PostMapping("api/testVideo")
    public String test(MultipartFile image) throws Exception {
//        ThumbnailStoreDto save = thumbnailStore.storeThumbnail(image);
//        System.out.println("save.getCutName() = " + save.getCutName());
//        System.out.println("save.getExecuteName() = " + save.getExecuteName());
//        videoStore.modifyVideo(video.getFile(), "074d590f-f354-4e08-9725-6f5d9bb67451/074d590f-f354-4e08-9725-6f5d9bb67451.m3u8");
        Thumbnail thumbnail = Thumbnail.builder()
                .cutFile("/WhatzDev/thumbnail/9acacd1a-26fa-4422-90b5-50bf76c72d10/9acacd1a-26fa-4422-90b5-50bf76c72d10_CUT.jpeg")
                .executeFile("/WhatzDev/thumbnail/9acacd1a-26fa-4422-90b5-50bf76c72d10/9acacd1a-26fa-4422-90b5-50bf76c72d10.jpeg")
                .build();
        thumbnailStore.removeThumbnail(thumbnail);
        return "ok";
    }

}
