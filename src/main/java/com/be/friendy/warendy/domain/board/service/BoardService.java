package com.be.friendy.warendy.domain.board.service;

import com.be.friendy.warendy.domain.board.dto.request.BoardCreateRequest;
import com.be.friendy.warendy.domain.board.dto.request.BoardUpdateRequest;
import com.be.friendy.warendy.domain.board.entity.Board;
import com.be.friendy.warendy.domain.board.repository.BoardRepository;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.member.repository.MemberRepository;
import com.be.friendy.warendy.domain.winebar.entity.WineBar;
import com.be.friendy.warendy.domain.winebar.repository.WineBarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final WineBarRepository wineBarRepository;

    public Board createBoard(Long winebarId, BoardCreateRequest createRequest) {


        boolean exists = boardRepository.existsByName(createRequest.getName());
        if (exists) {
            throw new RuntimeException("already exists");
        }

        Member member = memberRepository.findById(createRequest.getMemberId())
                .orElseThrow(() -> new RuntimeException("user does not exists"));
        WineBar wineBar = wineBarRepository.findById(winebarId).
                orElseThrow(() -> new RuntimeException("the bar does not exists"));;

        return boardRepository.save(createRequest.toEntity(wineBar,member));
    }

    public BoardCreateRequest creatBoard2(Long winebarId, BoardCreateRequest createRequest) {
        boolean exists = boardRepository.existsByName(createRequest.getName());
        if (exists) {
            throw new RuntimeException("already exists");
        }

        Member member = memberRepository.findById(createRequest.getMemberId())
                .orElseThrow(() -> new RuntimeException("user does not exists"));

        Board board = boardRepository.save(
                Board.builder()
                        .member(member)
                        .wineBar(wineBarRepository
                                .findById(winebarId)
                                .orElseThrow(() -> new RuntimeException()))
                        .name(createRequest.getName())
                        .creator(createRequest.getCreator())
                        .date(createRequest.getDate())
                        .wineName(createRequest.getWineName())
                        .headcount(createRequest.getHeadcount())
                        .contents(createRequest.getContents())
                        .build()
        );
        return BoardCreateRequest.fromEntity(board);
    }

    public List<Board> searchBoard() {
        return boardRepository.findAll();
    }

    public List<Board> searchBoardByBoardName(String name) {

        return boardRepository.findAllByNameAndDeletedAtIsNull(name);
    }

    public List<Board> searchBoardByWineName(String wineName) {

        return boardRepository.findAllByWineNameAndDeletedAtIsNull(wineName);
    }

    public List<Board> searchBoardByWineBarName(String wineBarName) {

        var wineBar = this.wineBarRepository.findByName(wineBarName)
                .orElseThrow(() -> new RuntimeException("the wine does not exists"));

        return boardRepository.findAllByWineBarIdAndDeletedAtIsNull(wineBar.getId());
    }

    public List<Board> searchBoardByCreator(String creator) {
        return boardRepository.findAllByCreatorAndDeletedAtIsNull(creator);
    }

    public List<Board> searchBoardByDate(LocalDate date) {
        String dateToStr = String.valueOf(date);
        return boardRepository.findAllByDateAndDeletedAtIsNull(dateToStr);
    }

    public Board updateBoard(Long id, BoardUpdateRequest boardUpdateRequest) {
        Board nowBoard = boardRepository.findByIdAndDeletedAtIsNull(id)
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
        Board nowBoard = boardRepository.findByIdAndDeletedAtIsNull(boardId)
                .orElseThrow(() -> new RuntimeException("the board does not exists"));;
        nowBoard.updateBoardInfo(request);
        return boardRepository.save(nowBoard);
    }

    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findByIdAndDeletedAtIsNull(boardId)
                .orElseThrow(() -> new RuntimeException("The board does not exist"));
        boardRepository.delete(board);
    }

}
