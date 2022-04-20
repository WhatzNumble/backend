package com.numble.whatz.domain.video;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
public class VideoStore {

    @Value("${file.dir}")
    private String fileDir;

    public String storeVideo(MultipartFile multipartFile) throws Exception {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        // UUID로 바꿔서 파일을 저장한다. 테스트용에서는 파일이름 그대로 저장하고 있음
//        String storeFilename = createStoreFilename(originalFilename);
        multipartFile.transferTo(new File(getFullPath(originalFilename)));
        return originalFilename;
    }

    public String getFullPath(String storeFilename) {
        return fileDir + storeFilename;
    }

    private String createStoreFilename(String originalFilename) {
        String ext = extractedExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractedExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
