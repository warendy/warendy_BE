package com.be.friendy.warendy.domain.board.dto.response;


import com.be.friendy.warendy.domain.board.dto.request.BoardCreateRequest;
import com.be.friendy.warendy.domain.board.entity.Board;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.winebar.entity.WineBar;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCreateResponse {

    private Long memberId;
    private Long wineBarId;
    private String name;
    private String creator;
    private String date;
    private String wineName;
    private Integer headcount;
    private String contents;

    public static BoardCreateResponse fromEntity(Board board) {
        return BoardCreateResponse.builder()
                .memberId(board.getMember().getId())
                .wineBarId(board.getWineBar().getId())
                .name(board.getName())
                .creator(board.getCreator())
                .date(board.getDate())
                .wineName(board.getWineName())
                .headcount(board.getHeadcount())
                .contents(board.getContents())
                .build();
    }

    public static BoardCreateResponse fromRequest(Long winebarId, BoardCreateRequest createRequest) {
        return BoardCreateResponse.builder()
                .memberId(createRequest.getMemberId())
                .wineBarId(winebarId)
                .name(createRequest.getName())
                .creator(createRequest.getCreator())
                .date(createRequest.getDate())
                .wineName(createRequest.getWineName())
                .headcount(createRequest.getHeadcount())
                .contents(createRequest.getContents())
                .build();
    }


}
