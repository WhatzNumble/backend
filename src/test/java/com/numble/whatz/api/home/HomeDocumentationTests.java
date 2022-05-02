package com.numble.whatz.api.home;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.numble.whatz.api.utils.ApiDocumentUtils.getDocumentRequest;
import static com.numble.whatz.api.utils.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
public class HomeDocumentationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /*
    @Test
    public void test() throws Exception {
        //given
        TestDto testDto = TestDto.builder()
                .id(1L)
                .email("test")
                .build();

        //when
        ResultActions result = this.mockMvc.perform(
//                get("/api/accounts/{id}", 1L))
                post("/test")
                        .content(objectMapper.writeValueAsString(testDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("For-Test", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("들어온 아이디"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("들어온 이메일")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("결과메시지")
                        )
                ));
    }

    @Test
    public void test2() throws Exception {
        //given

        //when
        ResultActions result = this.mockMvc.perform(
                get("/test2/")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("For-Test1",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("1[].id").description("리스트안에 아이디가 들어있습니다."),
                                fieldWithPath("1[].email").description("리스트안에 이메일이 들어있습니다.")
                        )
                ));
    }
     */

    @Test
    public void homeApi() throws Exception {

        //when
        ResultActions result = this.mockMvc.perform(
                get("/api/home")
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
                                fieldWithPath("videos[].likes").description("좋아요 수"),
                                fieldWithPath("videos[].title").description("영상 제목"),
                                fieldWithPath("videos[].content").description("영상 내용"),
                                fieldWithPath("videos[].videoDate").description("게시날짜"),
                                fieldWithPath("videos[].views").description("조회수"),
                                fieldWithPath("videos[].directDir").description("직접 업로드 경로").optional(),
                                fieldWithPath("videos[].embedLink").description("임베드 링크").optional(),
                                fieldWithPath("likeList[]").description("로그인 회원의 관심 리스트")
                        )
                ));
    }
}
