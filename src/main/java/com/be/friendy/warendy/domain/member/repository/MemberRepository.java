package com.be.friendy.warendy.domain.member.repository;

import com.be.friendy.warendy.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    @NonNull
    Optional<Member> findById(Long memberId);

    boolean existsByEmail(String email);

}
