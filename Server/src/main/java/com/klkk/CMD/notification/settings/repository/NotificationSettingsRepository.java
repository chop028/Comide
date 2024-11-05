package com.klkk.CMD.notification.settings.repository;

import com.klkk.CMD.notification.settings.entity.NotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Long> {
}