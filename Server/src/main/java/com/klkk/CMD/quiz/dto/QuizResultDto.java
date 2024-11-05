package com.klkk.CMD.quiz.dto;

import com.klkk.CMD.quiz.entity.QuizResult;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuizResultDto {
    private Long id;
    private Long userId;
    private Long quizPostId;
    private String excuteDate;
    private String durationTime;
    private int correctAnswer;
    private String comment;

    public QuizResult toEntity() {
        QuizResult quizResult = QuizResult.builder()
                .id(id)
                .userId(userId)
                .quizPostId(quizPostId)
                .excuteDate(excuteDate)
                .durationTime(durationTime)
                .comment(comment)
                .correctAnswer(correctAnswer)
                .build();
        return quizResult;
    }

    @Builder
    public QuizResultDto(Long id, Long userId, Long quizPostId, String excuteDate, String durationTime, int correctAnswer, String comment) {
        this.id = id;
        this.userId = userId;
        this.quizPostId = quizPostId;
        this.excuteDate = excuteDate;
        this.durationTime = durationTime;
        this.correctAnswer = correctAnswer;
        this.comment = comment;
    }
}
