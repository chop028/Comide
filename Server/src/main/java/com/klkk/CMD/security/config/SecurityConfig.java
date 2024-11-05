package com.klkk.CMD.security.config;

import com.klkk.CMD.security.dto.CustomOAuth2User;
import com.klkk.CMD.security.jwt.JwtAuthenticationFilter;
import com.klkk.CMD.security.jwt.JwtTokenProvider;
import com.klkk.CMD.security.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${app.redirect-url}")
    private String redirectBaseUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 인코딩 필터 추가
        http.addFilterBefore(characterEncodingFilter(), UsernamePasswordAuthenticationFilter.class);

        // CSRF 비활성화 및 CORS 설정
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // 모든 요청에 대해 인증 필요
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()  // API 요청은 인증 없이 허용
                .anyRequest().authenticated()  // 그 외 요청은 인증 필요
        );

        // OAuth2 로그인 성공 후 JWT 토큰 생성 및 전송
        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)  // OAuth2 로그인 성공 시 사용자 정보 처리
                )
                .successHandler((request, response, authentication) -> {
                    CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
                    Map<String, Object> claims = new HashMap<>(oAuth2User.getAttributes());
                    claims.put("roles", oAuth2User.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()));

                    String token = jwtTokenProvider.createToken(claims, oAuth2User.getGoogleId());

                    // Optional 처리: 권한을 안전하게 가져오기
                    String role = oAuth2User.getAuthorities().stream()
                            .findFirst()
                            .map(GrantedAuthority::getAuthority)
                            .orElse("ROLE_USER");

                    // 클라이언트로 리다이렉트
                    String redirectUrl = redirectBaseUrl + "?token=" + token
                            + "&id=" + oAuth2User.getId()
                            + "&googleId=" + oAuth2User.getGoogleId()
                            + "&name=" + URLEncoder.encode(oAuth2User.getName(), StandardCharsets.UTF_8)
                            + "&email=" + oAuth2User.getEmail()
                            + "&picture=" + oAuth2User.getPicture()
                            + "&comment=" + URLEncoder.encode(oAuth2User.getComment(), StandardCharsets.UTF_8)
                            + "&role=" + role;

                    // 클라이언트로 JWT 및 유저 정보를 포함한 리다이렉트
                    response.sendRedirect(redirectUrl);
                })
        );

        // JWT 인증 필터 추가
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("https://comide.site");  // 배포된 Vue.js 클라이언트 도메인 허용
        configuration.addAllowedMethod("*");  // 모든 HTTP 메서드 허용 (GET, POST, PUT, DELETE 등)
        configuration.addAllowedHeader("*");  // 모든 헤더 허용
        configuration.setAllowCredentials(true);  // 자격 증명(쿠키 및 인증 헤더) 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // 모든 경로에 대해 CORS 설정 적용
        return source;
    }

    // UTF-8 인코딩 필터
    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceRequestEncoding(true);
        filter.setForceResponseEncoding(true);
        return filter;
    }
}