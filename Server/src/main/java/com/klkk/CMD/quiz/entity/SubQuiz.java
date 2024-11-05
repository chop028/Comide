package com.klkk.CMD.quiz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "sub_quiz")
public class SubQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subquiz_id")
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String title;
    private String content;
    private String answer;

    @ManyToOne
    @JoinColumn(name = "QUIZPOST_ID")
    private QuizPost quizPostId;

    @Builder
    public SubQuiz(Long id, String content, String answer, String title, QuizPost quizPostId) {
        Assert.hasText(answer, "answer must not be empty");
        Assert.hasText(content, "content must not be empty");

        this.id = id;
        this.title = title;
        this.content = content;
        this.answer = answer;
        this.quizPostId = quizPostId;
    }
}
