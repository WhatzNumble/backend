package com.numble.whatz.application.video.service;

import com.numble.whatz.application.video.controller.dto.DirectDto;
import com.numble.whatz.application.video.controller.dto.EmbedDto;

import java.io.IOException;
import java.security.Principal;

public interface VideoService {
    void saveDirect(DirectDto video, Principal principal) throws IOException;
    void saveEmbed(EmbedDto video, Principal principal);
    void removeVideo(String id, Principal principal);
}
