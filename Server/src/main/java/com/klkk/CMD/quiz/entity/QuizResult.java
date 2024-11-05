package com.klkk.CMD.quiz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "quiz_result")
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quizresult_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "quizpost_id")
    private Long quizPostId;

    @Column(name = "excute_date")
    private String excuteDate;
    @Column(name = "duration_time")
    private String durationTime;
    @Column(name = "crt_ans")
    private int correctAnswer;
    private String comment;


    @Builder
    public QuizResult(Long id, Long userId, Long quizPostId, String excuteDate, String durationTime, int correctAnswer, String comment) {
        this.id = id;
        this.userId = userId;
        this.quizPostId = quizPostId;
        this.excuteDate = excuteDate;
        this.durationTime = durationTime;
        this.correctAnswer = correctAnswer;
        this.comment = comment;
    }
}
