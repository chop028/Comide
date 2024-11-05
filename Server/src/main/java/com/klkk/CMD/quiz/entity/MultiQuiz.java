package com.klkk.CMD.quiz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "multi_quiz")
public class MultiQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "multiquiz_id")
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String title;
    private int answer;

    @Column(name = "QUIZPOST_ID")
    private Long quizPostId;

    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String choice5;

    @Builder
    public MultiQuiz(Long id, String title, int answer, Long quizPostId, String choice1, String choice2, String choice3, String choice4, String choice5) {
        this.id = id;
        this.answer = answer;
        this.title = title;
        this.quizPostId = quizPostId;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.choice5 = choice5;
    }
}
