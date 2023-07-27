package com.be.friendy.warendy.domain.board.dto.response;


import com.be.friendy.warendy.domain.board.entity.Board;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCreateResponse {

    private Long memberId;
    private Long winebarId;
    private String name;
    private String creator;
    private String date;
    private String wineName;
    private Integer headcount;
    private String contents;

    public static BoardCreateResponse fromEntity(Board board) {
        return BoardCreateResponse.builder()
                .memberId(board.getMember().getId())
                .winebarId(board.getWinebar().getId())
                .name(board.getName())
                .creator(board.getCreator())
                .date(board.getDate())
                .wineName(board.getWineName())
                .headcount(board.getHeadcount())
                .contents(board.getContents())
                .build();
    }
}
