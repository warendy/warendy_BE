package com.be.friendy.warendy.domain.board.controller;

import com.be.friendy.warendy.domain.board.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @MockBean
    private BoardService boardService;

    @Test
    void successCreateBoard() {
        //given
        //when
        //then
    }
}