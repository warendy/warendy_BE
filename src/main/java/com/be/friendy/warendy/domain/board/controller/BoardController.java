package com.be.friendy.warendy.domain.board.controller;


import com.be.friendy.warendy.domain.board.dto.request.BoardCreateRequest;
import com.be.friendy.warendy.domain.board.dto.request.BoardUpdateRequest;
import com.be.friendy.warendy.domain.board.dto.response.BoardCreateResponse;
import com.be.friendy.warendy.domain.board.dto.response.BoardSearchResponse;
import com.be.friendy.warendy.domain.board.entity.Board;
import com.be.friendy.warendy.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/winebars/{winebarId}")
    public ResponseEntity<BoardCreateResponse> boardCreate(
            @PathVariable Long winebarId,
            @RequestBody BoardCreateRequest request
    ) {
        return ResponseEntity.ok(boardService.creatBoard(winebarId, request));
    }

    @GetMapping("")
    public ResponseEntity<Page<BoardSearchResponse>> boardSearch(
            @PageableDefault(size = 3) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService.searchBoard(pageable));
    }

    @GetMapping("/board-name")
    public ResponseEntity<Page<BoardSearchResponse>> boardSearchByBoardName(
            @RequestParam String boardName,
            @PageableDefault(size = 3) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService
                .searchBoardByBoardName(boardName, pageable));
    }

    @GetMapping("/wine-name")
    public ResponseEntity<Page<BoardSearchResponse>> boardSearchByWineName(
            @RequestParam String wineName,
            @PageableDefault(size = 3) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService
                .searchBoardByWineName(wineName, pageable));
    }

    @GetMapping("/winebar-name")
    public ResponseEntity<Page<BoardSearchResponse>> boardSearchByWinebarName(
            @RequestParam String winebarName,
            @PageableDefault(size = 3) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService
                .searchBoardByWinebarName(winebarName, pageable));
    }

    @GetMapping("/creator")
    public ResponseEntity<Page<BoardSearchResponse>> boardSearchByCreator(
            @RequestParam String creator,
            @PageableDefault(size = 3) Pageable pageable
    ) {
        return ResponseEntity.ok(
                boardService.searchBoardByCreator(creator, pageable)
        );
    }

    @GetMapping("/date")
    public ResponseEntity<Page<BoardSearchResponse>> boardSearchByDate(
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

    @PatchMapping("/{boardId}")
    public ResponseEntity<Board> boardUpdatePATCH(
            @PathVariable Long boardId,
            @RequestBody BoardUpdateRequest boardUpdateRequest
    ) {
        return ResponseEntity.ok(
                boardService.updateBoard(boardId, boardUpdateRequest));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<Board> boardUpdatePUT(
            @PathVariable Long boardId,
            @RequestBody BoardUpdateRequest boardUpdateRequest
    ) {
        return ResponseEntity.ok(
                boardService.updateBoard2(boardId, boardUpdateRequest));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> boardDelete(
            @PathVariable Long boardId
    ) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok("board is deleted!");
    }

}
