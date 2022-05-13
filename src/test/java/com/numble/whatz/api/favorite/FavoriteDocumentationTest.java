package com.numble.whatz.api.favorite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.whatz.application.like.controller.dto.FavoritesDto;
import com.numble.whatz.application.like.service.FavoriteService;
import com.numble.whatz.application.video.controller.dto.HomeDto;
import com.numble.whatz.application.video.controller.dto.VideoInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.numble.whatz.api.utils.ApiDocumentUtils.getDocumentRequest;
import static com.numble.whatz.api.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser
public class FavoriteDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FavoriteService favoriteService;

    @Test
    public void favorite() throws Exception {
        //given
        doReturn(
                true
        ).when(favoriteService).toggleFavorite(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                post("/api/favorite/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("toggle-favorite", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("비디오 번호")
                        )
                ));
    }

    @Test
    public void favoriteVideo() throws Exception {
        //given
        HomeDto homeDto = getHomeDto();
        doReturn(homeDto).when(favoriteService).getFavoriteVideos(any(), any());

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
                                fieldWithPath("videos[].videoId").description("비디오 번호"),
                                fieldWithPath("videos[].nickname").description("닉네임"),
                                fieldWithPath("videos[].profile").description("프로필 사진 경로"),
                                fieldWithPath("videos[].videoLike").description("좋아요 수"),
                                fieldWithPath("videos[].videoTitle").description("영상 제목"),
                                fieldWithPath("videos[].videoContent").description("영상 내용"),
                                fieldWithPath("videos[].videoCreationDate").description("게시날짜"),
                                fieldWithPath("videos[].videoViews").description("조회수"),
                                fieldWithPath("videos[].directDir").description("직접 업로드 경로").optional(),
                                fieldWithPath("videos[].embedLink").description("임베드 링크").optional(),
                                fieldWithPath("videos[].category").description("영상 카테고리").optional(),
                                fieldWithPath("likeList[]").description("로그인 회원의 관심 리스트")
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
}
