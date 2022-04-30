package com.numble.whatz.application.video.controller;

import com.numble.whatz.application.video.controller.dto.*;
import com.numble.whatz.application.video.service.VideoService;
import com.numble.whatz.application.video.service.VideoStore;
import com.numble.whatz.application.video.service.VideoStoreImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class VideoController {

    private final VideoStore videoStore;
    private final VideoService videoService;

    @PostMapping("api/video/add/direct")
    public ResponseEntity uploadVideoDirect(DirectDto video) throws Exception {

        String executeFileName = videoStore.storeVideo(video.getFile());



        // 파일 저장

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("api/video/add/embed")
    public ResponseEntity uploadVideoEmbed(EmbedDto video) {

        System.out.println("video.getVideoThumbnail() = " + video.getVideoThumbnail());
        System.out.println("video.getContent() = " + video.getContent());
        System.out.println("video.getTitle() = " + video.getTitle());
        System.out.println("video.getFile() = " + video.getLink());
        // 링크 저장

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("api/video")
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

    @GetMapping("api/video/{id}")
    public VideoOneDto myVideo(@PathVariable Long id) {

        log.info("id={}", id);

        // ======== 서비스가 생기면 여기는 지우는 부분 ========
        VideoOneDto videoOneDto = new VideoOneDto(10, "title1", LocalDateTime.now(),
                300L, "Thumbnail1", "linkOrPath1", "content");
        // ======== 서비스가 생기면 여기는 지우는 부분 ========

        return videoOneDto;
    }

    //-------------No Test yet--------------

    @PostMapping("api/video/modify")
    public String modifyVideo() {
        return "success";
    }

    //-----------------------------------

    @PostMapping("api/video/delete")
    public String deleteVideo(String id) {
        return "success";
    }

    @GetMapping("api/favorite")
    public List<FavoritesDto> favoriteVideo(@PageableDefault(size = 5)Pageable pageable) {
        // ======== 서비스가 생기면 여기는 지우는 부분 ========
        FavoritesDto favoritesDto1 = new FavoritesDto(1L, "videoThumbnail1", 30L);
        FavoritesDto favoritesDto2 = new FavoritesDto(1L, "videoThumbnail1", 30L);

        List<FavoritesDto> favoritesDtos = new ArrayList<>();
        favoritesDtos.add(favoritesDto1);
        favoritesDtos.add(favoritesDto2);
        // ======== 서비스가 생기면 여기는 지우는 부분 ========

        return favoritesDtos;
    }


}
