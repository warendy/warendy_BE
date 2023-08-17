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
public class BoardUpdateResponse {
    private String name;
    private Long memberId;
    private String nickname;
    private String winebarName;
    private String date;
    private String time;
    private String region;
    private String wineName;
    private Integer headcount;
    private String contents;
    private Set<String> participants;

    public static BoardUpdateResponse fromEntity(Board board) {
        return BoardUpdateResponse.builder()
                .name(board.getName())
                .memberId(board.getMember().getId())
                .nickname(board.getNickname())
                .winebarName(board.getWinebar().getName())
                .date(board.getDate())
                .time(board.getTime())
                .region(board.getRegion())
                .wineName(board.getWineName())
                .headcount(board.getHeadcount())
                .contents(board.getContents())
                .participants(board.getParticipants())
                .build();
    }
}
