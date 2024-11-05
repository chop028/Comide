package com.klkk.CMD.quiz.dto;

import com.klkk.CMD.quiz.entity.QuizPost;
import com.klkk.CMD.quiz.entity.QuizResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
public class QuizResultListDto {
    QuizResult quizResult;
    QuizPost quizPost;
}
