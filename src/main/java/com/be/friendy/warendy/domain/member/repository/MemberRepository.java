package com.be.friendy.warendy.domain.member.repository;

import com.be.friendy.warendy.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @NonNull
    Optional<Member> findById(Long memberId);

    Optional<Member> findByNickname(String nickname);

    boolean existsByEmail(String email);

}
