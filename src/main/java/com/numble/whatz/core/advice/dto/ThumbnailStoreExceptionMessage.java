package com.numble.whatz.core.advice.dto;

public enum ThumbnailStoreExceptionMessage {

    IMAGE_CONVERT_EXCEPTION("이미지 변환을 실패하였습니다."),
    MULTIPART_NULL("파일이 비어있습니다.")
    ;

    private String message;

    ThumbnailStoreExceptionMessage(String message) { this.message = message; }

    public String getMessage() {
        return message;
    }
}
