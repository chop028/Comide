package com.klkk.CMD.security.service;

import com.klkk.CMD.notification.settings.entity.NotificationSettings;
import com.klkk.CMD.notification.settings.repository.NotificationSettingsRepository;
import com.klkk.CMD.security.dto.CustomOAuth2User;
import com.klkk.CMD.security.entity.Role;
import com.klkk.CMD.security.entity.User;
import com.klkk.CMD.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final NotificationSettingsRepository notificationSettingsRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String googleId = oAuth2User.getAttribute("sub");

        // Optional로 받은 user 처리
        Optional<User> userOptional = userRepository.findByGoogleId(googleId);

        User user = userOptional.orElseGet(() -> {
            // 유저가 없으면 새로 생성하여 저장
            User newUser = User.builder()
                    .googleId(googleId)
                    .name(oAuth2User.getAttribute("name"))
                    .email(oAuth2User.getAttribute("email"))
                    .picture(oAuth2User.getAttribute("picture"))
                    .role(Role.ROLE_USER)
                    .build();
            User savedUser = userRepository.save(newUser);

            // 새로운 사용자에 대해 기본 알림 설정 생성 (두 알림 모두 ON)
            NotificationSettings defaultSettings = NotificationSettings.builder()
                    .userId(savedUser.getId())
                    .siteAlertsEnabled(true) // 사이트 전체 알림 ON
                    .chatAlertsEnabled(true) // 채팅 알림 ON
                    .previousChatAlertsEnabled(true) // 초기 설정은 채팅 알림도 ON
                    .build();
            notificationSettingsRepository.save(defaultSettings);

            return savedUser;
        });

        // 유저 엔티티의 모든 필드를 포함한 CustomOAuth2User 생성
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return new CustomOAuth2User(user, attributes);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}