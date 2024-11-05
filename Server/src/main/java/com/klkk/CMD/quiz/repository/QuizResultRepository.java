package com.klkk.CMD.quiz.repository;

import com.klkk.CMD.quiz.entity.MultiQuiz;
import com.klkk.CMD.quiz.entity.QuizPost;
import com.klkk.CMD.quiz.entity.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    List<QuizResult> findQuizResultByUserId(@Param("userId") Long userId);
}
