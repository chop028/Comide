package com.klkk.CMD.notification.repository;

import com.klkk.CMD.notification.entity.CommentNotification;

import java.util.List;

public interface CommentNotificationRepository extends NotificationRepository<CommentNotification> {
    List<CommentNotification> findByRecipientId(Long recipientId);
}