package com.be.friendy.warendy.domain.wine.controller;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.config.jwt.filter.JwtAuthenticationFilter;
import com.be.friendy.warendy.domain.review.dto.response.WineReviewSearchByWineIdResponse;
import com.be.friendy.warendy.domain.review.entity.Review;
import com.be.friendy.warendy.domain.wine.dto.response.WineDetailSearchResponse;
import com.be.friendy.warendy.domain.wine.service.WineService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = WineController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtAuthenticationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = TokenProvider.class)
        }
)
class WineControllerTest {

    @MockBean
    private WineService wineService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("success search wine detail! - wine")
    void successSearchWineDetail() throws Exception {
        //given
        List<Review> reviewList = new ArrayList<>();
        List<WineReviewSearchByWineIdResponse> list = reviewList.stream()
                .map(WineReviewSearchByWineIdResponse::fromEntity).toList();
        given(wineService.searchWineDetail(anyLong()))
                .willReturn(WineDetailSearchResponse.builder()
                        .wineName("wineName")
                        .vintage(2014)
                        .price("W 25,000")
                        .picture("url")
                        .body(1)
                        .dry(1)
                        .tannin(1)
                        .acidity(1)
                        .alcohol(1.1)
                        .grapes("grapes")
                        .pairing("food")
                        .region("region")
                        .type("wineStyle")
                        .winery("winery")
                        .rating(1.1f)
                        .reviewList(list)
                        .build());
        //when
        //then
        mockMvc.perform(get("/wines/1/detail")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pairing")
                        .value("food"))
        ;
    }

}