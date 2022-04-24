package com.numble.whatz.web.user.controller;

import com.numble.whatz.domain.video.VideoStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class VideoController {

    private final VideoStore videoStore;

    @PostMapping("/video/upload")
    public String uploadVideo(MultipartFile video) throws Exception {
        String fileDir = videoStore.storeVideo(video);
        System.out.println("fileDir = " + fileDir);
        return fileDir;
    }

    /**
     * 파일을 직접 올렸을 경우, 이쪽 경로를 통해 비디오를 보여주게 된다.
     * 프론트 쪽에서 어떻게 작업을 했는지 아직 맞추지 않는 부분이라 추후 변경할것
     * @param headers
     * @return
     * @throws IOException
     */
    @GetMapping("/videos/{filename}")
    public ResponseEntity<ResourceRegion> showVideo(
            @PathVariable String filename,
            @RequestHeader HttpHeaders headers) throws IOException {
        log.info("showVideo controller");

        // 이 경로를 나중에 DB에서 가져오게 할 것.
//        videoplayback.mp4
        String videoDB = "file:" + videoStore.getFullPath(filename);

        UrlResource video = new UrlResource(videoDB);
        ResourceRegion resourceRegion;

        final long chunkSize = 1000000L;

        long contentLength = video.contentLength();

        Optional<HttpRange> optional = headers.getRange().stream().findFirst();

        HttpRange httpRange;

        if (optional.isPresent()) {
            httpRange = optional.get();
            long start = httpRange.getRangeStart(contentLength);
            long end = httpRange.getRangeEnd(contentLength);
            long rangeLength = Long.min(chunkSize, end - start + 1);
            resourceRegion = new ResourceRegion(video, start, rangeLength);
        } else {
            long rangeLength = Long.min(chunkSize, contentLength);
            resourceRegion = new ResourceRegion(video, 0, rangeLength);
        }
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(video)
                        .orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resourceRegion);
    }
}
