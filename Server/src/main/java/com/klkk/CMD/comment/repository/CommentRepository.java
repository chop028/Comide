package com.klkk.CMD.comment.repository;

import com.klkk.CMD.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(@Param("postId") Long postId);
    List<Comment> findByUserId(@Param("userId") Long userId);
}
