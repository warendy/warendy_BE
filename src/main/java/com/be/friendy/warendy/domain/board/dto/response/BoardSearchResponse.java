package com.be.friendy.warendy.domain.board.dto.response;


import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardSearchResponse {

    private String winebarName;
    private String name;
    private String creator;
    private String date;
    private String wineName;
    private Integer headcount;
    private String contents;

}
