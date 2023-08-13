package com.be.friendy.warendy.domain.board.dto.response;


import com.be.friendy.warendy.domain.board.entity.Board;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCreateResponse {

    private Long boardId;
    private Long memberId;
    private Long winebarId;
    private String name;
    private String nickname;
    private String date;
    private String time;
    private String wineName;
    private Integer headcount;
    private String contents;
    private Set<String> participants;

    public static BoardCreateResponse fromEntity(Board board) {
        return BoardCreateResponse.builder()
                .boardId(board.getId())
                .memberId(board.getMember().getId())
                .winebarId(board.getWinebar().getId())
                .name(board.getName())
                .nickname(board.getNickname())
                .date(board.getDate())
                .time(board.getTime())
                .wineName(board.getWineName())
                .headcount(board.getHeadcount())
                .contents(board.getContents())
                .participants(board.getParticipants())
                .build();
    }
}
