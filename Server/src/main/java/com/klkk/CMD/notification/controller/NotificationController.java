package com.klkk.CMD.notification.controller;

import com.klkk.CMD.notification.entity.Notification;
import com.klkk.CMD.notification.nums.NotificationType;
import com.klkk.CMD.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // 1. 사용자 모든 알림 가져오기
    @GetMapping("/all/{userId}")
    public List<? extends Notification> getAllNotifications(@PathVariable Long userId) {
        return notificationService.getAllNotificationsByUserId(userId);
    }

    // 2. 알림 읽음 처리
    @PostMapping("/read/{notificationId}")
    public ResponseEntity<?> markNotificationAsRead(
            @PathVariable Long notificationId,
            @RequestParam NotificationType notificationType) {
        notificationService.markNotificationAsRead(notificationId, notificationType);
        return ResponseEntity.ok().build();
    }
}