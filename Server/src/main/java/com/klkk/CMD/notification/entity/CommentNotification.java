package com.klkk.CMD.notification.entity;

import com.klkk.CMD.notification.nums.NotificationType;
import jakarta.persistence.Entity;


@Entity
public class CommentNotification extends Notification {
    public CommentNotification() {
        super.setType(NotificationType.COMMENT);
    }
}