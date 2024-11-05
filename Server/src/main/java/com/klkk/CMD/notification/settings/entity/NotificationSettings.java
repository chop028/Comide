package com.klkk.CMD.notification.settings.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationSettings {

    @Id
    private Long userId;

    @Builder.Default
    private boolean siteAlertsEnabled = true;

    @Builder.Default
    private boolean chatAlertsEnabled = true;

    @Builder.Default
    private boolean previousChatAlertsEnabled = true;  // 이전 채팅 알림 상태
}
