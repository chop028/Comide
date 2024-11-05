package com.klkk.CMD.notification.settings.controller;

import com.klkk.CMD.notification.settings.dto.NotificationSettingsRequest;
import com.klkk.CMD.notification.settings.entity.NotificationSettings;
import com.klkk.CMD.notification.settings.service.NotificationSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications/settings")
public class NotificationSettingsController {

    private final NotificationSettingsService notificationSettingsService;

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateNotificationSettings(@PathVariable Long userId, @RequestBody NotificationSettingsRequest request) {
        notificationSettingsService.updateNotificationSettings(userId, request.isSiteAlertsEnabled(), request.isChatAlertsEnabled());
        return ResponseEntity.ok("알림 설정이 업데이트되었습니다.");
    }

    // 알림 설정 조회 (옵션)
    @GetMapping("/{userId}")
    public ResponseEntity<NotificationSettings> getNotificationSettings(@PathVariable Long userId) {
        NotificationSettings settings = notificationSettingsService.getNotificationSettings(userId);
        return ResponseEntity.ok(settings);
    }
}