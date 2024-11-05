package com.klkk.CMD.notification.settings.service;

import com.klkk.CMD.notification.settings.entity.NotificationSettings;
import com.klkk.CMD.notification.settings.repository.NotificationSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSettingsService {

    private final NotificationSettingsRepository notificationSettingsRepository;

    // 알림 설정 가져오기 (없으면 기본 설정 생성)
    public NotificationSettings getNotificationSettings(Long userId) {
        return notificationSettingsRepository.findById(userId)
                .orElseGet(() -> {
                    // 기본 설정
                    NotificationSettings settings = NotificationSettings.builder()
                            .userId(userId)
                            .siteAlertsEnabled(true)   // 기본값: 사이트 알림 ON
                            .chatAlertsEnabled(true)   // 기본값: 채팅 알림 ON
                            .previousChatAlertsEnabled(true)  // 기본값: 이전 채팅 알림 ON
                            .build();
                    return notificationSettingsRepository.save(settings);
                });
    }

    // 알림 설정 업데이트
    public void updateNotificationSettings(Long userId, boolean siteAlertsEnabled, boolean chatAlertsEnabled) {
        NotificationSettings settings = getNotificationSettings(userId);

        if (!siteAlertsEnabled) {
            // 사이트 알림을 끌 때 현재 채팅 알림 상태를 저장하고, 채팅 알림을 끔
            settings.setPreviousChatAlertsEnabled(settings.isChatAlertsEnabled());  // 현재 채팅 알림 상태를 저장
            settings.setChatAlertsEnabled(false);  // 사이트 알림이 꺼지면 채팅 알림도 꺼짐
        } else {
            // 사이트 알림을 켤 때 이전 상태로 복원
            settings.setChatAlertsEnabled(settings.isPreviousChatAlertsEnabled());
        }

        // 채팅 알림 ON/OFF가 사용자가 명시적으로 변경한 경우
        if (siteAlertsEnabled && chatAlertsEnabled != settings.isChatAlertsEnabled()) {
            settings.setPreviousChatAlertsEnabled(chatAlertsEnabled);  // 채팅 알림 변경 시 이전 상태 업데이트
            settings.setChatAlertsEnabled(chatAlertsEnabled);
        }

        settings.setSiteAlertsEnabled(siteAlertsEnabled);  // 사이트 알림 상태 업데이트
        notificationSettingsRepository.save(settings);  // 데이터베이스에 저장
    }
}
