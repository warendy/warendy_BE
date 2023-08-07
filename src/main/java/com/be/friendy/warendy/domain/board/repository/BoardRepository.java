package com.be.friendy.warendy.domain.board.repository;

import com.be.friendy.warendy.domain.board.entity.Board;
import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @NonNull
    Optional<Board> findById(Long id);

    Page<Board> findByName(String boardName, Pageable pageable);

    Page<Board> findByWineName(String wineName, Pageable pageable);

    Page<Board> findByWinebar(Winebar winebar, Pageable pageable);

    Page<Board> findByNickname(String creator, Pageable pageable);

    Page<Board> findByDate(String date, Pageable pageable);

    Page<Board> findByTime(String time, Pageable pageable);

    boolean existsByName(String name);
}
