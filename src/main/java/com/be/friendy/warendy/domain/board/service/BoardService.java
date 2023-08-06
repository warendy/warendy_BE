package com.be.friendy.warendy.domain.board.service;

import com.be.friendy.warendy.domain.board.dto.request.BoardCreateRequest;
import com.be.friendy.warendy.domain.board.dto.request.BoardUpdateRequest;
import com.be.friendy.warendy.domain.board.dto.response.BoardCreateResponse;
import com.be.friendy.warendy.domain.board.dto.response.BoardSearchDetailResponse;
import com.be.friendy.warendy.domain.board.dto.response.BoardSearchResponse;
import com.be.friendy.warendy.domain.board.entity.Board;
import com.be.friendy.warendy.domain.board.repository.BoardRepository;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import com.be.friendy.warendy.domain.wine.repository.WineRepository;
import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import com.be.friendy.warendy.domain.winebar.repository.WinebarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final WinebarRepository wineBarRepository;
    private final WineRepository wineRepository;

    public BoardCreateResponse creatBoard(
            String email, Long winebarId, BoardCreateRequest createRequest) {
        boolean exists = boardRepository.existsByName(createRequest.getName());
        if (exists) {
            throw new RuntimeException("already exists");
        }

        Winebar winebar = wineBarRepository
                .findById(winebarId)
                .orElseThrow(() -> new RuntimeException("winebar does not exist"));

        Member memberById = memberRepository.findById(createRequest.getMemberId())
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        if (!memberById.equals(memberByEmail)) {
            throw new RuntimeException("check the user information");
        }

        return BoardCreateResponse.fromEntity(boardRepository.save(
                Board.builder()
                        .member(memberById)
                        .winebar(winebar)
                        .name(createRequest.getName())
                        .creator(createRequest.getCreator())
                        .date(createRequest.getDate())
                        .wineName(createRequest.getWineName())
                        .headcount(createRequest.getHeadcount())
                        .contents(createRequest.getContents())
                        .build()
        ));
    }

    public Page<BoardSearchResponse> searchBoard(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .map(BoardSearchResponse::fromEntity);
    }

    public BoardSearchDetailResponse searchBoardDetail(Long boardId) {
        return boardRepository.findById(boardId)
                .map(BoardSearchDetailResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("the board does not exist"));
    }

    public Page<BoardSearchResponse> searchBoardByBoardName(
            String name, Pageable pageable
    ) {
        return boardRepository.findByName(name, pageable)
                .map(BoardSearchResponse::fromEntity);
    }

    public Page<BoardSearchResponse> searchBoardByWineName(
            String wineName, Pageable pageable
    ) {
        Wine wine = wineRepository.findByName(wineName)
                .orElseThrow(() -> new RuntimeException("the wine does not exist"));

        return boardRepository.findByWineName(wine.getName(), pageable)
                .map(BoardSearchResponse::fromEntity);
    }

    public Page<BoardSearchResponse> searchBoardByWinebarName(
            String winebarName, Pageable pageable
    ) {
        Winebar winebar = wineBarRepository.findByName(winebarName)
                .orElseThrow(() -> new RuntimeException("the winebar does not exist"));

        return boardRepository.findByWinebar(winebar, pageable)
                .map(BoardSearchResponse::fromEntity);
    }

    public Page<BoardSearchResponse> searchBoardByCreator(
            String creator, Pageable pageable
    ) {
        Member member = memberRepository.findByNickname(creator)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        return boardRepository.findByCreator(member.getNickname(), pageable)
                .map(BoardSearchResponse::fromEntity);
    }


    public Page<BoardSearchResponse> searchBoardByDate(
            LocalDate date, Pageable pageable
    ) {
        String dateToStr = String.valueOf(date);
        return boardRepository.findByDate(dateToStr, pageable)
                .map(BoardSearchResponse::fromEntity);
    }

    public Page<BoardSearchResponse> searchBoardByTime(
            String time, Pageable pageable
    ) {
        return boardRepository.findByTime(time, pageable)
                .map(BoardSearchResponse::fromEntity);
    }

    public Board updateBoard(String email, Long boardId, BoardUpdateRequest request) {

        Board nowBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("the board does not exist"));
        nowBoard.updateBoardInfo(request);

        Member memberById = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        if (!memberById.equals(memberByEmail)) {
            throw new RuntimeException("check the user information");
        }

        return boardRepository.save(nowBoard);
    }

    public void deleteBoard(String email, Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("the board does not exist"));

        Member memberById = memberRepository.findById(board.getMember().getId())
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        if (!memberById.equals(memberByEmail)) {
            throw new RuntimeException("check the user information");
        }

        boardRepository.save(board); // test 코드에서 확인 위한
        boardRepository.delete(board);
    }

}
