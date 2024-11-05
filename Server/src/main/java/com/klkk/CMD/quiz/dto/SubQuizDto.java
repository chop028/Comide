package com.klkk.CMD.quiz.dto;

import com.klkk.CMD.quiz.entity.QuizPost;
import com.klkk.CMD.quiz.entity.SubQuiz;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SubQuizDto {
    private Long id;
    private String content;
    private String answer;
    private String title;
    private QuizPost quizPostId;

    public SubQuiz toEntity() {
        SubQuiz subQuiz = SubQuiz.builder()
                .id(id)
                .title(title)
                .content(content)
                .answer(answer)
                .quizPostId(quizPostId)
                .build();
        return subQuiz;
    }

    @Builder
    public SubQuizDto(Long id, String content, String answer, String title, QuizPost quizPostId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.answer = answer;
        this.quizPostId = quizPostId;
    }
}
