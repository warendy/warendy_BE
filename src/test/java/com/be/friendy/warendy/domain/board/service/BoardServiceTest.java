package com.be.friendy.warendy.domain.board.service;

import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.board.dto.request.BoardCreateRequest;
import com.be.friendy.warendy.domain.board.dto.request.BoardUpdateRequest;
import com.be.friendy.warendy.domain.board.dto.response.*;
import com.be.friendy.warendy.domain.board.entity.Board;
import com.be.friendy.warendy.domain.board.repository.BoardRepository;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.entity.constant.Role;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.be.friendy.warendy.domain.wine.repository.WineRepository;
import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import com.be.friendy.warendy.domain.winebar.repository.WinebarRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private WinebarRepository wineBarRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private WineRepository wineRepository;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private BoardService boardService;

    @Test
    @DisplayName("success create! - board")
    void successCreateBoard() {
        //given
        HashSet<String> partySet = new HashSet<>();
        partySet.add("Hong");
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("nick name1").avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Winebar winebar1 = Winebar.builder()
                .id(14L)
                .name("AA")
                .picture("asdfasdf")
                .address("tttttttt")
                .lat(0.0)
                .lnt(0.0)
                .rating(1.1)
                .reviews(1)
                .build();
        Board createdBoard = Board.builder()
                .id(1L)
                .member(member1)
                .winebar(winebar1)
                .name("A")
                .nickname("A")
                .date("2010-1-13")
                .time("7AM")
                .wineName("123")
                .headcount(4)
                .contents("123")
                .participants(partySet)
                .build();
        given(wineBarRepository.findById(anyLong()))
                .willReturn(Optional.of(winebar1));
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member1));
        given(boardRepository.save(any()))
                .willReturn(createdBoard);
        //when
        BoardCreateRequest createRequest = BoardCreateRequest.builder()
                .memberId(1L)
                .name("board name")
                .nickname("nick name")
                .date("2010-1-1")
                .time("7AM")
                .wineName("wine name")
                .headcount(6)
                .contents("hello world!")
                .build();

        BoardCreateResponse createResponse =
                boardService.creatBoard("AAA", 13L, createRequest);
        //then - given에서 값과 일치
        assertEquals(13L, createResponse.getMemberId());
        assertEquals(14L, createResponse.getWinebarId());
        assertEquals("A", createResponse.getNickname());
        assertEquals(4, createResponse.getHeadcount());
        assertEquals(1, createResponse.getParticipants().size());
    }

    @Test
    void successSearchBoardDetail() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("nick name").avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Winebar winebar1 = Winebar.builder()
                .id(1L)
                .name("AA")
                .picture("asdfasdf")
                .address("tttttttt")
                .lat(0.0)
                .lnt(0.0)
                .rating(1.1)
                .reviews(1)
                .build();
        HashSet<String> partySet = new HashSet<>();
        partySet.add(member1.getNickname());
        Board board = Board.builder()
                .id(1L).member(member1).winebar(winebar1).name("A")
                .nickname("A").date("2010-1-13").time("7AM")
                .wineName("123").headcount(4).contents("123")
                .participants(partySet)
                .build();
        given(boardRepository.findById(board.getId()))
                .willReturn(Optional.of(board));
        //when
        BoardSearchDetailResponse boardDetailResponse
                = boardService.searchBoardDetail(1L);
        //then
        assertEquals("AA", boardDetailResponse.getWinebarName());
        assertEquals(1, boardDetailResponse.getParticipants().size());

    }

    @Test
    void successSearchMyBoard() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("nick name").avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Winebar winebar1 = Winebar.builder()
                .id(1L)
                .name("AA")
                .picture("asdfasdf")
                .address("tttttttt")
                .lat(0.0)
                .lnt(0.0)
                .rating(1.1)
                .reviews(1)
                .build();
        HashSet<String> partySet = new HashSet<>();
        partySet.add(member1.getNickname());
        List<Board> boardList = Arrays.asList(
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("A")
                        .nickname("A").date("2010-1-13").time("7AM")
                        .wineName("123").headcount(4).contents("123")
                        .participants(partySet)
                        .build(),
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("Ab")
                        .nickname("Ab").date("2010-1-13b").time("7AM")
                        .wineName("123b").headcount(45).contents("123b")
                        .participants(partySet)
                        .build(),
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("Ac")
                        .nickname("Ac").date("2010-1-13c").time("7AM")
                        .wineName("123c").headcount(46).contents("123c")
                        .participants(partySet)
                        .build()
        );
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member1));
        given(boardRepository.findAllById(anyLong(), any()))
                .willReturn(new PageImpl<>(boardList));
        //when
        Pageable pageable = PageRequest.of(0, 3);
        Page<BoardSearchResponse> MyBoardPage
                = boardService.searchMyBoardByEmail("AAA", pageable);
        //then
        assertEquals(3, MyBoardPage.getTotalElements());
        assertEquals(1, MyBoardPage.getTotalPages());
    }

    @Test
    void successSearchParticipantInBoard() {
        //given
        String boardIdList = "4,1,2";
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("nick name").avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .inBoardIdList(boardIdList)
                .build();
        Winebar winebar1 = Winebar.builder()
                .id(1L)
                .name("AA")
                .picture("asdfasdf")
                .address("tttttttt")
                .lat(0.0)
                .lnt(0.0)
                .rating(1.1)
                .reviews(1)
                .build();
        HashSet<String> partySet = new HashSet<>();
        partySet.add(member1.getNickname());
        List<Board> boardList = Arrays.asList(
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("A")
                        .nickname("A").date("2010-1-13").time("7AM")
                        .wineName("123").headcount(4).contents("123")
                        .participants(partySet)
                        .build(),
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("Ab")
                        .nickname("Ab").date("2010-1-13b").time("7AM")
                        .wineName("123b").headcount(45).contents("123b")
                        .participants(partySet)
                        .build(),
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("Ac")
                        .nickname("Ac").date("2010-1-13c").time("7AM")
                        .wineName("123c").headcount(46).contents("123c")
                        .participants(partySet)
                        .build()
        );
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member1));
        for(Board board  : boardList) {
            given(boardRepository.existsById(anyLong())).willReturn(true);
            given(boardRepository.findById(anyLong()))
                    .willReturn(Optional.of(board));
        }
        //when
        Pageable pageable = PageRequest.of(0, 3);
        Page<BoardSearchResponse> MyBoardPage
                = boardService.searchParticipantInBoards("AAA");
        //then
        assertEquals(3, MyBoardPage.getTotalElements());
        assertEquals(1, MyBoardPage.getTotalPages());
    }

    @Test
    void successSearchBoardByWinebarId() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("nick name").avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Winebar winebar1 = Winebar.builder()
                .id(1L)
                .name("AA")
                .picture("asdfasdf")
                .address("tttttttt")
                .lat(0.0)
                .lnt(0.0)
                .rating(1.1)
                .reviews(1)
                .build();
        HashSet<String> partySet = new HashSet<>();
        partySet.add(member1.getNickname());
        List<Board> boardList = Arrays.asList(
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("A")
                        .nickname("A").date("2010-1-13").time("7AM")
                        .wineName("123").headcount(4).contents("123")
                        .participants(partySet)
                        .build(),
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("Ab")
                        .nickname("Ab").date("2010-1-13b").time("7AM")
                        .wineName("123b").headcount(45).contents("123b")
                        .participants(partySet)
                        .build(),
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("Ac")
                        .nickname("Ac").date("2010-1-13c").time("7AM")
                        .wineName("123c").headcount(46).contents("123c")
                        .participants(partySet)
                        .build()
        );
        given(wineBarRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(winebar1));
        given(boardRepository.findByWinebar(any(), any()))
                .willReturn(new PageImpl<>(boardList));
        //when
        Pageable pageable = PageRequest.of(0, 3);
        Page<BoardSearchResponse> boardPage
                = boardService.searchBoardByWinebarId(11L, pageable);
        //then
        assertEquals(3, boardPage.getTotalElements());
        assertEquals(1, boardPage.getTotalPages());
    }

    @Test
    @DisplayName("success search with a creator! - board")
    void successSearchBoardByCreator() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("nick name").avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Winebar winebar1 = Winebar.builder()
                .id(1L)
                .name("AA")
                .picture("asdfasdf")
                .address("tttttttt")
                .lat(0.0)
                .lnt(0.0)
                .rating(1.1)
                .reviews(1)
                .build();
        HashSet<String> partySet = new HashSet<>();
        partySet.add(member1.getNickname());
        List<Board> boardList = Arrays.asList(
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("A")
                        .nickname("A").date("2010-1-13").time("7AM")
                        .wineName("123").headcount(4).contents("123")
                        .participants(partySet)
                        .build(),
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("Ab")
                        .nickname("Ab").date("2010-1-13b").time("7AM")
                        .wineName("123b").headcount(45).contents("123b")
                        .participants(partySet)
                        .build(),
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("Ac")
                        .nickname("Ac").date("2010-1-13c").time("7AM")
                        .wineName("123c").headcount(46).contents("123c")
                        .participants(partySet)
                        .build()
        );
        given(memberRepository.findByNickname(anyString()))
                .willReturn(Optional.of(member1));
        given(boardRepository.findByNickname(anyString(), any()))
                .willReturn(new PageImpl<>(boardList));
        //when
        Pageable pageable = PageRequest.of(0, 3);
        Page<BoardSearchResponse> boardPage
                = boardService.searchBoardByCreator("a", pageable);
        //then
        assertEquals(3, boardPage.getTotalElements());
        assertEquals(1, boardPage.getTotalPages());
        assertEquals("A", boardPage.getContent().get(0).getNickname());
        assertEquals("123b", boardPage.getContent().get(1).getWineName());
        assertEquals("AA", boardPage.getContent().get(2).getWinebarName());
    }

    @Test
    @DisplayName("success search with a region! - board")
    void successSearchBoardByRegion() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("nick name").avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Winebar winebar1 = Winebar.builder()
                .id(1L)
                .name("AA")
                .picture("asdfasdf")
                .region("서울")
                .address("tttttttt")
                .lat(0.0)
                .lnt(0.0)
                .rating(1.1)
                .reviews(1)
                .build();
        HashSet<String> partySet = new HashSet<>();
        partySet.add(member1.getNickname());
        List<Board> boardList = Arrays.asList(
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("A")
                        .nickname("A").date("2010-1-13").time("7AM")
                        .wineName("123").headcount(4).contents("123")
                        .participants(partySet).region("경북")
                        .build(),
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("Ab")
                        .nickname("Ab").date("2010-1-13b").time("7AM")
                        .wineName("123b").headcount(45).contents("123b")
                        .participants(partySet).region("경남")
                        .build(),
                Board.builder()
                        .id(1L).member(member1).winebar(winebar1).name("Ac")
                        .nickname("Ac").date("2010-1-13c").time("7AM")
                        .wineName("123c").headcount(46).contents("123c")
                        .participants(partySet).region("호남")
                        .build()
        );
        given(boardRepository.findByRegion(anyString(), any()))
                .willReturn(new PageImpl<>(boardList));
        //when
        Pageable pageable = PageRequest.of(0, 3);
        Page<BoardSearchResponse> boardPage
                = boardService.searchBoardByRegion("a", pageable);
        //then
        assertEquals(3, boardPage.getTotalElements());
        assertEquals(1, boardPage.getTotalPages());
        assertEquals("경북", boardPage.getContent().get(0).getRegion());
        assertEquals("경남", boardPage.getContent().get(1).getRegion());
        assertEquals("호남", boardPage.getContent().get(2).getRegion());
    }

    @Test
    @DisplayName("success update! - board")
    void successUpdateBoard() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("nick name").avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Winebar winebar1 = Winebar.builder()
                .id(16L)
                .name("AA")
                .picture("asdfasdf")
                .address("tttttttt")
                .lat(0.0)
                .lnt(0.0)
                .rating(1.1)
                .reviews(1)
                .build();
        HashSet<String> partySet = new HashSet<>();
        partySet.add(member1.getNickname());
        Board targetBoard = Board.builder()
                .id(1L).member(member1).winebar(winebar1).name("A").nickname("A")
                .date("2010-1-13").wineName("123").headcount(4).contents("123")
                .time("7AM").participants(partySet)
                .build();
        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(targetBoard));
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member1));
        given(boardRepository.save(any())).willReturn(targetBoard);

        //when
        BoardUpdateRequest request = BoardUpdateRequest.builder()
                .memberId(1L)
                .winebarId(1L)
                .name("name1")
                .nickname("creator!")
                .date("2020-1-1")
                .time("7AM")
                .wineName("AAA")
                .headcount(5)
                .contents("empty at all")
                .build();
        BoardUpdateResponse board = boardService
                .updateBoard("AAA", 13L, request);
        //then
        assertEquals(5, board.getHeadcount());
        assertEquals("creator!", board.getNickname());
        assertEquals("AAA", board.getWineName());
    }

    @Test
    void successParticipantInBoard() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("nick name").avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Member member2 = Member.builder()
                .id(10L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("creator!").avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Winebar winebar1 = Winebar.builder()
                .id(16L)
                .name("AA")
                .picture("asdfasdf")
                .address("tttttttt")
                .lat(0.0)
                .lnt(0.0)
                .rating(1.1)
                .reviews(1)
                .build();
        Board targetBoard = Board.builder()
                .id(1L).member(member1).winebar(winebar1).name("A").nickname("A")
                .date("2010-1-13").wineName("123").headcount(4).contents("123")
                .time("7AM").participants(new HashSet<>())
                .build();
        targetBoard.insertBoardParticipant(targetBoard,targetBoard.getNickname());
        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(targetBoard));
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member2));
        given(memberRepository.save(any())).willReturn(member2);
        given(boardRepository.save(any())).willReturn(targetBoard);
        //when
        BoardParticipantResponse board = boardService
                .participantInBoard("AAA", 1L);
        //then
        assertEquals(4, board.getHeadcount());
        assertEquals("A", board.getNickname());
        assertEquals("AA", board.getWinebarName());
        assertEquals(2, board.getParticipants().size());
        assertTrue(board.getParticipants().contains("creator!"));
    }

    @Test
    void successParticipantOutBoard() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("nick name").avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Member member2 = Member.builder()
                .id(10L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("creator!").avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Winebar winebar1 = Winebar.builder()
                .id(16L)
                .name("AA")
                .picture("asdfasdf")
                .address("tttttttt")
                .lat(0.0)
                .lnt(0.0)
                .rating(1.1)
                .reviews(1)
                .build();
        Board targetBoard = Board.builder()
                .id(1L).member(member1).winebar(winebar1).name("A").nickname("A")
                .date("2010-1-13").wineName("123").headcount(4).contents("123")
                .time("7AM").participants(new HashSet<>())
                .build();
        targetBoard.deleteBoardParticipant(targetBoard,targetBoard.getNickname());
        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(targetBoard));
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member2));
        given(memberRepository.save(any())).willReturn(member2);
        given(boardRepository.save(any())).willReturn(targetBoard);
        //when
        BoardParticipantResponse board = boardService
                .participantInBoard("AAA", 1L);
        //then
        assertEquals(4, board.getHeadcount());
        assertEquals("A", board.getNickname());
        assertEquals("AA", board.getWinebarName());
        assertEquals(1, board.getParticipants().size());
        assertTrue(board.getParticipants().contains("creator!"));
    }

    @Test
    @DisplayName("success delete! - board")
    void successDeleteBoard() {
        //given
        Member member1 = Member.builder()
                .id(13L)
                .email("asdf@gmail.com")
                .password("asdfasdfasdf")
                .nickname("nick name").avatar("asdfasdfasdf")
                .mbti("istp")
                .role(Role.MEMBER).oauthType("fasdf")
                .body(1).dry(1).tannin(1).acidity(1)
                .build();
        Winebar winebar1 = Winebar.builder()
                .id(16L)
                .name("AA")
                .picture("asdfasdf")
                .address("tttttttt")
                .lat(0.0)
                .lnt(0.0)
                .rating(1.1)
                .reviews(1)
                .build();
        Board targetBoard = Board.builder()
                .id(1L).member(member1).winebar(winebar1).name("A").nickname("A")
                .date("2010-1-13").wineName("123").headcount(4).contents("123")
                .time("7AM")
                .build();
        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(targetBoard));
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member1));
        ArgumentCaptor<Board> captor = ArgumentCaptor.forClass(Board.class);
        //when
        boardService.deleteBoard("AAA", 123L);
        //then
        verify(boardRepository, times(1)).save(captor.capture());
        assertEquals(1, captor.getValue().getId());
    }

    @Test
    @DisplayName("fail create : Duplication Name ! - board")
    void failedCreateBoardDuplicationName() {
        //given
        given(boardRepository.existsByName(any())).willReturn(true);
        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> boardService.creatBoard("AAA", 1L,
                        BoardCreateRequest.builder()
                                .memberId(1L)
                                .name("name")
                                .nickname("creator")
                                .date("2020-1-1")
                                .time("7AM")
                                .wineName("winename")
                                .headcount(1)
                                .contents("cont")
                                .build()));
        //then
        assertEquals("already exists", exception.getMessage());
    }

    @Test
    @DisplayName("fail create : Not Found Winebar ! - board")
    void failedCreateBoardWinebarNotFound() {
        //given
        given(wineBarRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> boardService.creatBoard("aaa", 1L,
                        BoardCreateRequest.builder()
                                .memberId(1L)
                                .name("name")
                                .nickname("creator")
                                .date("2020-1-1")
                                .time("7AM")
                                .wineName("winename")
                                .headcount(1)
                                .contents("cont")
                                .build()));
        //then
        assertEquals("winebar does not exist", exception.getMessage());
    }

    @Test
    void failedSearchBoardDetailNotFoundBoard() {
        //given
        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> boardService.searchBoardDetail(1L));
        //then
        assertEquals("the board does not exist", exception.getMessage());
    }

    @Test
    void failedSearchMyBoardNotFoundUser() {
        //given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());
        //when
        Pageable pageable = PageRequest.of(0, 3);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> boardService.searchMyBoardByEmail("AAA", pageable));
        //then
        assertEquals("user does not exist", exception.getMessage());
    }

    @Test
    void failSearchBoardNotFoundWinebar() {
        //given
        given(wineBarRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        Pageable pageable = PageRequest.of(0, 3);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> boardService.searchBoardByWinebarId(15L, pageable));
        //then
        assertEquals("the winebar does not exist", exception.getMessage());

    }

    @Test
    @DisplayName("fail search : Not Found Wine ! - board")
    void failedSearchBoardWineNotFound() {
        //given
        given(wineRepository.findByName(anyString())).willReturn(Optional.empty());
        //when
        Pageable pageable = PageRequest.of(0, 3);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> boardService.searchBoardByWineName("A", pageable));
        //then
        assertEquals("the wine does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("fail search : Not Found User ! - board")
    void failedSearchBoardMemberNotFound() {
        //given
        given(memberRepository.findByNickname(anyString())).willReturn(Optional.empty());
        //when
        Pageable pageable = PageRequest.of(0, 3);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> boardService.searchBoardByCreator("A", pageable));
        //then
        assertEquals("user does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("fail update- board")
    void failedUpdateBoardNotFoundBoard() {
        //given
        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> boardService.updateBoard("AAA", 1L,
                        BoardUpdateRequest.builder()
                                .memberId(1L)
                                .name("name")
                                .nickname("creator")
                                .date("2020-1-1")
                                .time("7AM")
                                .wineName("winename")
                                .headcount(1)
                                .contents("cont")
                                .build()));
        //then
        assertEquals("the board does not exist", exception.getMessage());
    }

    @Test
    void failedParticipantInBoardNotFoundBoard() {
        //given
        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> boardService.participantInBoard("AAA", 1L));
        //then
        assertEquals("the board does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("fail delete! - board")
    void failedDeleteBoardNotFoundBoard() {
        //given
        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> boardService.deleteBoard("AAA", 1L));
        //then
        assertEquals("the board does not exist", exception.getMessage());
    }
}