package com.numble.whatz.application.video.service;

import com.numble.whatz.core.advice.dto.VideoStoreExceptionMessage;
import com.numble.whatz.core.exception.video.CustomVideoStoreException;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.numble.whatz.application.video.service.PathUtil.getFullPathExecuteFile;
import static com.numble.whatz.application.video.service.PathUtil.getFullPathMp4File;

@Component
public class FFmpegConverter {

    // brew 설치했을시 경로
    public void convert(String storeFilename, String executeFilename) throws CustomVideoStoreException {
        try {
            FFmpeg ffmpeg = new FFmpeg("/opt/homebrew/Cellar/ffmpeg/5.0.1/bin/ffmpeg");
            FFprobe ffprobe = new FFprobe("/opt/homebrew/Cellar/ffmpeg/5.0.1/bin/ffprobe");

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(getFullPathMp4File(storeFilename))
                    .overrideOutputFiles(true)
                    .addOutput(getFullPathExecuteFile(executeFilename))
                    .addExtraArgs("-profile:v", "baseline")
                    .addExtraArgs("-level", "3.0")
                    .addExtraArgs("-start_number", "0")
                    .addExtraArgs("-hls_time", "10")
                    .addExtraArgs("-hls_list_size", "0")
                    .addExtraArgs("-f", "hls")
                    .done();
//                .setTargetSize(250_000) // 250KB
//                .disableSubtitle() // 서브 타이틀 없음
//                .setAudioChannels(1) // Mono audio
//                .setAudioCodec("acc") // // acc codec 사용
//                .setAudioSampleRate(48_000) // 48KHz
//                .setVideoCodec("libx264") // 영상은 x264를 사용
//                .setVideoFrameRate(24, 1) // 매초 24프레임
//                .setVideoResolution(640, 480) // 640x480

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

            executor.createJob(builder).run();
        } catch (IOException e) {
            new CustomVideoStoreException(VideoStoreExceptionMessage.FFMPEG_EX, e);
        }
    }
}
