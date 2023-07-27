package com.be.friendy.warendy.domain.board.repository;

import com.be.friendy.warendy.domain.board.entity.Board;
import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByName(String boardName);

    List<Board> findAllByWineName(String wineName);

    List<Board> findAllByWinebar(Winebar winebar);

    List<Board> findAllByCreator(String creator);

    List<Board> findAllByDate(String date);

    Optional<Board> findById(Long id);

    boolean existsByName(String name);
}
