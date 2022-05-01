package com.numble.whatz.api.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.whatz.api.video.dto.EmbedDto;
import com.numble.whatz.application.video.service.VideoStore;
import com.numble.whatz.core.advice.VideoStoreExceptionMessage;
import com.numble.whatz.core.exception.video.CustomVideoStoreException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.io.IOException;

import static com.numble.whatz.api.utils.ApiDocumentUtils.getDocumentRequest;
import static com.numble.whatz.api.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
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
public class VideoDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VideoStore videoStore;

    @Test
    public void videoUploadDirect() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "video/mp4", "<<video data>>".getBytes());
        doReturn(
                "success"
        ).when(videoStore).storeVideo(any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/api/video/add/direct")
                        .file(file)
                        .param("videoThumbnail", "This is Thumbnail")
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
                        requestParts(partWithName("file").optional().description("영상")),
                        requestParameters(
                                parameterWithName("videoThumbnail").description("영상 썸네일"),
                                parameterWithName("title").description("영상 제목"),
                                parameterWithName("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void videoUploadDirectMultipartException() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "video/mp4", "<<video data>>".getBytes());
        doThrow(
                new CustomVideoStoreException(
                        VideoStoreExceptionMessage.MULTIPART_EX, new IOException())
        ).when(videoStore).storeVideo(any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/api/video/add/direct")
                        .file(file)
                        .param("videoThumbnail", "This is Thumbnail")
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        );

        //then
        result.andExpect(status().isForbidden())
                .andDo(document("video-upload-direct-multi", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(partWithName("file").optional().description("영상")),
                        requestParameters(
                                parameterWithName("videoThumbnail").description("영상 썸네일"),
                                parameterWithName("title").description("영상 제목"),
                                parameterWithName("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void videoUploadDirectFFmpegException() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "video/mp4", "<<video data>>".getBytes());
        doThrow(
                new CustomVideoStoreException(
                        VideoStoreExceptionMessage.FFMPEG_EX, new IOException())
        ).when(videoStore).storeVideo(any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/api/video/add/direct")
                        .file(file)
                        .param("videoThumbnail", "This is Thumbnail")
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        );

        //then
        result.andExpect(status().isForbidden())
                .andDo(document("video-upload-direct-ffmpeg", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(partWithName("file").optional().description("영상")),
                        requestParameters(
                                parameterWithName("videoThumbnail").description("영상 썸네일"),
                                parameterWithName("title").description("영상 제목"),
                                parameterWithName("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void videoUploadDirectS3Exception() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "video/mp4", "<<video data>>".getBytes());
        doThrow(
                new CustomVideoStoreException(
                        VideoStoreExceptionMessage.S3_UPLOAD_EX, new IOException())
        ).when(videoStore).storeVideo(any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/api/video/add/direct")
                        .file(file)
                        .param("videoThumbnail", "This is Thumbnail")
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        );

        //then
        result.andExpect(status().isForbidden())
                .andDo(document("video-upload-direct-s3", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(partWithName("file").optional().description("영상")),
                        requestParameters(
                                parameterWithName("videoThumbnail").description("영상 썸네일"),
                                parameterWithName("title").description("영상 제목"),
                                parameterWithName("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void videoUploadEmbed() throws Exception {
        //given
        EmbedDto embedDto = new EmbedDto("임베드 링크", "영상 썸네일", "영상 제목", "영상 내용");

        //when
        ResultActions result = this.mockMvc.perform(
                post("/api/video/add/embed")
                        .content(objectMapper.writeValueAsString(embedDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("video-upload-embed", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("link").description("임베드 링크"),
                                fieldWithPath("videoThumbnail").description("영상 썸네일"),
                                fieldWithPath("title").description("영상 제목"),
                                fieldWithPath("content").description("영상 내용")
                        )
                ));
    }

    @Test
    public void myVideo() throws Exception {
        //given

        //when
        ResultActions result = this.mockMvc.perform(
                get("/api/video")
                        .param("page", "1")
                        .param("size", "3")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("video-myvideo", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("가져올 크기")
                        ),
                        responseFields(
                                fieldWithPath("videos[].videoId").description("비디오 번호"),
                                fieldWithPath("videos[].videoThumbnail").description("비디오 썸네일")
                        )
                ));
    }

    @Test
    public void oneVideo() throws Exception {
        //given

        //when
        ResultActions result = this.mockMvc.perform(
                get("/api/video/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("video-oneVideo", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("비디오 번호")
                        ),
                        responseFields(
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("profile").description("프로필 사진 경로"),
                                fieldWithPath("likes").description("좋아요 수"),
                                fieldWithPath("title").description("영상 제목"),
                                fieldWithPath("content").description("영상 내용"),
                                fieldWithPath("videoDate").description("게시날짜"),
                                fieldWithPath("views").description("조회수"),
                                fieldWithPath("directDir").description("직접 업로드 경로").optional(),
                                fieldWithPath("embedLink").description("임베드 링크").optional()
                        )
                ));
    }

    @Test
    public void modifyVideo() throws Exception {
    }

    /*
    @Test
    public void deleteVideo() throws Exception {
        //given

        //when
        ResultActions result = this.mockMvc.perform(
                post("/api/video/delete")
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

    @Test
    public void favoriteVideos() throws Exception {
        //given

        //when
        ResultActions result = this.mockMvc.perform(
                get("/api/favorite")
                        .param("page", "1")
                        .param("size", "3")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("video-favorite", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("가져올 크기")
                        ),
                        responseFields(
                                fieldWithPath("[].videoId").description("영상 번호"),
                                fieldWithPath("[].videoThumbnail").description("영상 썸네일"),
                                fieldWithPath("[].views").description("영상 조회수")
                        )
                ));
    }

     */
}
