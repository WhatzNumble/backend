package com.numble.whatz.api.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.whatz.application.video.controller.dto.HomeDto;
import com.numble.whatz.application.video.controller.dto.VideoInfoDto;
import com.numble.whatz.application.video.service.VideoFileService;
import com.numble.whatz.application.video.service.VideoViewService;
import com.numble.whatz.core.advice.dto.ThumbnailStoreExceptionMessage;
import com.numble.whatz.core.advice.dto.VideoStoreExceptionMessage;
import com.numble.whatz.core.exception.thumbnail.ThumbnailStoreException;
import com.numble.whatz.core.exception.video.VideoStoreException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.numble.whatz.api.utils.ApiDocumentUtils.getDocumentRequest;
import static com.numble.whatz.api.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser
public class VideoFileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoFileService videoService;

    @Test
    public void videoUploadDirect() throws Exception {
        //given
        MockMultipartFile file = getVideoFile();
        MockMultipartFile videoThumbnail = getVideoThumbnail();
        doNothing().when(videoService).saveDirect(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/video/add/direct")
                        .file(file)
                        .file(videoThumbnail)
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("video-upload-direct", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("file").optional().description("영상"),
                                partWithName("videoThumbnail").optional().description("영상 썸네일")
                        ),
                        requestParameters(
                                parameterWithName("title").description("영상 제목"),
                                parameterWithName("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void videoUploadDirectThumbnailException() throws Exception {
        //given
        MockMultipartFile file = getVideoFile();
        MockMultipartFile videoThumbnail = getVideoThumbnail();
        doThrow(
                new ThumbnailStoreException(
                        ThumbnailStoreExceptionMessage.IMAGE_CONVERT_EXCEPTION, new IOException())
        ).when(videoService).saveDirect(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/video/add/direct")
                        .file(file)
                        .file(videoThumbnail)
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isInternalServerError())
                .andDo(document("video-upload-direct-ioEx", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("file").optional().description("영상"),
                                partWithName("videoThumbnail").optional().description("영상 썸네일")
                        ),
                        requestParameters(
                                parameterWithName("title").description("영상 제목"),
                                parameterWithName("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void videoUploadDirectLocalDeleteException() throws Exception {
        //given
        MockMultipartFile file = getVideoFile();
        MockMultipartFile videoThumbnail = getVideoThumbnail();
        doThrow(
                new VideoStoreException(
                        VideoStoreExceptionMessage.LOCAL_DELETE_EX, new IOException())
        ).when(videoService).saveDirect(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/video/add/direct")
                        .file(file)
                        .file(videoThumbnail)
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isInternalServerError())
                .andDo(document("video-upload-direct-localex", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("file").optional().description("영상"),
                                partWithName("videoThumbnail").optional().description("영상 썸네일")
                        ),
                        requestParameters(
                                parameterWithName("title").description("영상 제목"),
                                parameterWithName("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void videoUploadDirectMultipartException() throws Exception {
        //given
        MockMultipartFile file = getVideoFile();
        MockMultipartFile videoThumbnail = getVideoThumbnail();
        doThrow(
                new VideoStoreException(
                        VideoStoreExceptionMessage.MULTIPART_EX, new IOException())
        ).when(videoService).saveDirect(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/video/add/direct")
                        .file(file)
                        .file(videoThumbnail)
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isInternalServerError())
                .andDo(document("video-upload-direct-multi", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("file").optional().description("영상"),
                                partWithName("videoThumbnail").optional().description("영상 썸네일")
                        ),
                        requestParameters(
                                parameterWithName("title").description("영상 제목"),
                                parameterWithName("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void videoUploadDirectFFmpegException() throws Exception {
        //given
        MockMultipartFile file = getVideoFile();
        MockMultipartFile videoThumbnail = getVideoThumbnail();
        doThrow(
                new VideoStoreException(
                        VideoStoreExceptionMessage.FFMPEG_EX, new IOException())
        ).when(videoService).saveDirect(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/video/add/direct")
                        .file(file)
                        .file(videoThumbnail)
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isInternalServerError())
                .andDo(document("video-upload-direct-ffmpeg", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("file").optional().description("영상"),
                                partWithName("videoThumbnail").optional().description("영상 썸네일")
                        ),
                        requestParameters(
                                parameterWithName("title").description("영상 제목"),
                                parameterWithName("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void videoUploadDirectS3Exception() throws Exception {
        //given
        MockMultipartFile file = getVideoFile();
        MockMultipartFile videoThumbnail = getVideoThumbnail();
        doThrow(
                new VideoStoreException(
                        VideoStoreExceptionMessage.S3_UPLOAD_EX, new IOException())
        ).when(videoService).saveDirect(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/video/add/direct")
                        .file(file)
                        .file(videoThumbnail)
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isInternalServerError())
                .andDo(document("video-upload-direct-s3", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("file").optional().description("영상"),
                                partWithName("videoThumbnail").optional().description("영상 썸네일")
                        ),
                        requestParameters(
                                parameterWithName("title").description("영상 제목"),
                                parameterWithName("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void videoUploadNullFileException() throws Exception {
        //given
        MockMultipartFile file = getVideoFile();
        MockMultipartFile videoThumbnail = getVideoThumbnail();
        doThrow(
                new VideoStoreException(
                        VideoStoreExceptionMessage.MULTIPART_NULL, new IOException())
        ).when(videoService).saveDirect(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/video/add/direct")
                        .file(file)
                        .file(videoThumbnail)
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isForbidden())
                .andDo(document("video-upload-direct-vNullex", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("file").optional().description("영상"),
                                partWithName("videoThumbnail").optional().description("영상 썸네일")
                        ),
                        requestParameters(
                                parameterWithName("title").description("영상 제목"),
                                parameterWithName("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void videoUploadThumbnailNullFileException() throws Exception {
        //given
        MockMultipartFile file = getVideoFile();
        MockMultipartFile videoThumbnail = getVideoThumbnail();
        doThrow(
                new ThumbnailStoreException(
                        ThumbnailStoreExceptionMessage.MULTIPART_NULL, new NullPointerException("썸네일이 비어있습니다."))
        ).when(videoService).saveDirect(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/video/add/direct")
                        .file(file)
                        .file(videoThumbnail)
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isForbidden())
                .andDo(document("video-upload-direct-tNullex", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("file").optional().description("영상"),
                                partWithName("videoThumbnail").optional().description("영상 썸네일")
                        ),
                        requestParameters(
                                parameterWithName("title").description("영상 제목"),
                                parameterWithName("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void videoUploadEmbed() throws Exception {
        //given
        MockMultipartFile videoThumbnail = getVideoThumbnail();
        doNothing().when(videoService).saveEmbed(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/video/add/embed")
                        .file(videoThumbnail)
                        .param("link", "임베드 링크")
                        .param("title", "영상 제목")
                        .param("content", "영상 내용")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("video-upload-embed", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("videoThumbnail").optional().description("영상 썸네일")
                        ),
                        requestParameters(
                                parameterWithName("link").description("임베드 링크"),
                                parameterWithName("title").description("영상 제목"),
                                parameterWithName("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void modifyDirect() throws Exception {
        //given
        MockMultipartFile file = getVideoFile();
        MockMultipartFile videoThumbnail = getVideoThumbnail();
        doNothing().when(videoService).modifyDirect(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/video/modify/direct")
                        .file(file)
                        .file(videoThumbnail)
                        .param("id", "1")
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("video-modify-direct", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("file").optional().description("영상"),
                                partWithName("videoThumbnail").optional().description("영상 썸네일")
                        ),
                        requestParameters(
                                parameterWithName("id").description("수정할 영상 번호"),
                                parameterWithName("title").description("영상 제목"),
                                parameterWithName("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void modifyEmbed() throws Exception {
        //given
        MockMultipartFile videoThumbnail = getVideoThumbnail();
        doNothing().when(videoService).modifyEmbed(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/video/modify/embed")
                        .file(videoThumbnail)
                        .param("id", "1")
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .param("link", "This is link")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("video-modify-embed", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("videoThumbnail").optional().description("영상 썸네일")
                        ),
                        requestParameters(
                                parameterWithName("id").description("수정할 영상 번호"),
                                parameterWithName("title").description("수정할 영상 제목"),
                                parameterWithName("content").description("수정할 영상 내용"),
                                parameterWithName("link").description("수정할 임베드 링크")
                        )
                ));
    }

    @Test
    public void deleteVideo() throws Exception {
        //given
        doNothing().when(videoService).removeVideo(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                post("/video/delete")
                        .param("id", "1")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("video-deleteVideo", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("id").description("비디오 번호")
                        )
                ));
    }

    private MockMultipartFile getVideoThumbnail() {
        return new MockMultipartFile("videoThumbnail", "thumbnail.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes());
    }

    private MockMultipartFile getVideoFile() {
        return new MockMultipartFile("file", "video.mp4", "video/mp4", "<<video data>>".getBytes());
    }
}
