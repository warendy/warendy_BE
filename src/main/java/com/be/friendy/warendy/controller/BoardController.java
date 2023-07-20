package com.be.friendy.warendy.controller;


import com.be.friendy.warendy.domain.dto.request.BoardCreateRequest;
import com.be.friendy.warendy.domain.entity.Board;
import com.be.friendy.warendy.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/board/{winebar_id}")
    public ResponseEntity<?> boardCreate(
            @PathVariable Long winebar_id,
            @RequestBody BoardCreateRequest request
    ) {
        var result = this.boardService.createBoard(winebar_id, request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/board")
    public ResponseEntity<?> boardSearch(
    ) {
        var result = this.boardService.searchBoard();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/board/board_name")
    public ResponseEntity<?> boardSearchByBoardName(
            @RequestParam String name
    ) {
        var result = this.boardService.searchBoardByBoardName(name);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/board/wine_name")
    public ResponseEntity<?> boardSearchByWineName(
            @RequestParam String wineName
    ) {
        List<Board> boardList = this.boardService.searchBoardByWineName(wineName);
        return ResponseEntity.ok(boardList);
    }

    @GetMapping("/board/winebar_name")
    public ResponseEntity<?> boardSearchByWindBarName(
            @RequestParam String wineBarName
    ) {
        List<Board> boardList = this.boardService.searchBoardByWineBarName(wineBarName);
        return ResponseEntity.ok(boardList);
    }

    @GetMapping("/board/date")
    public ResponseEntity<?> boardSearchByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<Board> boardList = this.boardService.searchBoardByDate(date);
        return ResponseEntity.ok(boardList);
    }

    @GetMapping("/board/creator")
    public ResponseEntity<?> boardSearchByCreator(
            @RequestParam String creator
    ) {
        List<Board> boardList = this.boardService.searchBoardByCreator(creator);
        return ResponseEntity.ok(boardList);
    }

//    @GetMapping("/board/")
//    public ResponseEntity<?> boardSearchByRegion(
//    ) {
//
//    }

    @PatchMapping("/board/{board_id}")
    public ResponseEntity<?> boardUpdate(
            @PathVariable Long board_id,
            @RequestBody BoardCreateRequest boardCreateRequest
    ) {
        Board board = this.boardService.updateBoard(board_id, boardCreateRequest);
        return ResponseEntity.ok(board);
    }

    @DeleteMapping("/board/{board_id}")
    public void boardDelete (
            @PathVariable Long board_id
    ){
        this.boardService.deleteBoard(board_id);
    }

}
