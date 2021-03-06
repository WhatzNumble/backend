package com.numble.whatz.api.video;

import com.numble.whatz.application.video.controller.dto.MainContentDetailDto;
import com.numble.whatz.application.video.controller.dto.MainContentDto;
import com.numble.whatz.application.video.controller.dto.MainContentsDto;
import com.numble.whatz.application.video.controller.dto.UserVideoDto;
import com.numble.whatz.application.video.controller.dto.UserVideosDto;
import com.numble.whatz.application.video.service.AdminService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(roles = {"MEMBER", "ADMIN"})
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    public void userVideoListTest() throws Exception {
        //given
        UserVideosDto userVideosDto = getUserVideosDto();

        doReturn(userVideosDto).when(adminService).getUserVideos(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                get("/api/admin/user/{id}", 1L)
                        .param("page", "1")
                        .param("size", "3")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("admin-user-videos", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("?????? ??????")
                        ),
                        requestParameters(
                                parameterWithName("page").description("????????? ??????"),
                                parameterWithName("size").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("videos[].id").description("????????? ??????"),
                                fieldWithPath("videos[].type").description("????????? ????????? ??????"),
                                fieldWithPath("videos[].title").description("?????? ??????"),
                                fieldWithPath("videos[].description").description("????????? ???"),
                                fieldWithPath("videos[].thumbnail").description("?????? ????????? Uri"),
                                fieldWithPath("videos[].videoUri").description("?????? Uri"),
                                fieldWithPath("userId").description("?????? ??????"),
                                fieldWithPath("email").description("?????? ?????????"),
                                fieldWithPath("nickname").description("?????? ?????????")
                        )
                ));
    }

    @Test
    public void mainContentTest() throws Exception {
        MainContentsDto mainContentsDto = getMainContentsDto();

        doReturn(mainContentsDto).when(adminService).getMainContent(any());

        //when
        ResultActions result = this.mockMvc.perform(
                get("/api/admin/main")
                        .param("page", "1")
                        .param("size", "3")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("admin-main", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("page").description("????????? ??????"),
                                parameterWithName("size").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("contents[].id").description("????????? ??????"),
                                fieldWithPath("contents[].videoThumbnail").description("????????? ?????????"),
                                fieldWithPath("contents[].title").description("????????? ??????"),
                                fieldWithPath("contents[].nickName").description("?????? ?????????")
                        )
                ));
    }

    @Test
    public void mainContentDetailTest() throws Exception {
        MainContentDetailDto mainContentDetailDto = getMainContentDetailDto();

        doReturn(mainContentDetailDto).when(adminService).getDetail(any());

        //when
        ResultActions result = this.mockMvc.perform(
                get("/api/admin/main/{videoId}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("admin-main-video", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("videoId").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("videoId").description("????????? ??????"),
                                fieldWithPath("showId").description("????????? ????????????"),
                                fieldWithPath("nickname").description("?????? ?????????"),
                                fieldWithPath("userId").description("?????? ??????"),
                                fieldWithPath("userEmail").description("?????? ?????????"),
                                fieldWithPath("videoLike").description("????????? ???"),
                                fieldWithPath("videoTitle").description("????????? ??????"),
                                fieldWithPath("videoContent").description("????????? ??????"),
                                fieldWithPath("videoCreationDate").description("????????? ?????? ??????"),
                                fieldWithPath("videoViews").description("????????? ?????????"),
                                fieldWithPath("videoUrl").description("????????? ??????"),
                                fieldWithPath("videoThumbnail").description("????????? ?????????"),
                                fieldWithPath("type").description("????????? ????????? ??????")
                        )
                ));
    }

    @Test
    public void deleteVideoTest() throws Exception {
        doNothing().when(adminService).removeVideo(any());

        //when
        ResultActions result = this.mockMvc.perform(
                post("/api/admin/main/delete/{videoId}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("admin-main-delete", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("videoId").description("????????? ??????")
                        )
                ));
    }

    @Test
    public void modifyVideoIdTest() throws Exception {
        doNothing().when(adminService).modifyVideoId(any(), any());

        //when
        ResultActions result = this.mockMvc.perform(
                post("/api/admin/main/modify/{videoId}", 1L)
                        .param("showId", "2")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("admin-main-modify", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("videoId").description("????????? ??????")
                        ),
                        requestParameters(
                                parameterWithName("showId").description("???????????? ?????? showId")
                        )
                ));
    }

    private MainContentDetailDto getMainContentDetailDto() {
        MainContentDetailDto mainContentDetailDto = MainContentDetailDto.builder()
                .videoId(0L)
                .showId(0L)
                .userEmail("userEmail1")
                .userId(0L)
                .nickname("nickname1")
                .videoCreationDate(LocalDateTime.now())
                .videoViews(10L)
                .videoTitle("title1")
                .videoContent("content1")
                .videoLike(2)
                .videoThumbnail("Thumbnail1")
                .build();
        mainContentDetailDto.setVideoUrl("url1");
        mainContentDetailDto.setType("self");
        return mainContentDetailDto;
    }

    private MainContentsDto getMainContentsDto() {
        List<MainContentDto> mainContentDtos = new ArrayList<>();
        MainContentDto mainContentDto1 = MainContentDto.builder()
                .id(0L)
                .nickName("nickName1")
                .title("title1")
                .videoThumbnail("thumbnail1")
                .build();
        MainContentDto mainContentDto2 = MainContentDto.builder()
                .id(1L)
                .nickName("nickName2")
                .title("title2")
                .videoThumbnail("thumbnail2")
                .build();
        MainContentDto mainContentDto3 = MainContentDto.builder()
                .id(2L)
                .nickName("nickName3")
                .title("title3")
                .videoThumbnail("thumbnail3")
                .build();
        mainContentDtos.add(mainContentDto1);
        mainContentDtos.add(mainContentDto2);
        mainContentDtos.add(mainContentDto3);

        MainContentsDto mainContentsDto = new MainContentsDto(mainContentDtos);
        return mainContentsDto;
    }

    private UserVideosDto getUserVideosDto() {
        List<UserVideoDto> userVideoDtos = new ArrayList<>();
        UserVideoDto userVideoDto1 = new UserVideoDto(0L, "self", "title1", "description1", "thumbnail1", "videoUri1");
        UserVideoDto userVideoDto2 = new UserVideoDto(1L, "self", "title2", "description2", "thumbnail2", "videoUri2");
        userVideoDtos.add(userVideoDto1);
        userVideoDtos.add(userVideoDto2);

        UserVideosDto userVideosDto = new UserVideosDto(0L, "email1", "nickName1", userVideoDtos);
        return userVideosDto;
    }
}
