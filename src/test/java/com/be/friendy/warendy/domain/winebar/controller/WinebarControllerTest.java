package com.be.friendy.warendy.domain.winebar.controller;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.config.jwt.filter.JwtAuthenticationFilter;
import com.be.friendy.warendy.domain.winebar.dto.response.WinebarSearchResponse;
import com.be.friendy.warendy.domain.winebar.service.WineBarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(value = WinebarController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtAuthenticationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = TokenProvider.class)
        }
)
class WinebarControllerTest {

    @MockBean
    private WineBarService wineBarService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "MEMBER")
    void successWinebarAroundSearch() throws Exception {
        //given

        List<WinebarSearchResponse> responses =
                Arrays.asList(
                        WinebarSearchResponse.builder()
                                .name("Hong")
                                .picture("Asdfasdf")
                                .address("ASD")
                                .lat(1.1)
                                .lnt(1.1)
                                .rating(1.1)
                                .reviews(1)
                                .build(),
                        WinebarSearchResponse.builder()
                                .name("Hong1")
                                .picture("Asdfasdf")
                                .address("A")
                                .lat(1.13)
                                .lnt(1.13)
                                .rating(1.1)
                                .reviews(1)
                                .build(),
                        WinebarSearchResponse.builder()
                                .name("Hong2")
                                .picture("Asdfasdf")
                                .address("ASDFasd")
                                .lat(1.1)
                                .lnt(1.1)
                                .rating(1.1)
                                .reviews(1)
                                .build()
                );

        given(wineBarService.searchWinebarAround(anyDouble(), anyDouble()))
                .willReturn(responses);
        //when
        //then
        mockMvc.perform(get("/winebars/around")
                        .param("lat", String.valueOf(12.3))
                        .param("lnt", String.valueOf(12.3)))
                .andDo(print())
                .andExpect(jsonPath("$.[0].name").value("Hong"))
        ;
    }
}