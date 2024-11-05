package com.klkk.CMD.notification.repository;

import com.klkk.CMD.notification.entity.GroupJoinedNotification;

import java.util.List;

public interface GroupJoinedNotificationRepository extends NotificationRepository<GroupJoinedNotification> {
    List<GroupJoinedNotification> findByRecipientId(Long recipientId);
}