package com.be.friendy.warendy.domain.board.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardUpdateRequest {

    private Long memberId;
    private Long winebarId;
    private String name;
    private String creator;
    private String date;
    private String time;
    private String wineName;
    private Integer headcount;
    private String contents;

}
