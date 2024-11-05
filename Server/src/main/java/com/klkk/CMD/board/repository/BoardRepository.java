package com.klkk.CMD.board.repository;

import com.klkk.CMD.board.Entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findByCategory(@Param("category") String category);

    List<Board> findByUserId(@Param("userId") Long userId);

    Optional<Board> findById(@Param("postId") Long postId);
}