package com.klkk.CMD.quiz.repository;

import com.klkk.CMD.quiz.entity.MultiQuiz;
import com.klkk.CMD.quiz.entity.QuizPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultiQuizRepository extends JpaRepository<MultiQuiz, Long> {
    List<MultiQuiz> findQuizByQuizPostId(@Param("quizPostId") Long quizPostId);

}