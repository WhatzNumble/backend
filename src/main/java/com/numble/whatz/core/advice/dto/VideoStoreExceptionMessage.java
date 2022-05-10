package com.numble.whatz.core.advice.dto;

import lombok.Getter;

public enum VideoStoreExceptionMessage {

    MULTIPART_EX("파일 저장에 실패하였습니다."),
    S3_UPLOAD_EX ("s3 업로드에 실패하였습니다."),
    FFMPEG_EX (".mp4 -> .m3u8 변환에 실패하였습니다."),
    LOCAL_DELETE_EX("로컬 파일 삭제를 실패하였습니다.")
    ;


    private String message;

    VideoStoreExceptionMessage(String message) { this.message = message; }

    public String getMessage() {
        return message;
    }

}
