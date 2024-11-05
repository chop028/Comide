package com.klkk.CMD.notification.entity;

import com.klkk.CMD.notification.nums.NotificationType;
import jakarta.persistence.Entity;

@Entity
public class FriendRequestNotification extends Notification {
    public FriendRequestNotification() {
        super.setType(NotificationType.FRIEND_REQUEST);
    }
}