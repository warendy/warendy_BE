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

    private String winebarName;
    private String name;
    private String creator;
    private String date;
    private String wineName;
    private Integer headcount;
    private String contents;

    public static BoardSearchResponse fromEntity(Board board) {
        return BoardSearchResponse.builder()
                .winebarName(board.getWinebar().getName())
                .name(board.getName())
                .creator(board.getCreator())
                .date(board.getDate())
                .wineName(board.getWineName())
                .headcount(board.getHeadcount())
                .contents(board.getContents())
                .build();
    }

}
