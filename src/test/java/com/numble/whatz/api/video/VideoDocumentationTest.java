package com.numble.whatz.api.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.whatz.application.video.controller.dto.HomeDto;
import com.numble.whatz.application.video.controller.dto.MyVideoDto;
import com.numble.whatz.application.video.controller.dto.MyVideosDto;
import com.numble.whatz.application.video.controller.dto.VideoInfoDto;
import com.numble.whatz.application.video.service.VideoService;
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
public class VideoDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VideoService videoService;

    @Test
    public void homeApi() throws Exception {
        HomeDto homeDto = getHomeDto();

        doReturn(
                homeDto
        ).when(videoService).findAll(any(), any());
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("me");

        //when
        ResultActions result = this.mockMvc.perform(
                get("/api/home")
                        .principal(mockPrincipal)
                        .param("page", "1")
                        .param("size", "3")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("home", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("가져올 크기")
                        ),
                        responseFields(
                                fieldWithPath("videos[].nickname").description("닉네임"),
                                fieldWithPath("videos[].profile").description("프로필 사진 경로"),
                                fieldWithPath("videos[].videoLike").description("좋아요 수"),
                                fieldWithPath("videos[].videoTitle").description("영상 제목"),
                                fieldWithPath("videos[].videoContent").description("영상 내용"),
                                fieldWithPath("videos[].videoCreationDate").description("게시날짜"),
                                fieldWithPath("videos[].videoViews").description("조회수"),
                                fieldWithPath("videos[].directDir").description("직접 업로드 경로").optional(),
                                fieldWithPath("videos[].embedLink").description("임베드 링크").optional(),
                                fieldWithPath("likeList[]").description("로그인 회원의 관심 리스트")
                        )
                ));
    }

    @Test
    public void videoUploadDirect() throws Exception {
        //given
        MockMultipartFile file = getVideoFile();
        MockMultipartFile videoThumbnail = getVideoThumbnail();
        doNothing().when(videoService).saveDirect(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/api/video/add/direct")
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
                multipart("/api/video/add/direct")
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
                multipart("/api/video/add/direct")
                        .file(file)
                        .file(videoThumbnail)
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isForbidden())
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
                multipart("/api/video/add/direct")
                        .file(file)
                        .file(videoThumbnail)
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isForbidden())
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
                multipart("/api/video/add/direct")
                        .file(file)
                        .file(videoThumbnail)
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isForbidden())
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
                multipart("/api/video/add/direct")
                        .file(file)
                        .file(videoThumbnail)
                        .param("title", "This is title")
                        .param("content", "This is content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isForbidden())
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
    public void videoUploadEmbed() throws Exception {
        //given
        MockMultipartFile videoThumbnail = getVideoThumbnail();
        doNothing().when(videoService).saveEmbed(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                multipart("/api/video/add/embed")
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
    public void myVideo() throws Exception {
        //given
        doReturn(getMyVideosDto()).when(videoService).getMyVideos(any(), any());

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
        VideoInfoDto videoInfoDto = getVideoInfoDto();
        doReturn(videoInfoDto).when(videoService).getOneVideo(any());

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
                                fieldWithPath("videoLike").description("좋아요 수"),
                                fieldWithPath("videoTitle").description("영상 제목"),
                                fieldWithPath("videoContent").description("영상 내용"),
                                fieldWithPath("videoCreationDate").description("게시날짜"),
                                fieldWithPath("videoViews").description("조회수"),
                                fieldWithPath("directDir").description("직접 업로드 경로").optional(),
                                fieldWithPath("embedLink").description("임베드 링크").optional()
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
                multipart("/api/video/modify/direct")
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
                multipart("/api/video/modify/embed")
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

    private MockMultipartFile getVideoThumbnail() {
        return new MockMultipartFile("videoThumbnail", "thumbnail.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes());
    }

    private MockMultipartFile getVideoFile() {
        return new MockMultipartFile("file", "video.mp4", "video/mp4", "<<video data>>".getBytes());
    }

    private HomeDto getHomeDto() {
        List<Long> likeList = new ArrayList<>();
        likeList.add(1L);
        likeList.add(5L);

        VideoInfoDto homeDto1 = VideoInfoDto.builder()
                .nickname("user1")
                .profile("profile1")
                .likes(5)
                .title("title1")
                .content("content1")
                .videoDate(LocalDateTime.now())
                .views(20L)
                .directDir("/WhatzDev/d6fb2d78-a6e6-4c11-ab91-f7d00dac52d4/d6fb2d78-a6e6-4c11-ab91-f7d00dac52d4.m3u8")
                .embedLink(null)
                .build();
        VideoInfoDto homeDto2 = VideoInfoDto.builder()
                .nickname("user2")
                .profile("profile2")
                .likes(5)
                .title("title2")
                .content("content2")
                .videoDate(LocalDateTime.now())
                .views(20L)
                .directDir("/WhatzDev/ffaf8b8e-7df5-468c-80b2-fe4cba007be4/ffaf8b8e-7df5-468c-80b2-fe4cba007be4.m3u8")
                .embedLink(null)
                .build();
        VideoInfoDto homeDto3 = VideoInfoDto.builder()
                .nickname("user2")
                .profile("profile2")
                .likes(5)
                .title("title2")
                .content("content2")
                .videoDate(LocalDateTime.now())
                .views(20L)
                .directDir("/WhatzDev/5ebfbf21-4a4b-4361-b00b-d03d3b1d6516/5ebfbf21-4a4b-4361-b00b-d03d3b1d6516.m3u8")
                .embedLink(null)
                .build();
        VideoInfoDto homeDto4 = VideoInfoDto.builder()
                .nickname("user1")
                .profile("profile1")
                .likes(5)
                .title("title1")
                .content("content1")
                .videoDate(LocalDateTime.now())
                .views(20L)
                .directDir(null)
                .embedLink("https://www.youtube.com/watch?v=_whaAD__3vI")
                .build();
        List<VideoInfoDto> videoInfoDtos = new ArrayList<>();
        videoInfoDtos.add(homeDto1);
        videoInfoDtos.add(homeDto2);
        videoInfoDtos.add(homeDto3);
        videoInfoDtos.add(homeDto4);

        HomeDto homeDto = new HomeDto(videoInfoDtos, likeList);
        return homeDto;
    }

    private MyVideosDto getMyVideosDto() {
        List<MyVideoDto> videos = new ArrayList<>();

        MyVideoDto video1 = new MyVideoDto(1L, "/WhatzDev/thumbnail/c95d7879-0f99-44c6-9ad3-acd6251db537/c95d7879-0f99-44c6-9ad3-acd6251db537_CUT.jpeg");
        MyVideoDto video2 = new MyVideoDto(2L, "/WhatzDev/thumbnail/1eb39e24-d7f2-4794-950d-2e9cec5361ef/1eb39e24-d7f2-4794-950d-2e9cec5361ef_CUT.jpg");
        MyVideoDto video3 = new MyVideoDto(3L, "/WhatzDev/thumbnail/6b85d54c-3391-4393-b074-1ecafacd969b/6b85d54c-3391-4393-b074-1ecafacd969b_CUT.png");
        MyVideoDto video4 = new MyVideoDto(4L, "/WhatzDev/thumbnail/2d1f1d9e-7fb0-4189-ae03-79a07494dc73/2d1f1d9e-7fb0-4189-ae03-79a07494dc73_CUT.jpeg");

        videos.add(video1);
        videos.add(video2);
        videos.add(video3);
        videos.add(video4);

        MyVideosDto myVideosDto = new MyVideosDto(videos);
        return myVideosDto;
    }

    private VideoInfoDto getVideoInfoDto() {
        VideoInfoDto videoInfoDto = VideoInfoDto.builder()
                .content("content1")
                .title("title1")
                .views(20L)
                .videoDate(LocalDateTime.now())
                .nickname("user1")
                .likes(3)
                .profile("profile1")
                .directDir("/WhatzDev/d6fb2d78-a6e6-4c11-ab91-f7d00dac52d4/d6fb2d78-a6e6-4c11-ab91-f7d00dac52d4.m3u8")
                .build();
        return videoInfoDto;
    }
}
