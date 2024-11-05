package com.klkk.CMD.board.repository;

import com.klkk.CMD.board.Entity.BoardContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardContentRepository extends JpaRepository<BoardContent, Long> {
    List<BoardContent> findByBoardId(@Param("boardId") Long boardId);
    void deleteByBoardId(Long boardId);
}