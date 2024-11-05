package com.klkk.CMD.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String googleId = null;

        // JWT 토큰이 있는지 확인
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);  // "Bearer " 부분 제거
            try {
                googleId = jwtTokenProvider.extractGoogleId(token);  // JWT에서 사용자 ID 추출
            } catch (IllegalArgumentException e) {
                // JWT 추출 과정에서 예외 발생 시 처리 (예: 유효하지 않은 토큰)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 Unauthorized 반환
                response.getWriter().write("Invalid or expired JWT token");
                return;  // 더 이상 필터 체인을 진행하지 않음
            }
        }

        // JWT가 존재하고, SecurityContext에 인증 정보가 없는 경우
        if (googleId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 토큰이 만료되지 않았는지 확인
            if (!jwtTokenProvider.isTokenExpired(token)) {
                // 유효한 토큰일 경우 인증 설정
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(googleId, null, jwtTokenProvider.getAuthorities(token));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                // 토큰이 만료된 경우 401 반환
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has expired");
                return;
            }
        }

        // 요청 처리
        chain.doFilter(request, response);
    }
}