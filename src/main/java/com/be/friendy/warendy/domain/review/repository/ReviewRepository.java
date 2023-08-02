package com.be.friendy.warendy.domain.review.repository;

import com.be.friendy.warendy.domain.member.entity.Member;
import com.be.friendy.warendy.domain.review.entity.Review;
import com.be.friendy.warendy.domain.wine.entity.Wine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByWine(Wine wine, Pageable pageable);
    Page<Review> findByMember(Member member, Pageable pageable);
}