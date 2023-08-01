package com.be.friendy.warendy.domain.board.controller;

import com.be.friendy.warendy.config.AppConfig;
import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.config.jwt.filter.JwtAuthenticationFilter;
import com.be.friendy.warendy.config.security.SecurityConfig;
import com.be.friendy.warendy.domain.board.dto.request.BoardCreateRequest;
import com.be.friendy.warendy.domain.board.dto.request.BoardUpdateRequest;
import com.be.friendy.warendy.domain.board.dto.response.BoardCreateResponse;
import com.be.friendy.warendy.domain.board.dto.response.BoardSearchDetailResponse;
import com.be.friendy.warendy.domain.board.dto.response.BoardSearchResponse;
import com.be.friendy.warendy.domain.board.entity.Board;
import com.be.friendy.warendy.domain.board.service.BoardService;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.entity.constant.Role;
import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = BoardController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = AppConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtAuthenticationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = TokenProvider.class)
        }
)
class BoardControllerTest {

    @MockBean
    private BoardService boardService;

    @Autowired
    private MockMvc mockMvc;

    //json에서 오브젝트로 오브젝트에서 json으로 바꿔주는
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("success Create! - board")
    void successCreateBoard() throws Exception {
        //given
        given(boardService.creatBoard(anyLong(), any()))
                .willReturn(BoardCreateResponse.builder()
                        .memberId(1L)
                        .winebarId(1L)
                        .name("board name")
                        .creator("nick name")
                        .date("2010-1-1")
                        .time("7AM")
                        .wineName("wine name")
                        .headcount(4)
                        .contents("hello world!")
                        .build());
        //when
        //then
        mockMvc.perform(post("/boards/winebars/1")
                        .contentType(MediaType.APPLICATION_JSON).with(csrf())
                        .content(objectMapper
                                .writeValueAsString(BoardCreateRequest
                                        .builder()
                                        .memberId(14L)
                                        .name("board name1")
                                        .creator("nick name1")
                                        .date("2010-1-11")
                                        .time("7AM")
                                        .wineName("wine name1")
                                        .headcount(4)
                                        .contents("hello world!1")
                                        .build())))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.memberId").value(1))
                .andExpect(
                        jsonPath("$.name").value("board name"))
                .andExpect(
                        jsonPath("$.contents")
                                .value("hello world!"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("success Update! - board")
    void successUpdateBoard() throws Exception {
        //given
        given(boardService.updateBoard(anyLong(), any()))
                .willReturn(Board.builder().id(1L)
                        .member(Member.builder().id(1L)
                                .email("asdf@gmail.com")
                                .password("asdfasdfasdf")
                                .nickname("nick name").avatar("asdfasdfasdf")
                                .mbti("istp")
                                .role(Role.MEMBER).oauthType("fasdf")
                                .body(1).dry(1).tannin(1).acidity(1)
                                .build())
                        .winebar(Winebar.builder().id(1L)
                                .name("AAA").picture("asdfasd")
                                .address("Asdfasdf").lnt(0.0).lat(0.0)
                                .rating(0.0).reviews(1)
                                .build())
                        .name("board name")
                        .creator("nick name")
                        .date("2010-1-1")
                        .time("7AM")
                        .wineName("wine name")
                        .headcount(4)
                        .contents("hello world!")
                        .build());

        //when
        //then
        mockMvc.perform(put("/boards/{board-id}", 1)
                        .contentType(MediaType.APPLICATION_JSON).with(csrf())
                        .content(objectMapper
                                .writeValueAsString(BoardUpdateRequest
                                        .builder()
                                        .memberId(14L)
                                        .winebarId(15L)
                                        .name("board name1")
                                        .creator("nick name1")
                                        .date("2010-1-11")
                                        .time("7AM")
                                        .wineName("wine name1")
                                        .headcount(4)
                                        .contents("hello world!1")
                                        .build())))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.member.id").value(1))
                .andExpect(
                        jsonPath("$.winebar.id").value(1))
                .andExpect(
                        jsonPath("$.name").value("board name"))
                .andExpect(
                        jsonPath("$.contents").value("hello world!"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("success Search Detail! - board")
    void successSearchBoardDetail() throws Exception {
        //given
        given(boardService.searchBoardDetail(anyLong()))
                .willReturn(BoardSearchDetailResponse.builder()
                        .name("A")
                        .winebarName("B")
                        .winebarAddress("C")
                        .creator("D")
                        .date("2020-1-1")
                        .time("7PM")
                        .wineName("E")
                        .headcount(3)
                        .contents("F")
                        .build());
        //when
        //then
        mockMvc.perform(get("/boards/{board-id}/detail", 1L))
                .andDo(print())
                .andExpect(jsonPath("$.name").value("A"))
        ;
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("success Search with a Creator! - board")
    void successSearchBoardByCreator() throws Exception {
        //given
        List<BoardSearchResponse> boardSearchResponses =
                Arrays.asList(
                        BoardSearchResponse.builder()
                                .winebarName("wine bar")
                                .name("board")
                                .creator("Hong")
                                .date("2000-1-1")
                                .time("7AM")
                                .wineName("wine")
                                .headcount(4)
                                .contents("content yo")
                                .build(),
                        BoardSearchResponse.builder()
                                .winebarName("wine bar2")
                                .name("board2")
                                .creator("Hong")
                                .date("2000-1-12")
                                .time("10AM")
                                .wineName("wine2")
                                .headcount(5)
                                .contents("content yo2")
                                .build(),
                        BoardSearchResponse.builder()
                                .winebarName("wine bar3")
                                .name("board3")
                                .creator("Hong")
                                .date("2000-1-13")
                                .time("8AM")
                                .wineName("wine3")
                                .headcount(6)
                                .contents("content yo3")
                                .build()
                );
        PageImpl<BoardSearchResponse> boardSearchResponsePage =
                new PageImpl<>(boardSearchResponses);
        given(boardService.searchBoardByCreator(anyString(), any()))
                .willReturn(boardSearchResponsePage);
        //when
        //then
        mockMvc.perform(get("/boards/creator?creator=1&page=0"))
                .andDo(print())
                .andExpect(jsonPath("$.content[0].creator")
                        .value("Hong"))
                .andExpect(jsonPath("$.content[1].creator")
                        .value("Hong"))
        ;
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("success Delete - board")
    void successDeleteBoard() throws Exception {
        //given
        Long deletedBoardID = 1L;
        //when
        //then
        mockMvc.perform(delete("/boards/{boardId}", deletedBoardID)
                        .contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("failed search for wine name - board")
    void failedSearchBoardByWineName() throws Exception {
        //given
        given(boardService.searchBoardByWinebarName(anyString(), any()))
                .willThrow(new RuntimeException("the wine does not exist"));
        //when
        //then
        mockMvc.perform(get("/boards/wine-name?wineName={}", "AAA")
                        .contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

}