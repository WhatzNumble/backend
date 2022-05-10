package com.numble.whatz.application.video.controller;

import com.numble.whatz.application.video.service.VideoFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final VideoFileService videoService;
}
