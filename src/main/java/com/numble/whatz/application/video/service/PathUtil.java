package com.numble.whatz.application.video.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PathUtil {

    @Value("${file.dir}")
    private static String fileDir;

    static public String getFullPathExecuteFile(String executeFilename) {
        return fileDir + "executeFile/" + executeFilename;
    }


    static public String getFullPathMp4File(String storeFilename) {
        return fileDir + "mp4File/" + storeFilename;
    }
}
