package com.be.friendy.warendy.domain.board.dto.response;


import com.be.friendy.warendy.domain.board.entity.Board;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardSearchDetailResponse {
    private String name;        // board 제목
    private String winebarName;
    private String winebarAddress;
    private String nickname;
    private String date;
    private String time;
    private String wineName;
    private Integer headcount;
    private String contents;

    public static BoardSearchDetailResponse fromEntity(Board board) {
        return BoardSearchDetailResponse.builder()
                .name(board.getName())
                .winebarName(board.getWinebar().getName())
                .winebarAddress(board.getWinebar().getAddress())
                .nickname(board.getNickname())
                .date(board.getDate())
                .time(board.getTime())
                .wineName(board.getWineName())
                .headcount(board.getHeadcount())
                .contents(board.getContents())
                .build();
    }
}
