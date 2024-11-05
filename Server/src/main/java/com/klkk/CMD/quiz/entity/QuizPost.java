package com.klkk.CMD.quiz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "quiz_post")
public class QuizPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUIZPOST_ID")
    private Long id;
    private String title;
    @Column(name = "TOTAL_SCORE")
    private double totalScore;
    @Column(name = "LANG_ID")
    private String category;
    @Column(name = "quiz_type")
    private String quizType;

    @Builder
    public QuizPost(Long id, String title, double totalScore, String category, String quizType) {
        this.id = id;
        this.title = title;
        this.totalScore = totalScore;
        this.category = category;
        this.quizType = quizType;
    }
}
