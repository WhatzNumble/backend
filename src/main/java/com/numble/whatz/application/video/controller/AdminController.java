package com.numble.whatz.application.video.controller;

import com.numble.whatz.application.video.controller.dto.UserVideosDto;
import com.numble.whatz.application.video.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("admin/user/{id}")
    public UserVideosDto userVideoList(@PathVariable Long id) {
        UserVideosDto userVideoDto = adminService.getUserVideos(id);
        return userVideoDto;
    }
}
