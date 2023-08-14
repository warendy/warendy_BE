package com.be.friendy.warendy.domain.member.dto.request;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequest {

    private String email;
    private String password;
    private String nickname;
    private String avatar;

    @Min(0)
    @Max(4)
    private Integer body;

    @Min(0)
    @Max(4)
    private Integer dry;

    @Min(0)
    @Max(4)
    private Integer tannin;

    @Min(0)
    @Max(4)
    private Integer acidity;
}


