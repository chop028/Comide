package com.klkk.CMD.quiz.repository;

import com.klkk.CMD.quiz.entity.QuizPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizPostRepository extends JpaRepository<QuizPost, Long> {

}
