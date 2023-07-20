package com.be.friendy.warendy.domain.repository;

import com.be.friendy.warendy.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndOauthType(String email, String oauthType);

    boolean existsByEmail(String email);

    Optional<Member> findBySocialId(String socialId);
}
