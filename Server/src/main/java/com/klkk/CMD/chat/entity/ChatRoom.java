package com.klkk.CMD.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_rooms")
public class ChatRoom {

    @Id
    private String roomId;

    private String roomName;
    private int userCount;
    private int maxUserCnt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "chat_users", joinColumns = @JoinColumn(name = "room_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "user_name")
    private Map<Long, String> userlist = new HashMap<>();

    private boolean isPrivate;

    @PrePersist
    public void prePersist() {
        roomId = UUID.randomUUID().toString();
    }
}