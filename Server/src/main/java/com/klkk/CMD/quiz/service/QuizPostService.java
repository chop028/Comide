package com.klkk.CMD.quiz.service;

import com.klkk.CMD.quiz.dto.MultiQuizDto;
import com.klkk.CMD.quiz.dto.QuizPostDto;
import com.klkk.CMD.quiz.dto.QuizResultDto;
import com.klkk.CMD.quiz.dto.QuizResultListDto;
import com.klkk.CMD.quiz.entity.MultiQuiz;
import com.klkk.CMD.quiz.entity.QuizPost;
import com.klkk.CMD.quiz.entity.QuizResult;
import com.klkk.CMD.quiz.repository.MultiQuizRepository;
import com.klkk.CMD.quiz.repository.QuizPostRepository;
import com.klkk.CMD.quiz.repository.QuizResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@EnableAsync
@RequiredArgsConstructor
public class QuizPostService {
    private final QuizPostRepository quizPostRepository;
    private final MultiQuizRepository multiQuizRepository;
    private final QuizResultRepository quizResultRepository;

    public List<QuizPost> getQuizPostList() {
        return quizPostRepository.findAll();
    }

    public List<QuizResultListDto> getQuizResultListByUserId(Long userId){
        List<QuizResult> quizResultList = quizResultRepository.findQuizResultByUserId(userId);
        ArrayList<QuizResultListDto> quizResultListDtoArrayList = new ArrayList<>();
        for(QuizResult quizResult : quizResultList) {
            QuizResultListDto quizResultListDto = new QuizResultListDto();
            quizResultListDto.setQuizResult(quizResult);
            quizResultListDto.setQuizPost(getQuizPostById(quizResult.getQuizPostId()).get());
            quizResultListDtoArrayList.add(quizResultListDto);
        }
        return quizResultListDtoArrayList;
    }

    public Optional<QuizPost> getQuizPostById(Long postId) {
        return quizPostRepository.findById(postId);
    }

    public List<MultiQuiz> getQuizDetailListByPostId(Long quizPostId) {
        return multiQuizRepository.findQuizByQuizPostId(quizPostId); // 수정된 부분

    }

    public Optional<QuizResult> getQuizResultById(Long resultId) {
        return quizResultRepository.findById(resultId);
    }

    @Transactional
        public Long savePost(QuizPostDto quizPostDto) {
        return quizPostRepository.save(quizPostDto.toEntity()).getId();
    }

    @Transactional
    public Long saveQuizResult(QuizResultDto quizResultDto) {
        return quizResultRepository.save(quizResultDto.toEntity()).getId();
    }

    @Transactional
    public void saveMultiQuiz(List<MultiQuizDto> multiQuizDtoList) {
        for (MultiQuizDto multiQuizDto : multiQuizDtoList) {
            multiQuizRepository.save(multiQuizDto.toEntity());
        }
    }
}
