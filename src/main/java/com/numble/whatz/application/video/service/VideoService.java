package com.numble.whatz.application.video.service;

import com.numble.whatz.application.video.controller.dto.*;
import com.numble.whatz.application.video.domain.Videos;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.security.Principal;

public interface VideoService {
    void saveDirect(DirectDto video, Principal principal) throws IOException;
    void saveEmbed(EmbedDto video, Principal principal);
    void removeVideo(String id, Principal principal);
    HomeDto findAll(Pageable pageable, Principal principal);
    MyVideosDto getMyVideos(Pageable pageable, Principal principal);
    VideoInfoDto getOneVideo(Long id);
}
