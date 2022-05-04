package com.numble.whatz.application.video.service;

import com.numble.whatz.application.video.controller.dto.*;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.security.Principal;

public interface VideoService {
    void saveDirect(DirectDto video, Principal principal) throws IOException;
    void saveEmbed(EmbedDto video, Principal principal) throws IOException;
    void removeVideo(String id, Principal principal) throws IOException;
    HomeDto findAll(Pageable pageable, Principal principal);
    MyVideosDto getMyVideos(Pageable pageable, Principal principal);
    VideoInfoDto getOneVideo(Long id);
    void modifyDirect(ModifyDirectDto video, Principal principal) throws IOException;
    void modifyEmbed(ModifyEmbedDto video, Principal principal) throws IOException;
}
