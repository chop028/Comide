package com.klkk.CMD.gpt.controller;

import com.klkk.CMD.board.ResultVO;
import com.klkk.CMD.codeEditer.dto.CodeDto;
import com.klkk.CMD.gpt.service.GptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/gpt")
public class GptController {

    private final GptService gptService;

    public GptController(GptService gptService) {
        this.gptService = gptService;
    }

    @PostMapping("/ask-about-code")
    public ResponseEntity<String> askCode(@RequestBody Map<String, String> request) {
        String userQuery = request.get("query");

        String prompt = userQuery;

        try {
            String gptResponse = gptService.askGpt(prompt);
            return ResponseEntity.ok(gptResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("GPT API 호출 중 오류 발생: " + e.getMessage());
        }
    }

    @PostMapping("/analyze-code")
    public ResultVO analyzeCode(@RequestBody CodeDto codeDto) {
        ResultVO resultVO = new ResultVO(true, null);

        // GPT API를 통해 코드 분석
        String analysisResult = gptService.analyzeCode(codeDto.getContent());

        resultVO.setResult(analysisResult);
        resultVO.setSuccess(true);
        return resultVO;
    }
}
