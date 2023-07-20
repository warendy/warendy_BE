package com.be.friendy.warendy.domain.dto.request;

import com.be.friendy.warendy.domain.entity.Board;
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

    private int headcount;

    private String contents;

    public Board toEntity() {
        return Board.builder()
                .wineBarId(this.wineBarId)
                .memberId(this.memberId)
                .name(this.name)
                .creator(this.creator)
                .date(this.date)
                .wineName(this.wineName)
                .headcount(this.headcount)
                .contents(this.contents)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
