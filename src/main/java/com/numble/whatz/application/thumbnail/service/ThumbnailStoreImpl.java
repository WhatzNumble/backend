package com.numble.whatz.application.thumbnail.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import com.numble.whatz.application.video.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ThumbnailStoreImpl implements ThumbnailStore{

    private final S3Uploader s3Uploader;

    private String s3dir = "/WhatzDev/thumbnail/";

    @Override
    public ThumbnailStoreDto storeThumbnail(MultipartFile videoThumbnail) throws IOException {
        BufferedImage image = ImageIO.read(videoThumbnail.getInputStream());

        image.getWidth(); // null 체크 if문을 통해 예외처리 해야됨.

        // 16:9 -> 320:180

        String uuid = UUID.randomUUID().toString();
        String storeFilename = createStoreFilename(uuid, videoThumbnail.getOriginalFilename()); //UUID.png
        String cutFilename = getCutFilename(uuid, videoThumbnail.getOriginalFilename()); //UUID_CUT.png

        MultipartFile resizedImage = getResizedImage(image, 320, storeFilename);
        MultipartFile cutImage = getCutImage(image, 180, 320, cutFilename);

        s3Uploader.uploadThumbnail(resizedImage, s3dir + uuid, videoThumbnail.getContentType());
        s3Uploader.uploadThumbnail(cutImage, s3dir + uuid, videoThumbnail.getContentType());

        return new ThumbnailStoreDto(s3dir + uuid + "/" + storeFilename
                , s3dir + uuid + "/" +  cutFilename);
    }

    @Override
    public ThumbnailStoreDto modifyThumbnail(MultipartFile videoThumbnail, Thumbnail thumbnail) throws IOException {
        removeThumbnail(thumbnail);
        return storeThumbnail(videoThumbnail);
    }

    @Override
    public void removeThumbnail(Thumbnail thumbnail) {
        String pullPath = thumbnail.getExecuteFile();
        String folderName = pullPath.substring(0, pullPath.lastIndexOf("/"));
        s3Uploader.deleteFolderS3(folderName);
    }

    private String getCutFilename(String uuid, String originalFilename) {
        return uuid + "_CUT." +extractedExt(originalFilename);
    }

    private String createStoreFilename(String uuid, String originalFilename) {
        return uuid + "." + extractedExt(originalFilename);
    }

    private String extractedExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    private MultipartFile getResizedImage(BufferedImage image, int cutSize, String storeFilename) throws IOException {
        int ow = image.getWidth();
        int oh = image.getHeight();

        int nw = ow;
        int nh = oh;

        if (oh > cutSize && ow > cutSize) {
            nh = cutSize;
            nw = nh * ow / oh;
        }

        BufferedImage resizedImage = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = resizedImage.getGraphics();
        graphics.drawImage(image, 0, 0, resizedImage.getWidth(), resizedImage.getHeight(),
                0, 0, ow, oh, null);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, extractedExt(storeFilename), os);

        return new MockMultipartFile(storeFilename, os.toByteArray());
    }

    private MultipartFile getCutImage(BufferedImage image, int dw, int dh, String cutFilename) throws IOException {
        int ow = image.getWidth();
        int oh = image.getHeight();

        int nw = ow;
        int nh = (ow * dh) / dw;

        if (nh > oh) {
            nw = (oh * dw) / dh;
            nh = oh;
        }

        BufferedImage resizedImage = new BufferedImage(dw, dh, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.drawImage(image, 0, 0, resizedImage.getWidth(), resizedImage.getHeight(),
                (ow - nw) / 2, (oh - nh) / 2, (ow - nw) / 2 + nw, (oh - nh) / 2 + nh, null);
        graphics.dispose();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, extractedExt(cutFilename), os);

        return new MockMultipartFile(cutFilename, os.toByteArray());
    }
}
