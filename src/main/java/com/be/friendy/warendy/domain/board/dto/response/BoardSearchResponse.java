package com.be.friendy.warendy.domain.board.dto.response;


import com.be.friendy.warendy.domain.board.entity.Board;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardSearchResponse {

    private String name;        // board 제목
    private String winebarName;
    private String creator;
    private String date;
    private String time;
    private String wineName;
    private Integer headcount;
    private String contents;

    public static BoardSearchResponse fromEntity(Board board) {
        return BoardSearchResponse.builder()
                .name(board.getName())
                .winebarName(board.getWinebar().getName())
                .creator(board.getCreator())
                .date(board.getDate())
                .time(board.getTime())
                .wineName(board.getWineName())
                .headcount(board.getHeadcount())
                .contents(board.getContents())
                .build();
    }

}
