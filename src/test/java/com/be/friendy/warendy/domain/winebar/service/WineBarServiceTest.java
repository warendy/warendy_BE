package com.be.friendy.warendy.domain.winebar.service;

import com.be.friendy.warendy.domain.winebar.dto.response.WinebarSearchResponse;
import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import com.be.friendy.warendy.domain.winebar.repository.WinebarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WineBarServiceTest {
    @Mock
    private WinebarRepository winebarRepository;

    @InjectMocks
    private WineBarService wineBarService;

    @Test
    void successSearchWinebarAround() {
        //given
        List<Winebar> winebarList =
                Arrays.asList(
                        Winebar.builder()
                                .id(2L)
                                .name("Hong")
                                .picture("Asdfasdf")
                                .address("ASD")
                                .lat(1.1)
                                .lnt(1.1)
                                .rating(1.1)
                                .reviews(1)
                                .build(),
                        Winebar.builder()
                                .id(1L)
                                .name("Hong1")
                                .picture("Asdfasdf")
                                .address("A")
                                .lat(1.13)
                                .lnt(1.13)
                                .rating(1.1)
                                .reviews(1)
                                .build(),
                        Winebar.builder()
                                .id(3L)
                                .name("Hong2")
                                .picture("Asdfasdf")
                                .address("ASDFasd")
                                .lat(1.1)
                                .lnt(1.1)
                                .rating(1.1)
                                .reviews(1)
                                .build()
                );

        given(winebarRepository.findByDistance(anyDouble(), anyDouble()))
                .willReturn(winebarList);
        //when
        List<WinebarSearchResponse> responses = wineBarService
                .searchWinebarAround(1.1, 1.1);
        //then
        assertEquals("Hong", responses.get(0).getName());
    }


}