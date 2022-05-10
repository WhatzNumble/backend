package com.numble.whatz.core.advice;

import com.numble.whatz.application.video.controller.VideoViewController;
import com.numble.whatz.core.advice.dto.ErrorResult;
import com.numble.whatz.core.exception.thumbnail.ThumbnailStoreException;
import com.numble.whatz.core.exception.video.VideoStoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackageClasses = VideoViewController.class)

public class ExVideoControllerAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(VideoStoreException.class)
    public ErrorResult ffempegHandler(VideoStoreException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("VideoDirectException", e.getVideoStoreExceptionMessage().getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ThumbnailStoreException.class)
    public ErrorResult thumbnailException(ThumbnailStoreException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("ThumbnailStoreException", e.getThumbnailStoreExceptionMessage().getMessage());
    }

}
