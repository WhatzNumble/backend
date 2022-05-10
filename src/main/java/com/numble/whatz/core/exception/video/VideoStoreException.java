package com.numble.whatz.core.exception.video;

import com.numble.whatz.core.advice.dto.VideoStoreExceptionMessage;
import lombok.Getter;

import java.io.IOException;

@Getter
public class VideoStoreException extends IOException {

    private Exception e;
    private VideoStoreExceptionMessage videoStoreExceptionMessage;

    public VideoStoreException(VideoStoreExceptionMessage videoStoreExceptionMessage, Exception e) {
        this.videoStoreExceptionMessage = videoStoreExceptionMessage;
        this.e = e;
    }

}
