package com.klkk.CMD.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long expirationTime;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.expiration}") long expirationTime) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());  // SecretKey 설정
        this.expirationTime = expirationTime;
    }

    // JWT 토큰 생성 시 권한을 포함
    public String createToken(Map<String, Object> claims, String googleId) {
        Claims tokenClaims = Jwts.claims().setSubject(googleId);
        tokenClaims.putAll(claims);

        // JWT에 권한 정보를 포함
        Object rolesObject = claims.get("roles");

        if (rolesObject instanceof List<?>) {
            List<?> roles = (List<?>) rolesObject;

            // 각 요소가 String인지 확인 후 처리
            List<String> roleList = roles.stream()
                    .filter(role -> role instanceof String)
                    .map(role -> (String) role)
                    .collect(Collectors.toList());

            tokenClaims.put("roles", roleList);  // 권한을 claims에 추가
        } else {
            throw new IllegalArgumentException("Roles must be a list of strings");
        }

        return Jwts.builder()
                .setClaims(tokenClaims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)  // SecretKey 및 알고리즘 설정
                .compact();
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)  // SecretKey로 서명 검증
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }

    public String extractGoogleId(String token) {
        return extractAllClaims(token).getSubject();
    }

    // JWT 토큰에서 권한을 추출
    public List<SimpleGrantedAuthority> getAuthorities(String token) {
        Claims claims = extractAllClaims(token);

        // 안전한 타입 변환
        Object rolesObject = claims.get("roles");
        if (rolesObject instanceof List<?>) {
            List<?> roles = (List<?>) rolesObject;
            return roles.stream()
                    .filter(role -> role instanceof String)  // 각 요소가 String인지 확인
                    .map(role -> new SimpleGrantedAuthority((String) role))
                    .collect(Collectors.toList());
        }
        throw new IllegalArgumentException("Roles not found or invalid format");
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}