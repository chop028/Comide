package com.klkk.CMD.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GptService {

    private final RestTemplate restTemplate;

    @Value("${openai.url.prompt}")
    private String apiUrl;

    public String askGpt(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4-turbo");  // 사용할 GPT 모델
        requestBody.put("messages", new Object[]{
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });
        requestBody.put("max_tokens", 1000);

        // 요청을 위한 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept-Charset", "UTF-8");

        // 요청 객체 생성
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // GPT API 호출
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        // 응답 반환
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();  // 정상 응답 시 결과 반환
        } else {
            throw new RuntimeException("GPT API 응답 오류: " + response.getStatusCode());
        }
    }


    public String analyzeCode(String code) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");  // 사용 모델
        body.put("messages", List.of(
                Map.of("role", "system", "content", "당신은 자바 코드를 분석하고, 사용자에게 학습할 수 있는 도움을 제공하는 선생님의 역할을 맡고 있습니다."),
                Map.of("role", "user", "content", "다음 자바 코드를 분석해 주세요. 코드에서 오류가 있으면 알려주고, 더 나은 구현 방안을 제안해 주세요. 또한 사용자가 이 코드를 통해 학습할 수 있도록 개념을 설명하고, 관련 자료를 제공해 주세요. 응답은 한국어로 해주세요:\n\n" + code)
        ));

        body.put("max_tokens", 800);

        // GPT API 호출
        return restTemplate.postForObject(apiUrl, body, String.class);
    }
}