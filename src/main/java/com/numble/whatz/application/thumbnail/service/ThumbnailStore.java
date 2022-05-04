package com.numble.whatz.application.thumbnail.service;


import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ThumbnailStore {
    ThumbnailStoreDto storeThumbnail(MultipartFile videoThumbnail) throws IOException;
    ThumbnailStoreDto modifyThumbnail(MultipartFile videoThumbnail, Thumbnail thumbnail) throws IOException;
    void removeThumbnail(Thumbnail thumbnail) throws IOException;
}
