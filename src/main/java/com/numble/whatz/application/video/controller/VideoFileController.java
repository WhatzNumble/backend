package com.numble.whatz.application.video.controller;

import com.numble.whatz.application.video.controller.dto.DirectDto;
import com.numble.whatz.application.video.controller.dto.EmbedDto;
import com.numble.whatz.application.video.controller.dto.ModifyDirectDto;
import com.numble.whatz.application.video.controller.dto.ModifyEmbedDto;
import com.numble.whatz.application.video.service.VideoFileService;
import com.numble.whatz.core.exception.thumbnail.ThumbnailStoreException;
import com.numble.whatz.core.exception.video.VideoStoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
public class VideoFileController {

    private final VideoFileService videoService;
    
    @PostMapping("video/add/direct")
    public ResponseEntity uploadVideoDirect(DirectDto video, Principal principal) throws ThumbnailStoreException, VideoStoreException {
        videoService.saveDirect(video, principal);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("video/add/embed")
    public ResponseEntity uploadVideoEmbed(EmbedDto video, Principal principal) throws ThumbnailStoreException {
        videoService.saveEmbed(video, principal);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("video/modify/direct")
    public ResponseEntity modifyDirectVideo(ModifyDirectDto video, Principal principal) throws Exception {
        videoService.modifyDirect(video, principal);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("video/modify/embed")
    public ResponseEntity modifyEmbedVideo(ModifyEmbedDto video, Principal principal) throws IOException {
        videoService.modifyEmbed(video, principal);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("video/delete")
    public ResponseEntity deleteVideo(String id, Principal principal) {
        videoService.removeVideo(id, principal);
        return new ResponseEntity(HttpStatus.OK);
    }
}
