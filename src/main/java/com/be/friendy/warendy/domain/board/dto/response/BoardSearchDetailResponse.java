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
public class BoardSearchDetailResponse {
    private String name;        // board 제목
    private String winebarName;
    private String winebarAddress;
    private Long memberId;
    private String nickname;
    private String date;
    private String time;
    private String region;
    private String wineName;
    private Integer headcount;
    private String contents;
    private Set<String> participants;

    public static BoardSearchDetailResponse fromEntity(Board board) {
        return BoardSearchDetailResponse.builder()
                .name(board.getName())
                .winebarName(board.getWinebar().getName())
                .winebarAddress(board.getWinebar().getAddress())
                .memberId(board.getMember().getId())
                .nickname(board.getNickname())
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
