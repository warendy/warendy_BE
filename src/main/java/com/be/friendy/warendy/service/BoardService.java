package com.be.friendy.warendy.service;

import com.be.friendy.warendy.domain.dto.request.BoardCreateRequest;
import com.be.friendy.warendy.domain.entity.Board;
import com.be.friendy.warendy.domain.repository.BoardRepository;
import com.be.friendy.warendy.domain.repository.MemberRepository;
import com.be.friendy.warendy.domain.repository.WineBarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final WineBarRepository wineBarRepository;

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
    public Board createBoard(Long winebarId, BoardCreateRequest board) {


        boolean exists = this.boardRepository.existsByName(board.getName());
        if (exists) {
            throw new RuntimeException("already exists");
        }

        var user = this.memberRepository.findById(board.getMemberId())
                .orElseThrow(() -> new RuntimeException("user does not exists"));

        board.setWineBarId(winebarId);
        board.setCreator(user.getNickname());

        return this.boardRepository.save(board.toEntity());
    }

    @Transactional
    public List<Board> searchBoard() {
        return boardRepository.findAll();
    }

    @Transactional
    public List<Board> searchBoardByBoardName(String name) {

        return boardRepository.findAllByNameAndDeletedAtIsNull(name);
    }

    @Transactional
    public List<Board> searchBoardByWineName(String wineName) {

        return boardRepository.findAllByWineNameAndDeletedAtIsNull(wineName);
    }

    @Transactional
    public List<Board> searchBoardByWineBarName(String wineBarName) {

        var wineBar = this.wineBarRepository.findByName(wineBarName)
                .orElseThrow(() -> new RuntimeException("the wine does not exists"));

        return boardRepository.findAllByWineBarIdAndDeletedAtIsNull(wineBar.getId());
    }

    @Transactional
    public List<Board> searchBoardByCreator(String creator) {
        return boardRepository.findAllByCreatorAndDeletedAtIsNull(creator);
    }

    @Transactional
    public List<Board> searchBoardByDate(LocalDate date) {
        String dateToStr = String.valueOf(date);
        return boardRepository.findAllByDateAndDeletedAtIsNull(dateToStr);
    }

    @Transactional(readOnly = false)
    public Board updateBoard(Long id, BoardCreateRequest boardCreateRequest) {
        var nowboard = this.boardRepository.findBoardByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("the board does not exists"));

        List<BoardCreateRequest> list = new ArrayList<>();
        list.add(boardCreateRequest);
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName() != null) {
                nowboard.setName(list.get(i).getName());
                flag = true;
            }
            if (list.get(i).getDate() != null) {
                nowboard.setDate(list.get(i).getDate());
                flag = true;
            }
            if (list.get(i).getWineName() != null) {
                nowboard.setWineName(list.get(i).getWineName());
                flag = true;
            }
            if (list.get(i).getHeadcount() != nowboard.getHeadcount()) {
                nowboard.setHeadcount(list.get(i).getHeadcount());
                flag = true;
            }
            if (list.get(i).getContents() != null) {
                nowboard.setContents(list.get(i).getContents());
                flag = true;
            }
        }
        if (flag) {
            nowboard.setModifiedAt(LocalDateTime.now());
        }
        return this.boardRepository.save(nowboard);
    }

    @Transactional(readOnly = false)
    public void deleteBoard(Long boardId) {
        var board = this.boardRepository.findBoardByIdAndDeletedAtIsNull(boardId)
                .orElseThrow(() -> new RuntimeException());
        board.setDeletedAt(LocalDateTime.now());
    }

}
