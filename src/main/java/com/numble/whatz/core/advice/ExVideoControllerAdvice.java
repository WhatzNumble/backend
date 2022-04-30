package com.numble.whatz.core.advice;

import com.numble.whatz.application.video.controller.VideoController;
import com.numble.whatz.core.exception.video.CustomVideoStoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackageClasses = VideoController.class)
public class ExVideoControllerAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(CustomVideoStoreException.class)
    public ErrorResult ffempegHandler(CustomVideoStoreException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("VideoDirectException", e.getVideoStoreExceptionMessage().getMessage());
    }




}
