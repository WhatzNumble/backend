package com.numble.whatz.core.exception.thumbnail;

import com.numble.whatz.core.advice.dto.ThumbnailStoreExceptionMessage;
import com.numble.whatz.core.advice.dto.VideoStoreExceptionMessage;
import lombok.Getter;

import java.io.IOException;

@Getter
public class ThumbnailStoreException extends IOException {
    private Exception e;
    private ThumbnailStoreExceptionMessage thumbnailStoreExceptionMessage;

    public ThumbnailStoreException(ThumbnailStoreExceptionMessage thumbnailStoreExceptionMessage, Exception e) {
        this.thumbnailStoreExceptionMessage = thumbnailStoreExceptionMessage;
        this.e = e;
    }
}
