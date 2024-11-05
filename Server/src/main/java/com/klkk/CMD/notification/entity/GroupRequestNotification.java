package com.klkk.CMD.notification.entity;

import com.klkk.CMD.notification.nums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GroupRequestNotification extends Notification {

    @Column(nullable = false)
    private Long groupId;

    public GroupRequestNotification() {
        super.setType(NotificationType.GROUP_REQUEST);
    }
}
