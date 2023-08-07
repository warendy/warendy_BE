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

    private String nickname;

    private String date;

    private String time;

    private String wineName;

    private Integer headcount;

    private String contents;

    public Board toEntity(Winebar wineBar, Member member) {
        return Board.builder()
                .member(member)
                .winebar(wineBar)
                .name(name)
                .nickname(member.getNickname())
                .date(date)
                .time(time)
                .wineName(wineName)
                .headcount(headcount)
                .contents(contents)
                .build();
    }
}
