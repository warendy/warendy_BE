package com.be.friendy.warendy.domain.member.entity;

import com.be.friendy.warendy.domain.common.BaseEntity;
import com.be.friendy.warendy.domain.member.entity.constant.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE member SET deleted_at = current_timestamp WHERE member_id=?")
// delete 요청이 들어올때 db에 삭제되지 않고 deleted_at 컬럼에 삭제요청 시간으로 업데이트 된다.
@Where(clause = "deleted_at is NULL")
@Entity(name = "MEMBER")
public class Member extends BaseEntity {

    @Id // 엔티티 내부에서 아이디임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "MEMBER_ID") // 아이디에 해당하는 컬럼명 선언
    private Long id;
  
    private String email;
    private String password;

    @Column(unique = true)
    private String nickname;

    private String avatar;
    private String mbti;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String oauthType;

    private int body;
    private int dry;
    private int tannin;
    private int acidity;

    private String inBoardIdList;

    public void InBoard(Long boardId) {

        if(inBoardIdList != null){
            StringBuilder targetBoard = new StringBuilder();
            targetBoard.append(boardId).append(",");
            if(inBoardIdList.contains(targetBoard.toString())
                    || inBoardIdList.contains(String.valueOf(boardId))) {

            } else {
                targetBoard.append(inBoardIdList);
                inBoardIdList = targetBoard.toString();
            }
        } else {
            inBoardIdList = String.valueOf(boardId);
        }

    }

    public void OutBoard(Long boardId) {

        String targetBoard = boardId + ",";
        if(inBoardIdList.contains(targetBoard)){
            inBoardIdList = inBoardIdList.replace(targetBoard,"");
        } else {
            inBoardIdList = inBoardIdList.replace(
                    String.valueOf(boardId),"");
        }
    }

    // Member 엔티티에서 원하는 필드만 수정하는 메서드
    public void updateMemberInfo(String email, String password, String nickname,
                                 String avatar, Integer body,
                                 Integer dry, Integer tannin, Integer acidity) {
        if (email != null) {
            this.email = email;
        }
        if (password != null) {
            this.password = password;
        }
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (avatar != null) {
            this.avatar = avatar;
        }
        if (body != null) {
            this.body = body;
        }
        if (dry != null) {
            this.dry = dry;
        }
        if (tannin != null) {
            this.tannin = tannin;
        }
        if (acidity != null) {
            this.acidity = acidity;
        }
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role userRole = this.getRole();
        String authority = userRole.getKey();
        SimpleGrantedAuthority simpleAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleAuthority);

        return authorities;
    }

}
