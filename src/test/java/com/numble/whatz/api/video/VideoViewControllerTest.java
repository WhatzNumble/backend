package com.numble.whatz.api.video;

import com.numble.whatz.application.video.controller.dto.*;
import com.numble.whatz.application.video.service.VideoFileService;
import com.numble.whatz.application.video.service.VideoViewService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.numble.whatz.api.utils.ApiDocumentUtils.getDocumentRequest;
import static com.numble.whatz.api.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser
public class VideoViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoViewService videoViewService;

    @Test
    public void homeApi() throws Exception {
        HomeDto homeDto = getHomeDto();

        doReturn(
                homeDto
        ).when(videoViewService).findAll(any(), any());
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
                                parameterWithName("page").description("????????? ??????"),
                                parameterWithName("size").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("videos[].videoId").description("????????? ??????"),
                                fieldWithPath("videos[].nickname").description("?????????"),
                                fieldWithPath("videos[].profile").description("????????? ?????? ??????"),
                                fieldWithPath("videos[].videoLike").description("????????? ???"),
                                fieldWithPath("videos[].videoTitle").description("?????? ??????"),
                                fieldWithPath("videos[].videoContent").description("?????? ??????"),
                                fieldWithPath("videos[].videoCreationDate").description("????????????"),
                                fieldWithPath("videos[].videoViews").description("?????????"),
                                fieldWithPath("videos[].directDir").description("?????? ????????? ??????").optional(),
                                fieldWithPath("videos[].embedLink").description("????????? ??????").optional(),
                                fieldWithPath("videos[].category").description("?????? ????????????").optional(),
                                fieldWithPath("likeList[]").description("????????? ????????? ?????? ?????????")
                        )
                ));
    }

    @Test
    public void myVideo() throws Exception {
        //given
        doReturn(getHomeDto()).when(videoViewService).getMyVideos(any(), any());

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
                                parameterWithName("page").description("????????? ??????"),
                                parameterWithName("size").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("videos[].videoId").description("????????? ??????"),
                                fieldWithPath("videos[].nickname").description("?????????"),
                                fieldWithPath("videos[].profile").description("????????? ?????? ??????"),
                                fieldWithPath("videos[].videoLike").description("????????? ???"),
                                fieldWithPath("videos[].videoTitle").description("?????? ??????"),
                                fieldWithPath("videos[].videoContent").description("?????? ??????"),
                                fieldWithPath("videos[].videoCreationDate").description("????????????"),
                                fieldWithPath("videos[].videoViews").description("?????????"),
                                fieldWithPath("videos[].directDir").description("?????? ????????? ??????").optional(),
                                fieldWithPath("videos[].embedLink").description("????????? ??????").optional(),
                                fieldWithPath("videos[].category").description("?????? ????????????").optional(),
                                fieldWithPath("likeList[]").description("????????? ????????? ?????? ?????????")
                        )
                ));
    }

    @Test
    public void oneVideo() throws Exception {
        //given
        VideoDetailDto videoDetailDto = getVideoInfoDto();
        doReturn(videoDetailDto).when(videoViewService).getOneVideo(any());

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
                                parameterWithName("id").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("videoId").description("????????? ??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("profile").description("????????? ?????? ??????"),
                                fieldWithPath("videoLike").description("????????? ???"),
                                fieldWithPath("videoTitle").description("?????? ??????"),
                                fieldWithPath("videoContent").description("?????? ??????"),
                                fieldWithPath("videoCreationDate").description("????????????"),
                                fieldWithPath("videoViews").description("?????????"),
                                fieldWithPath("directDir").description("?????? ????????? ??????").optional(),
                                fieldWithPath("embedLink").description("????????? ??????").optional(),
                                fieldWithPath("category").description("?????? ????????????").optional(),
                                fieldWithPath("videoThumbnail").description("?????? ?????????")
                        )
                ));
    }

    private HomeDto getHomeDto() {
        List<Long> likeList = new ArrayList<>();
        likeList.add(1L);
        likeList.add(5L);

        VideoInfoDto homeDto1 = VideoInfoDto.builder()
                .videoId(1L)
                .nickname("user1")
                .profile("profile1")
                .likes(5)
                .title("title1")
                .content("content1")
                .videoDate(LocalDateTime.now())
                .views(20L)
                .directDir("/WhatzDev/d6fb2d78-a6e6-4c11-ab91-f7d00dac52d4/d6fb2d78-a6e6-4c11-ab91-f7d00dac52d4.m3u8")
                .embedLink(null)
                .category("category1")
                .build();
        VideoInfoDto homeDto2 = VideoInfoDto.builder()
                .videoId(2L)
                .nickname("user2")
                .profile("profile2")
                .likes(5)
                .title("title2")
                .content("content2")
                .videoDate(LocalDateTime.now())
                .views(20L)
                .directDir("/WhatzDev/ffaf8b8e-7df5-468c-80b2-fe4cba007be4/ffaf8b8e-7df5-468c-80b2-fe4cba007be4.m3u8")
                .embedLink(null)
                .category("category1")
                .build();
        VideoInfoDto homeDto3 = VideoInfoDto.builder()
                .videoId(3L)
                .nickname("user2")
                .profile("profile2")
                .likes(5)
                .title("title2")
                .content("content2")
                .videoDate(LocalDateTime.now())
                .views(20L)
                .directDir("/WhatzDev/5ebfbf21-4a4b-4361-b00b-d03d3b1d6516/5ebfbf21-4a4b-4361-b00b-d03d3b1d6516.m3u8")
                .embedLink(null)
                .category("category2")
                .build();
        VideoInfoDto homeDto4 = VideoInfoDto.builder()
                .videoId(4L)
                .nickname("user1")
                .profile("profile1")
                .likes(5)
                .title("title1")
                .content("content1")
                .videoDate(LocalDateTime.now())
                .views(20L)
                .directDir(null)
                .category("category2")
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

    private VideoDetailDto getVideoInfoDto() {
        VideoDetailDto videoInfoDto = VideoDetailDto.builder()
                .videoId(1L)
                .videoContent("content1")
                .videoTitle("title1")
                .videoViews(20L)
                .videoCreationDate(LocalDateTime.now())
                .nickname("user1")
                .videoLike(3)
                .profile("profile1")
                .directDir("/WhatzDev/d6fb2d78-a6e6-4c11-ab91-f7d00dac52d4/d6fb2d78-a6e6-4c11-ab91-f7d00dac52d4.m3u8")
                .embedLink("if directDir is null, embedLink has link")
                .videoThumbnail("videoThumbnail1")
                .build();
        return videoInfoDto;
    }
}
