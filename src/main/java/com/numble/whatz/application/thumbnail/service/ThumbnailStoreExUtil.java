package com.numble.whatz.application.thumbnail.service;

import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import com.numble.whatz.core.advice.dto.ThumbnailStoreExceptionMessage;
import com.numble.whatz.core.exception.thumbnail.ThumbnailStoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class ThumbnailStoreExUtil {

    private final ThumbnailStore thumbnailStore;

    public ThumbnailStoreDto storeThumbnail(MultipartFile videoThumbnail) throws ThumbnailStoreException {
        try {
            return thumbnailStore.storeThumbnail(videoThumbnail);
        } catch (IOException e) {
            throw new ThumbnailStoreException(ThumbnailStoreExceptionMessage.IMAGE_CONVERT_EXCEPTION, e);
        }
    }

    public ThumbnailStoreDto modifyThumbnail(MultipartFile videoThumbnail, Thumbnail thumbnail) throws ThumbnailStoreException {
        try {
            return thumbnailStore.modifyThumbnail(videoThumbnail, thumbnail);
        } catch (IOException e) {
            throw new ThumbnailStoreException(ThumbnailStoreExceptionMessage.IMAGE_CONVERT_EXCEPTION, e);
        }
    }

    public void removeThumbnail(Thumbnail thumbnail) {
        thumbnailStore.removeThumbnail(thumbnail);
    }
}
