package com.klkk.CMD.notification.repository;

import com.klkk.CMD.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface NotificationRepository<T extends Notification> extends JpaRepository<T, Long> {
}