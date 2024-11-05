package com.klkk.CMD.notification.entity;

import com.klkk.CMD.notification.nums.NotificationType;
import jakarta.persistence.Entity;

@Entity
public class GroupJoinedNotification extends Notification {
    public GroupJoinedNotification() {
        super.setType(NotificationType.GROUP_JOINED);
    }
}