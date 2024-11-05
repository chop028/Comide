package com.klkk.CMD.notification.repository;

import com.klkk.CMD.notification.entity.FriendRequestNotification;

import java.util.List;

public interface FriendRequestNotificationRepository extends NotificationRepository<FriendRequestNotification> {
    List<FriendRequestNotification> findByRecipientId(Long recipientId);
}