package com.be.friendy.warendy.domain.board.dto.response;

import com.be.friendy.warendy.domain.board.entity.Board;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardUpdateResponse {
    private String name;
    private String nickname;
    private String winebarName;
    private String date;
    private String time;
    private String wineName;
    private Integer headcount;
    private String contents;

    public static BoardUpdateResponse fromEntity(Board board) {
        return BoardUpdateResponse.builder()
                .name(board.getName())
                .nickname(board.getNickname())
                .winebarName(board.getWinebar().getName())
                .date(board.getDate())
                .time(board.getTime())
                .wineName(board.getWineName())
                .headcount(board.getHeadcount())
                .contents(board.getContents())
                .build();
    }
}
