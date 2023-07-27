package com.be.friendy.warendy.domain.board.dto.request;


import com.be.friendy.warendy.domain.board.entity.Board;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCreateRequest {

    private Long memberId;

    private String name;

    private String creator;

    private String date;

    private String wineName;

    private Integer headcount;

    private String contents;

    public Board toEntity(Winebar wineBar, Member member) {
        return Board.builder()
                .member(member)
                .winebar(wineBar)
                .name(name)
                .creator(member.getNickname())
                .date(date)
                .wineName(wineName)
                .headcount(headcount)
                .contents(contents)
                .build();
    }
}
