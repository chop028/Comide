package com.klkk.CMD.notification.repository;

import com.klkk.CMD.notification.entity.GroupRequestNotification;

import java.util.List;

public interface GroupRequestNotificationRepository extends NotificationRepository<GroupRequestNotification> {
    List<GroupRequestNotification> findByRecipientId(Long recipientId);
}