package com.klkk.CMD.quiz.controller;

import com.klkk.CMD.board.ResultVO;
import com.klkk.CMD.quiz.dto.MultiQuizDto;
import com.klkk.CMD.quiz.dto.QuizPostDto;
import com.klkk.CMD.quiz.dto.QuizResultDto;
import com.klkk.CMD.quiz.dto.QuizResultListDto;
import com.klkk.CMD.quiz.entity.QuizPost;
import com.klkk.CMD.quiz.entity.QuizResult;
import com.klkk.CMD.quiz.service.QuizPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizPostController {
    private final QuizPostService quizPostService;

    @RequestMapping(value = "get-quiz-post.do", method = RequestMethod.GET)
    public ResultVO getQuizPost(@RequestParam Long postId) {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            resultVO.setResult(quizPostService.getQuizPostById(postId));
            resultVO.setSuccess(true);
        } catch (Exception e) {

        }
        return resultVO;
    }

    @RequestMapping(value = "get-quiz-list.do", method = RequestMethod.GET)
    @GetMapping("get-quiz-list.do")
    public ResultVO getQuizList() {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            resultVO.setResult(quizPostService.getQuizPostList());
            resultVO.setSuccess(true);
        } catch (Exception e) {

        }
        return resultVO;
    }

    @RequestMapping(value = "get-quiz-detail-list.do", method = RequestMethod.GET)
    @GetMapping("get-quiz-detail-list.do")
    public ResultVO getQuizListByPostId(@RequestParam Long postId) {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            resultVO.setResult(quizPostService.getQuizDetailListByPostId(postId));
            resultVO.setSuccess(true);
        } catch (Exception e) {
            resultVO.setSuccess(false);
            // 예외를 로깅하거나 적절한 방법으로 처리할 수 있습니다.
        }
        return resultVO;
    }

    @RequestMapping(value = "post-quiz-result.do", method = RequestMethod.POST)
    @GetMapping("post-quiz-result.do")
    public ResultVO setQuizResult(@RequestBody QuizResultDto quizResultDto) {
        ResultVO resultVO = new ResultVO(false, null);
        Long savedId = quizPostService.saveQuizResult(quizResultDto); // saveQuizResult 메서드에서 엔티티의 id값을 반환하도록 설정
        resultVO.setResult(savedId); // 반환할 데이터를 저장
        resultVO.setSuccess(true);
        return resultVO;
    }

    @RequestMapping(value = "get-quiz-result.do", method = RequestMethod.GET)
    @GetMapping("get-quiz-result.do")
    public ResultVO getQuizResultById(@RequestParam Long postId) {
        QuizResult quizResult = quizPostService.getQuizResultById(postId).get();
        QuizPost quizPost = quizPostService.getQuizPostById(quizResult.getQuizPostId()).get();
        ResultVO resultVO = new ResultVO(false, null);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("quizResult", quizResult);
        resultMap.put("quizPost", quizPost);
        try {
            resultVO.setResult(resultMap);
            resultVO.setSuccess(true);
        } catch (Exception e) {
            resultVO.setSuccess(false);
            // 예외를 로깅하거나 적절한 방법으로 처리할 수 있습니다.
        }
        return resultVO;
    }

    @RequestMapping(value = "get-quiz-result-list.do", method = RequestMethod.GET)
    public ResultVO getQuizResultList(@RequestParam Long userId) {
        ResultVO resultVO = new ResultVO(false, null);
        List<QuizResultListDto> quizResultListDtoList = quizPostService.getQuizResultListByUserId(userId);
        resultVO.setSuccess(true);
        resultVO.setResult(quizResultListDtoList);
        return resultVO;
    }

    @RequestMapping(value = "post-quizPost.do", method = RequestMethod.POST)
    public ResultVO postQuizPost(@RequestBody QuizPostDto quizPostDto) {
        ResultVO resultVO = new ResultVO(false, null);
        Long quizPostId = quizPostService.savePost(quizPostDto);
        resultVO.setResult(quizPostId); // 반환할 데이터를 저장
        resultVO.setSuccess(true);
        return resultVO;
    }

    @RequestMapping(value = "post-multiQuiz-list.do", method = RequestMethod.POST)
    public ResultVO postMultiQuizList(@RequestBody List<MultiQuizDto> multiQuizDtoList) {
        ResultVO resultVO = new ResultVO(false, null);
        quizPostService.saveMultiQuiz(multiQuizDtoList);
        resultVO.setResult("success"); // 반환할 데이터를 저장
        resultVO.setSuccess(true);
        return resultVO;
    }
}
