package com.be.friendy.warendy.domain.board.controller;


import com.be.friendy.warendy.domain.board.dto.request.BoardCreateRequest;
import com.be.friendy.warendy.domain.board.dto.request.BoardUpdateRequest;
import com.be.friendy.warendy.domain.board.dto.response.BoardCreateResponse;
import com.be.friendy.warendy.domain.board.entity.Board;
import com.be.friendy.warendy.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/winebars/{winebar_id}/ver1")
    public ResponseEntity<BoardCreateResponse> boardCreate(
            @PathVariable Long winebar_id,
            @RequestBody BoardCreateRequest request
    ) {
        return ResponseEntity.ok(boardService.createBoard(winebar_id, request));
    }

    @PostMapping("/winebars/{winebar_id}")
    public ResponseEntity<BoardCreateResponse> boardCreate2(
            @PathVariable Long winebar_id,
            @RequestBody BoardCreateRequest request
    ) {
        return ResponseEntity.ok(boardService.creatBoard2(winebar_id, request));
    }

    @GetMapping("")
    public ResponseEntity<List> boardSearch(
            @PageableDefault(size = 3) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService.searchBoard(pageable));
    }

    @GetMapping("/board_name")
    public ResponseEntity<List> boardSearchByBoardName(
            @RequestParam String boardName,
            @PageableDefault(size = 3) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService
                .searchBoardByBoardName(boardName, pageable));
    }

    @GetMapping("/wine_name")
    public ResponseEntity<List> boardSearchByWineName(
            @RequestParam String wineName,
            @PageableDefault(size = 3) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService
                .searchBoardByWineName(wineName, pageable));
    }

    @GetMapping("/winebar_name")
    public ResponseEntity<List> boardSearchByWindBarName(
            @RequestParam String winebarName,
            @PageableDefault(size = 3) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService
                .searchBoardByWineBarName(winebarName, pageable));
    }

    @GetMapping("/creator")
    public ResponseEntity<List> boardSearchByCreator(
            @RequestParam String creator,
            @PageableDefault(size = 3) Pageable pageable
    ) {
        return ResponseEntity.ok(
                boardService.searchBoardByCreator(creator, pageable)
        );
    }

    @GetMapping("/date")
    public ResponseEntity<List> boardSearchByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault(size = 3) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService.searchBoardByDate(date, pageable));
    }

    @GetMapping("/region")
    public ResponseEntity<?> boardSearchByRegion(
    ) {
        return null;
    }

    @PatchMapping("/{board_id}")
    public ResponseEntity<Board> boardUpdate_PATCH(
            @PathVariable Long board_id,
            @RequestBody BoardUpdateRequest boardUpdateRequest
    ) {
        return ResponseEntity.ok(
                boardService.updateBoard(board_id, boardUpdateRequest));
    }

    @PutMapping("/{board_id}")
    public ResponseEntity<Board> boardUpdate_PUT(
            @PathVariable Long board_id,
            @RequestBody BoardUpdateRequest boardUpdateRequest
    ) {
        return ResponseEntity.ok(
                boardService.updateBoard2(board_id, boardUpdateRequest));
    }

    @DeleteMapping("/{board_id}")
    public ResponseEntity<String> boardDelete(
            @PathVariable Long board_id
    ) {
        boardService.deleteBoard(board_id);
        return ResponseEntity.ok("board is deleted!");
    }

}
