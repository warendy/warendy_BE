package com.be.friendy.warendy.domain.board.dto.response;


import com.be.friendy.warendy.domain.board.entity.Board;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardParticipantResponse {

    private String name;
    private String nickname;
    private String winebarName;
    private String date;
    private String time;
    private String region;
    private String wineName;
    private Integer headcount;
    private String contents;
    private Set<String> participants;

    public static BoardParticipantResponse fromEntity(Board board) {

        return BoardParticipantResponse.builder()
                .name(board.getName())
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
