package com.be.friendy.warendy.domain.board.service;

import com.be.friendy.warendy.domain.board.dto.request.BoardCreateRequest;
import com.be.friendy.warendy.domain.board.dto.request.BoardUpdateRequest;
import com.be.friendy.warendy.domain.board.dto.response.BoardCreateResponse;
import com.be.friendy.warendy.domain.board.dto.response.BoardSearchResponse;
import com.be.friendy.warendy.domain.board.entity.Board;
import com.be.friendy.warendy.domain.board.repository.BoardRepository;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import com.be.friendy.warendy.domain.winebar.repository.WinebarRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final WinebarRepository wineBarRepository;

    public BoardCreateResponse creatBoard(
            Long winebarId, BoardCreateRequest createRequest) {
        boolean exists = boardRepository.existsByName(createRequest.getName());
        if (exists) {
            throw new RuntimeException("already exists");
        }

        Winebar winebar = wineBarRepository
                .findById(winebarId)
                .orElseThrow(() -> new RuntimeException("winebar does not exists"));

        Member member = memberRepository.findById(createRequest.getMemberId())
                .orElseThrow(() -> new RuntimeException("user does not exists"));

        return BoardCreateResponse.fromEntity(boardRepository.save(
                Board.builder()
                        .member(member)
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

    public Page<BoardSearchResponse> searchBoardByBoardName(
            String name, Pageable pageable
    ) {
        return boardRepository.findByName(name, pageable)
                .map(BoardSearchResponse::fromEntity);
    }

    public Page<BoardSearchResponse> searchBoardByWineName(
            String wineName, Pageable pageable
    ) {
        return boardRepository.findByWineName(wineName, pageable)
                .map(BoardSearchResponse::fromEntity);
    }

    public Page<BoardSearchResponse> searchBoardByWinebarName(
            String winebarName, Pageable pageable
    ) {
        Winebar winebar = this.wineBarRepository.findByName(winebarName)
                .orElseThrow(() -> new RuntimeException("the wine does not exists"));

        return boardRepository.findByWinebar(winebar, pageable)
                .map(BoardSearchResponse::fromEntity);
    }

    public Page<BoardSearchResponse> searchBoardByCreator(
            String creator, Pageable pageable
    ) {
        return boardRepository.findByCreator(creator, pageable)
                .map(BoardSearchResponse::fromEntity);
    }


    public Page<BoardSearchResponse> searchBoardByDate(
            LocalDate date, Pageable pageable
    ) {
        String dateToStr = String.valueOf(date);
        return boardRepository.findByDate(dateToStr, pageable)
                .map(BoardSearchResponse::fromEntity);
    }

    public Board updateBoard(Long id, BoardUpdateRequest boardUpdateRequest) {
        Board nowBoard = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("the board does not exists"));

        updateFunction(boardUpdateRequest, nowBoard);
        return boardRepository.save(nowBoard);
    }

    private static void updateFunction(
            BoardUpdateRequest boardUpdateRequest, Board nowBoard) {
        List<BoardUpdateRequest> list = new ArrayList<>();
        list.add(boardUpdateRequest);

        for (BoardUpdateRequest request : list) {
            if (request.getName() != null) {
                nowBoard.setName(request.getName());
            }
            if (request.getDate() != null) {
                nowBoard.setDate(request.getDate());
            }
            if (request.getWineName() != null) {
                nowBoard.setWineName(request.getWineName());
            }
            if (request.getHeadcount() != nowBoard.getHeadcount()) {
                nowBoard.setHeadcount(request.getHeadcount());
            }
            if (request.getContents() != null) {
                nowBoard.setContents(request.getContents());
            }
        }
    }

    public Board updateBoard2(Long boardId, BoardUpdateRequest request) {
        Board nowBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("the board does not exists"));
        ;
        nowBoard.updateBoardInfo(request);
        return boardRepository.save(nowBoard);
    }

    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("The board does not exist"));
        boardRepository.delete(board);
    }

}
