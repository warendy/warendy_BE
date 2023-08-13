package com.be.friendy.warendy.domain.board.service;

import com.be.friendy.warendy.domain.board.dto.request.BoardCreateRequest;
import com.be.friendy.warendy.domain.board.dto.request.BoardUpdateRequest;
import com.be.friendy.warendy.domain.board.dto.response.*;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final WinebarRepository wineBarRepository;
    private final WineRepository wineRepository;

    @Transactional(timeout = 30) // 30초
    public BoardCreateResponse creatBoard(
            String email, Long winebarId, BoardCreateRequest createRequest) {
        boolean exists = boardRepository.existsByName(createRequest.getName());
        if (exists) {
            throw new RuntimeException("already exists");
        }

        Winebar winebar = wineBarRepository
                .findById(winebarId)
                .orElseThrow(() -> new RuntimeException("winebar does not exist"));

        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        HashSet<String> partySet = new HashSet<>();
        partySet.add(memberByEmail.getNickname());

        return BoardCreateResponse.fromEntity(boardRepository.save(
                Board.builder()
                        .member(memberByEmail)
                        .winebar(winebar)
                        .name(createRequest.getName())
                        .nickname(createRequest.getNickname())
                        .date(createRequest.getDate())
                        .time(createRequest.getTime())
                        .region(createRequest.getRegion())
                        .wineName(createRequest.getWineName())
                        .headcount(createRequest.getHeadcount())
                        .contents(createRequest.getContents())
                        .participants(partySet)
                        .build()
        ));
    }

    public Page<BoardSearchResponse> searchBoard(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .map(BoardSearchResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public BoardSearchDetailResponse searchBoardDetail(Long boardId) {
        return boardRepository.findById(boardId)
                .map(BoardSearchDetailResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("the board does not exist"));
    }

    public Page<BoardSearchResponse> searchMyBoardByEmail(
            String email, Pageable pageable
    ) {
        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        return boardRepository.findByNickname(memberByEmail.getNickname(), pageable)
                .map(BoardSearchResponse::fromEntity);
    }

    public Page<BoardSearchResponse> searchParticipantInBoards(String email
    ) {
        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        if(memberByEmail.getInBoardIdList() == null) {
            return new PageImpl<>(new ArrayList<>());
        }

        String[] boardIdList = memberByEmail.getInBoardIdList().split(",");
        List<Board> boardInPartyList = new ArrayList<>();
        for (String boardId : boardIdList) {
            Long id = Long.valueOf(boardId);
            if (!boardRepository.existsById(id)) {
                continue;
            }
            Board board = boardRepository.findById(id).orElse(new Board());
            boardInPartyList.add(board);
        }
        List<BoardSearchResponse> boardSearchResponseList
                = boardInPartyList.stream()
                .map(BoardSearchResponse::fromEntity)
                .toList();
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        return new PageImpl<>(boardSearchResponseList, pageable, boardSearchResponseList.size());
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

    public Page<BoardSearchResponse> searchBoardByWinebarId(
            Long winebarId, Pageable pageable
    ) {
        Winebar winebar = wineBarRepository.findById(winebarId)
                .orElseThrow(() -> new RuntimeException("the winebar does not exist"));

        return boardRepository.findByWinebar(winebar, pageable)
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

        return boardRepository.findByNickname(member.getNickname(), pageable)
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

    public Page<BoardSearchResponse> searchBoardByRegion(
            String region, Pageable pageable
    ) {
        return boardRepository.findByRegion(region, pageable)
                .map(BoardSearchResponse::fromEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {RuntimeException.class})
    public BoardUpdateResponse updateBoard(String email, Long boardId, BoardUpdateRequest request) {

        Board nowBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("the board does not exist"));

        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        if (!nowBoard.getMember().getId().equals(memberByEmail.getId())) {
            throw new RuntimeException("check the user information");
        }
        nowBoard.updateBoardInfo(request);
        boardRepository.save(nowBoard);
        return BoardUpdateResponse.fromEntity(nowBoard);
    }

    public BoardParticipantResponse participantInBoard(String email, Long boardId) {

        Board nowBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("the board does not exist"));

//        if(nowBoard.getParticipants().size() == nowBoard.getHeadcount()) {
//            throw new RuntimeException("이미 자리가 다 찼습니다.");
//        }

        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        memberByEmail.InBoard(boardId);
        memberRepository.save(memberByEmail);

        nowBoard.insertBoardParticipant(nowBoard, memberByEmail.getNickname());
        boardRepository.save(nowBoard);
        return BoardParticipantResponse.fromEntity(nowBoard);
    }

    public BoardParticipantResponse participantOutBoard(String email, Long boardId) {

        Board nowBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("the board does not exist"));
        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

//        if(Objects.equals(nowBoard.getMember().getNickname()
//                , memberByEmail.getNickname())) {
//            throw new RuntimeException("자기 자신은 나갈 수 없습니다.");
//        }
        memberByEmail.OutBoard(boardId);
        memberRepository.save(memberByEmail);

        nowBoard.deleteBoardParticipant(nowBoard, memberByEmail.getNickname());
        boardRepository.save(nowBoard);
        return BoardParticipantResponse.fromEntity(nowBoard);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {RuntimeException.class})
    public void deleteBoard(String email, Long boardId) {

        Board nowBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("the board does not exist"));

        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user does not exist"));

        if (!nowBoard.getMember().getId().equals(memberByEmail.getId())) {
            throw new RuntimeException("check the user information");
        }

        boardRepository.save(nowBoard); // test 코드에서 확인 위한
        boardRepository.delete(nowBoard);
    }

}
