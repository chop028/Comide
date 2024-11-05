package com.klkk.CMD.quiz.dto;

import com.klkk.CMD.quiz.entity.Language;
import com.klkk.CMD.quiz.entity.QuizPost;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuizPostDto {
    private Long id;
    private String title;
    private double totalScore;
    private String category;
    private String quizType;
    private String userId;

    public QuizPost toEntity() {
        QuizPost quizPost = QuizPost.builder()
                .id(id)
                .title(title)
                .totalScore(totalScore)
                .category(category)
                .quizType(quizType)
                .build();
        return quizPost;
    }

    @Builder
    public QuizPostDto(Long id, String title, double totalScore, String userId, String category, String quizType) {
        this.id = id;
        this.title = title;
        this.totalScore = totalScore;
        this.userId = userId;
        this.category = category;
        this.quizType = quizType;
    }
}
