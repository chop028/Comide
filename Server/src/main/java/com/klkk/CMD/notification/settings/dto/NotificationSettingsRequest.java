package com.klkk.CMD.notification.settings.dto;

import lombok.Data;

@Data
public class NotificationSettingsRequest {
    private boolean siteAlertsEnabled;
    private boolean chatAlertsEnabled;
}