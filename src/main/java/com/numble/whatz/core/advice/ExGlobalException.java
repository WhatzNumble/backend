package com.numble.whatz.core.advice;

import com.numble.whatz.application.like.controller.FavoriteController;
import com.numble.whatz.application.member.controller.MemberController;
import com.numble.whatz.application.video.controller.VideoFileController;
import com.numble.whatz.application.video.controller.VideoViewController;
import com.numble.whatz.core.advice.dto.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackageClasses =
        {VideoViewController.class, VideoFileController.class, FavoriteController.class, MemberController.class})
public class ExGlobalException {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResult notFoundHandler(IllegalStateException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("IllegalStateException", e.getMessage());
    }
}
