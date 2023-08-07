package com.be.friendy.warendy.domain.board.entity;


import com.be.friendy.warendy.domain.board.dto.request.BoardUpdateRequest;
import com.be.friendy.warendy.domain.common.BaseEntity;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE warendy.board SET deleted_at = current_timestamp WHERE board_id = ?")
// delete 요청이 들어올때 db에 삭제되지 않고 deleted_at 컬럼에 삭제요청 시간으로 업데이트 된다.
@Where(clause = "deleted_at is NULL")
@Entity(name = "BOARD")
public class Board extends BaseEntity {

    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "BOARD_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WINEBAR_ID", nullable = false)
    private Winebar winebar;

    private String name;
    private String creator;
    private String date;
    private String time;
    private String wineName;
    private Integer headcount;
    private String contents;

    public void updateBoardInfo(BoardUpdateRequest request) {
        name = request.getName();
        creator = request.getCreator();
        date = request.getDate();
        time = request.getTime();
        wineName = request.getWineName();
        headcount = request.getHeadcount();
        contents = request.getContents();
    }

}
