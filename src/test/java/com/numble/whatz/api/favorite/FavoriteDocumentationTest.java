package com.numble.whatz.api.favorite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.whatz.application.like.controller.dto.FavoritesDto;
import com.numble.whatz.application.like.service.FavoriteService;
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
        List<FavoritesDto> favoritesDtos = getFavoritesDtos();
        doReturn(favoritesDtos).when(favoriteService).getFavoriteVideos(any(), any());

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
                                fieldWithPath("[].videoThumbnail").description("영상 썸네일")
                        )
                ));
    }

    private List<FavoritesDto> getFavoritesDtos() {
        List<FavoritesDto> favoritesDtos = new ArrayList<>();
        FavoritesDto favoritesDto1 = new FavoritesDto(1L, "/WhatzDev/thumbnail/c95d7879-0f99-44c6-9ad3-acd6251db537/c95d7879-0f99-44c6-9ad3-acd6251db537_CUT.jpeg");
        FavoritesDto favoritesDto2 = new FavoritesDto(2L, "/WhatzDev/thumbnail/1eb39e24-d7f2-4794-950d-2e9cec5361ef/1eb39e24-d7f2-4794-950d-2e9cec5361ef_CUT.jpg");
        FavoritesDto favoritesDto3 = new FavoritesDto(3L, "/WhatzDev/thumbnail/6b85d54c-3391-4393-b074-1ecafacd969b/6b85d54c-3391-4393-b074-1ecafacd969b_CUT.png");
        favoritesDtos.add(favoritesDto1);
        favoritesDtos.add(favoritesDto2);
        favoritesDtos.add(favoritesDto3);
        return favoritesDtos;
    }
}
