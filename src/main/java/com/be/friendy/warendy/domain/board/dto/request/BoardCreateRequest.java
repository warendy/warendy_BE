package com.be.friendy.warendy.domain.board.dto.request;


import com.be.friendy.warendy.domain.board.entity.Board;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.winebar.entity.WineBar;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCreateRequest {

    private Long memberId;

    private String name;

    private String creator;

    private String date;

    private String wineName;

    private Integer headcount;

    private String contents;

    public Board toEntity(WineBar wineBar, Member member) {
        return Board.builder()
                .wineBar(wineBar)
                .member(member)
                .name(name)
                .creator(member.getNickname())
                .date(date)
                .wineName(wineName)
                .headcount(headcount)
                .contents(contents)
                .build();
    }

    public static BoardCreateRequest fromEntity(Board board) {
        return BoardCreateRequest.builder()
                .memberId(board.getMember().getId())
                .name(board.getName())
                .creator(board.getCreator())
                .date(board.getDate())
                .wineName(board.getWineName())
                .headcount(board.getHeadcount())
                .contents(board.getContents())
                .build();
    }

}
