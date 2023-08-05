//package com.be.friendy.warendy.domain.review.controller;
//
//import com.be.friendy.warendy.config.AppConfig;
//import com.be.friendy.warendy.config.jwt.TokenProvider;
//import com.be.friendy.warendy.config.jwt.filter.JwtAuthenticationFilter;
//import com.be.friendy.warendy.config.security.SecurityConfig;
//import com.be.friendy.warendy.domain.member.entity.Member;
//import com.be.friendy.warendy.domain.member.entity.constant.Role;
//import com.be.friendy.warendy.domain.review.dto.request.ReviewUpdateRequest;
//import com.be.friendy.warendy.domain.review.dto.request.WineReviewCreateRequest;
//import com.be.friendy.warendy.domain.review.dto.response.MyReviewSearchResponse;
//import com.be.friendy.warendy.domain.review.dto.response.WineReviewCreateResponse;
//import com.be.friendy.warendy.domain.review.dto.response.WineReviewSearchByWineIdResponse;
//import com.be.friendy.warendy.domain.review.entity.Review;
//import com.be.friendy.warendy.domain.review.service.ReviewService;
//import com.be.friendy.warendy.domain.wine.entity.Wine;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(value = ReviewController.class,
//        excludeFilters = {
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
//                        classes = SecurityConfig.class),
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
//                        classes = AppConfig.class),
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
//                        classes = JwtAuthenticationFilter.class),
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
//                        classes = TokenProvider.class)
//        }
//)
//class ReviewControllerTest {
//    @MockBean
//    private ReviewService reviewService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @WithMockUser(roles = "MEMBER")
//    @DisplayName("success create! - wine review")
//    void successCreateWineReview() throws Exception {
//        //given
//        given(reviewService.createWineReview(anyLong(), any()))
//                .willReturn(WineReviewCreateResponse.builder()
//                        .nickname("Hong")
//                        .wineName("AA")
//                        .contents("content")
//                        .createdAt("2020-1-1")
//                        .modifiedAt("2020-1-1")
//                        .rating(1.1f)
//                        .build());
//        //when
//        //then
//        mockMvc.perform(post("/reviews/wines/{wine-id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON).with(csrf())
//                        .content(objectMapper.writeValueAsString(
//                                WineReviewCreateRequest.builder()
//                                        .nickname("Gil")
//                                        .contents("dong")
//                                        .rating(1.0f)
//                                        .build()
//                        )))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.nickname").value("Hong"))
//                .andExpect(jsonPath("$.rating").value(1.1))
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser(roles = "MEMBER")
//    @DisplayName("success search with wine id! - review")
//    void successSearchWineReviewByWineId() throws Exception {
//        //given
//        List<WineReviewSearchByWineIdResponse> responseList =
//                Arrays.asList(
//                        WineReviewSearchByWineIdResponse.builder()
//                                .nickname("Hong")
//                                .contents("AAA")
//                                .rating(1.1f)
//                                .createdAt("now")
//                                .modifiedAt("now")
//                                .build(),
//                        WineReviewSearchByWineIdResponse.builder()
//                                .nickname("Hong2")
//                                .contents("BBB")
//                                .rating(2.f)
//                                .createdAt("now222")
//                                .modifiedAt("now222")
//                                .build(),
//                        WineReviewSearchByWineIdResponse.builder()
//                                .nickname("Hong3")
//                                .contents("CCC")
//                                .rating(3.1f)
//                                .createdAt("now333")
//                                .modifiedAt("now333")
//                                .build()
//                );
//        Page<WineReviewSearchByWineIdResponse> wineReviewPage =
//                new PageImpl<>(responseList);
//        given(reviewService.searchWineReviewByWineId(anyLong(), any()))
//                .willReturn(wineReviewPage);
//        //when
//        //then
//        mockMvc.perform(get("/reviews//wines/{wine-id}", 1L))
//                .andDo(print())
//                .andExpect(jsonPath("$.content[0].nickname")
//                        .value("Hong"))
//        ;
//    }
//
//    @Test
//    @WithMockUser(roles = "MEMBER")
//    @DisplayName("success search my review! - review")
//    void successSearchMyReview() throws Exception {
//        //given
//        List<MyReviewSearchResponse> responseList = Arrays.asList(
//                MyReviewSearchResponse.builder()
//                        .wineName("AAA")
//                        .winePicture("URL123")
//                        .contents("asdf")
//                        .rating(1.1f)
//                        .createdAt("now")
//                        .modifiedAt("now")
//                        .build(),
//                MyReviewSearchResponse.builder()
//                        .wineName("BBB")
//                        .winePicture("URL456")
//                        .contents("asdfasdf")
//                        .rating(3.1f)
//                        .createdAt("now2")
//                        .modifiedAt("now2")
//                        .build(),
//                MyReviewSearchResponse.builder()
//                        .wineName("CCC")
//                        .winePicture("URL789")
//                        .contents("asdfasdfasdf")
//                        .rating(2.1f)
//                        .createdAt("now3")
//                        .modifiedAt("now3")
//                        .build());
//        PageImpl<MyReviewSearchResponse> myReviewPage =
//                new PageImpl<>(responseList);
//        given(reviewService.searchMyReview(anyString(), any()))
//                .willReturn(myReviewPage);
//        //when
//        //then
//        mockMvc.perform(get("/reviews/my-review/{nickname}", "A"))
//                .andDo(print())
//                .andExpect(jsonPath("$.content[0].wineName")
//                        .value("AAA"));
//    }
//
//    @Test
//    @WithMockUser(roles = "MEMBER")
//    @DisplayName("success update! - review")
//    void successUpdateReview() throws Exception {
//        //given
//        List<String> food = new ArrayList<>();
//        food.add("chocolate");
//        given(reviewService.updateReview(anyLong(), any()))
//                .willReturn(Review.builder()
//                        .id(15L)
//                        .member(Member.builder()
//                                .id(13L)
//                                .email("asdf@gmail.com")
//                                .password("asdfasdfasdf")
//                                .nickname("Hong")
//                                .avatar("asdfasdfasdf")
//                                .mbti("istp")
//                                .role(Role.MEMBER).oauthType("fasdf")
//                                .body(1).dry(1).tannin(1).acidity(1)
//                                .build())
//                        .wine(Wine.builder()
//                                .id(1L)
//                                .name("AA")
//                                .vintage(1234)
//                                .price("12345")
//                                .picture("url-?")
//                                .body(1).dry(1).tannin(1).acidity(1).alcohol(1.1)
//                                .grapes("grapes")
//                                .paring(food)
//                                .region("region1")
//                                .type("types")
//                                .winery("winery")
//                                .rating(1.1f)
//                                .build())
//                        .contents("AAAAAAA")
//                        .rating(4.2f)
//                        .build());
//        //when
//        //then
//        mockMvc.perform(put("/reviews/{review-id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON).with(csrf())
//                        .content(objectMapper.writeValueAsString(
//                                ReviewUpdateRequest.builder()
//                                        .contents("CC")
//                                        .rating(1.9f)
//                                        .build())))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.member.nickname")
//                        .value("Hong"))
//                .andExpect(jsonPath("$.wine.name")
//                        .value("AA"))
//                .andExpect(jsonPath("$.contents")
//                        .value("AAAAAAA"))
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser(roles = "MEMBER")
//    @DisplayName("success Delete - review")
//    void successDeleteReview() throws Exception {
//        //given
//        Long deletedReview = 1L;
//        //when
//        //then
//        mockMvc.perform(delete("/reviews/{review-id}", deletedReview)
//                        .contentType(MediaType.APPLICATION_JSON).with(csrf()))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//}