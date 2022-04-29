package com.numble.whatz.application.video.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
public class VideoStore {

    /**
     * application.properties 에서 file.dir , 파일 저장 경로
     */
    @Value("${file.dir}")
    private String fileDir;

    /**
     * UUID로 바꿔서 파일을 저장한다. 테스트용에서는 파일이름 그대로 저장하고 있음
     * @param multipartFile
     * @return storeFilename
     * @throws Exception
     */
    public String storeVideo(MultipartFile multipartFile) throws Exception {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String storeFilename = createStoreFilename(uuid, originalFilename);
        String executeFilename = createStoreExecuteFilename(uuid);

        FFmpeg ffmpeg = new FFmpeg(fileDir + "mp4File/");
        FFprobe fFprobe = new FFprobe(fileDir + "executeFile/");

        multipartFile.transferTo(new File(getFullPathMp4File(storeFilename)));

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(storeFilename)
                        .overrideOutputFiles(true)
                                .addOutput(executeFilename)
                                        .setFormat("m3u8")
//                .setTargetSize(250_000) // 250KB
//                .disableSubtitle() // 서브 타이틀 없음
//                .setAudioChannels(1) // Mono audio
//                .setAudioCodec("acc") // // acc codec 사용
//                .setAudioSampleRate(48_000) // 48KHz
//                .setVideoCodec("libx264") // 영상은 x264를 사용
//                .setVideoFrameRate(24, 1) // 매초 24프레임
//                .setVideoResolution(640, 480) // 640x480
                                                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, fFprobe);

        executor.createJob(builder).run();

//        executor.createTwoPassJob(builder).run(); 느리지만 더 좋은 품질


        return originalFilename; // 원래 return storeFilename;
    }



    public String getFullPathMp4File(String storeFilename) {
        return fileDir + "mp4File/" + storeFilename;
    }

    private String createStoreFilename(String uuid, String originalFilename) {
        String ext = extractedExt(originalFilename);
        return uuid + "." + ext;
    }

    private String createStoreExecuteFilename(String uuid) {
        return uuid + ".m3u8";
    }

    private String extractedExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
