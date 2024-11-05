package com.klkk.CMD.webConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해 CORS 설정 적용
                .allowedOrigins("https://comide.site")  // 허용할 클라이언트 도메인 (개발 환경)
                .allowedMethods("GET", "POST", "PUT")  // 허용할 HTTP 메서드
                .exposedHeaders("Authorization", "Content-Type")  // 클라이언트에 노출할 헤더 지정
                .allowCredentials(true)  // 자격 증명(쿠키 등)을 포함한 요청 허용
                .maxAge(3600);  // pre-flight 요청의 결과를 3600초(1시간) 동안 캐시
    }
}