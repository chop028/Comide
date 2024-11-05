package com.klkk.CMD.board.repository;

import com.klkk.CMD.board.Entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    List<HashTag> findByBoardId(@Param("boardId") Long boardId);
}