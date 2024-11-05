package com.klkk.CMD.quiz.dto;

import com.klkk.CMD.quiz.entity.MultiQuiz;
import com.klkk.CMD.quiz.entity.QuizPost;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MultiQuizDto {
    private Long id;
    private String title;
    private int answer;
    private Long quizPostId;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String choice5;

    public MultiQuiz toEntity() {
        MultiQuiz multiQuiz = MultiQuiz.builder()
                .id(id)
                .title(title)
                .answer(answer)
                .quizPostId(quizPostId)
                .choice1(choice1)
                .choice2(choice2)
                .choice3(choice3)
                .choice4(choice4)
                .choice5(choice5)
                .build();
        return multiQuiz;
    }

    @Builder
    public MultiQuizDto(Long id, String title, int answer, Long quizPostId, String choice1, String choice2, String choice3, String choice4, String choice5) {
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
