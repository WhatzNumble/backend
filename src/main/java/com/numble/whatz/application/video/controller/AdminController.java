package com.numble.whatz.application.video.controller;

import com.numble.whatz.application.video.controller.dto.MainContentsDto;
import com.numble.whatz.application.video.controller.dto.UserVideosDto;
import com.numble.whatz.application.video.controller.dto.VideoDetailDto;
import com.numble.whatz.application.video.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("admin/user/{id}")
    public UserVideosDto userVideoList(@PathVariable Long id, Pageable pageable) {
        UserVideosDto userVideoDto = adminService.getUserVideos(id, pageable);
        return userVideoDto;
    }

    @GetMapping("admin/main")
    public MainContentsDto mainContent(@PageableDefault(size = 10) Pageable pageable) {
        MainContentsDto mainContentDto = adminService.getMainContent(pageable);
        return mainContentDto;
    }

    @GetMapping("admin/main/{videoId}")
    public MainContentDetailDto mainContentDetail(@PathVariable Long videoId) {
        MainContentDetailDto mainContentDetailDto = adminService.getDetail(videoId);
        return mainContentDetailDto;
    }

    @PostMapping("admin/main/delete/{videoId}")
    public ResponseEntity deleteVideo(@PathVariable Long videoId) {
        adminService.removeVideo(videoId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("admin/main/modify/{videoId}")
    public ResponseEntity modifyVideoId(@PathVariable Long videoId, String showId) {
        adminService.modifyVideoId(videoId, showId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
