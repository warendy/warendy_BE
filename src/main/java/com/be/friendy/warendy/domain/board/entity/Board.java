package com.be.friendy.warendy.domain.board.entity;


import com.be.friendy.warendy.domain.board.dto.request.BoardUpdateRequest;
import com.be.friendy.warendy.domain.common.BaseEntity;
import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;
    private String date;
    private String time;
    private String wineName;

    @Positive(message = "Headcount must be a positive number")
    private Integer headcount;
    private String contents;

    @ElementCollection
    private Set<String> participants = new HashSet<>();

    public void updateBoardInfo(BoardUpdateRequest request) {
        name = request.getName();
        nickname = request.getNickname();
        date = request.getDate();
        time = request.getTime();
        wineName = request.getWineName();
        headcount = request.getHeadcount();
        contents = request.getContents();
    }

    public void insertBoardParticipant(Board board, String nickname) {
        Set<String> boardParticipants = board.getParticipants();
        boardParticipants.add(nickname);
        participants = boardParticipants;
    }

    public void deleteBoardParticipant(Board board, String nickname) {
        Set<String> boardParticipants = board.getParticipants();
        boardParticipants.remove(nickname);
        participants = boardParticipants;
    }

}
