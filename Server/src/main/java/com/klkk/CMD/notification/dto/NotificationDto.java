package com.klkk.CMD.notification.dto;

import com.klkk.CMD.notification.nums.NotificationType;
import com.mysql.cj.log.Log;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDto {
    private Long id;

    private String message;

    private NotificationType notificationType;

    private Long groupId;

    public NotificationDto(Long id, String message, NotificationType notificationType) {
        this.id = id;
        this.message = message;
        this.notificationType = notificationType;
    }

    public NotificationDto(String message, NotificationType notificationType) {
        this.message = message;
        this.notificationType = notificationType;
    }

    public NotificationDto(Long id, String message, NotificationType notificationType, Long groupId) {
        this.id = id;
        this.message = message;
        this.notificationType = notificationType;
        this.groupId = groupId;
    }
}