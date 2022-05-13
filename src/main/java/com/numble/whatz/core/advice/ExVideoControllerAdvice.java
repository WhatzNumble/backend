package com.numble.whatz.core.advice;

import com.numble.whatz.application.video.controller.VideoViewController;
import com.numble.whatz.core.advice.dto.ErrorResult;
import com.numble.whatz.core.advice.dto.ThumbnailStoreExceptionMessage;
import com.numble.whatz.core.advice.dto.VideoStoreExceptionMessage;
import com.numble.whatz.core.exception.thumbnail.ThumbnailStoreException;
import com.numble.whatz.core.exception.video.VideoStoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackageClasses = VideoViewController.class)
public class ExVideoControllerAdvice {

    @ExceptionHandler(VideoStoreException.class)
    public ResponseEntity<ErrorResult> ffempegHandler(VideoStoreException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult response =
                new ErrorResult("VideoDirectException", e.getVideoStoreExceptionMessage().getMessage());

        if (e.getVideoStoreExceptionMessage().equals(VideoStoreExceptionMessage.MULTIPART_NULL))
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR) ;
    }

    @ExceptionHandler(ThumbnailStoreException.class)
    public ResponseEntity<ErrorResult> thumbnailException(ThumbnailStoreException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult response =
                new ErrorResult("ThumbnailStoreException", e.getThumbnailStoreExceptionMessage().getMessage());

        if (e.getThumbnailStoreExceptionMessage().equals(ThumbnailStoreExceptionMessage.MULTIPART_NULL))
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
