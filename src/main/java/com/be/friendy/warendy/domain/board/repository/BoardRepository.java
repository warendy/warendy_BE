package com.be.friendy.warendy.domain.board.repository;

import com.be.friendy.warendy.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByNameAndDeletedAtIsNull(String boardName);

    List<Board> findAllByWineNameAndDeletedAtIsNull(String wineName);

    List<Board> findAllByWineBarIdAndDeletedAtIsNull(Long wineBarId);

    List<Board> findAllByCreatorAndDeletedAtIsNull(String creator);

    List<Board> findAllByDateAndDeletedAtIsNull(String date);

    Optional<Board> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByName(String name);
}
