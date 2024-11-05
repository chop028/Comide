package com.klkk.CMD.security.controller;

import com.klkk.CMD.security.dto.UserUpdateRequest;
import com.klkk.CMD.security.entity.User;
import com.klkk.CMD.security.jwt.JwtTokenProvider;
import com.klkk.CMD.security.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PutMapping("/update")
    public ResponseEntity<?> updateUserInfo(HttpServletRequest request, @RequestBody UserUpdateRequest updatedUserInfo) {
        try {
            // JWT에서 Google ID 추출
            String token = request.getHeader("Authorization").substring(7);
            String googleId = jwtTokenProvider.extractGoogleId(token);

            // Google ID로 사용자 조회
            Optional<User> optionalUser = userRepository.findByGoogleId(googleId);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // name과 comment 업데이트
                user.setName(updatedUserInfo.getName());
                user.setComment(updatedUserInfo.getComment());

                // 변경 사항 저장
                userRepository.save(user);

                // 업데이트된 name과 comment만 반환
                Map<String, String> response = new HashMap<>();
                response.put("name", user.getName());
                response.put("comment", user.getComment());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or user information");
        }
    }

    @GetMapping("/get-user-data")
    public ResponseEntity<?> getUserData(@RequestParam Long userId) {
        if (userId == null || userId <= 0) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Invalid userId");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Map<String, Object> result = new HashMap<>();
            result.put("name", user.getName());
            result.put("picture", user.getPicture());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("result", result);

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}