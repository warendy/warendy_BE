package com.be.friendy.warendy.domain.board.dto.request;

import com.be.friendy.warendy.domain.board.entity.Board;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.winebar.entity.WineBar;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardUpdateRequest {
    private Long memberId;
    private Long wineBarId;
    private String name;
    private String creator;
    private String date;
    private String wineName;
    private Integer headcount;
    private String contents;

}
