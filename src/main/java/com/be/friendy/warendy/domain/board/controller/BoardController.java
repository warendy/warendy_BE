package com.be.friendy.warendy.domain.board.controller;


import com.be.friendy.warendy.config.jwt.TokenProvider;
import com.be.friendy.warendy.domain.board.dto.request.BoardCreateRequest;
import com.be.friendy.warendy.domain.board.dto.request.BoardUpdateRequest;
import com.be.friendy.warendy.domain.board.dto.response.*;
import com.be.friendy.warendy.domain.board.service.BoardService;
import jakarta.validation.Valid;
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
    private final TokenProvider tokenProvider;

    @PostMapping("/winebars")
    public ResponseEntity<BoardCreateResponse> boardCreate(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(value = "winebar-id") Long winebarId,
            @Valid @RequestBody BoardCreateRequest request
    ) {
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok(boardService.creatBoard(email, winebarId, request));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<BoardSearchResponse>> boardSearch(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService.searchBoard(pageable));
    }

    @GetMapping("/{board-id}/detail")
    public ResponseEntity<BoardSearchDetailResponse> boardSearchDetail(
            @PathVariable("board-id") Long boardId
    ) {
        return ResponseEntity.ok(boardService.searchBoardDetail(boardId));
    }

    @GetMapping("")
    public ResponseEntity<Page<BoardSearchResponse>> boardSearchMyBoard(
            @RequestHeader("Authorization") String authorizationHeader,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok(boardService.searchMyBoardByEmail(email, pageable));
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

    @GetMapping("/winebar-id")
    public ResponseEntity<Page<BoardSearchResponse>> boardSearchByWinebarId(
            @RequestParam(value = "winebar-id") Long winebarId,
            @PageableDefault(size = 3) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService
                .searchBoardByWinebarId(winebarId, pageable));
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

    @GetMapping("/time")
    public ResponseEntity<Page<BoardSearchResponse>> boardSearchByTime(
            @RequestParam String time,
            @PageableDefault(size = 3) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService.searchBoardByTime(time, pageable));
    }

    @GetMapping("/region")
    public ResponseEntity<?> boardSearchByRegion(
    ) {
        return null;
    }

    @PutMapping("/{board-id}")
    public ResponseEntity<BoardUpdateResponse> boardUpdate(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable(value = "board-id") Long boardId,
            @RequestBody BoardUpdateRequest boardUpdateRequest
    ) {
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok(
                boardService.updateBoard(email, boardId, boardUpdateRequest));
    }

    @PutMapping("/participants")
    public ResponseEntity<BoardParticipantResponse> boardParticipant(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(value = "board-id") Long boardId
    ) {
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok(boardService.participantInBoard(email, boardId));
    }


    @DeleteMapping("/{board-id}")
    public ResponseEntity<String> boardDelete(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable(value = "board-id") Long boardId
    ) {
        String email = tokenProvider.getEmailFromToken(authorizationHeader);
        boardService.deleteBoard(email, boardId);
        return ResponseEntity.ok("board is deleted!");
    }

}
