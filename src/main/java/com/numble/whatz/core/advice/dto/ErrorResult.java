package com.numble.whatz.core.advice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.http.HttpStatus;

@Getter
@ToString
public class ErrorResult {
    private String errorCode;
    private String errorMessage;

    @Builder
    public ErrorResult(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
