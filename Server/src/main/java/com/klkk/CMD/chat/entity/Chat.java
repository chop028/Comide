package com.klkk.CMD.chat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.klkk.CMD.chat.dto.ChatDto.MessageType;
import com.klkk.CMD.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType type; // 메시지 타입

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonBackReference
    private ChatRoom chatRoom; // 방 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_sender")
    @JsonBackReference
    private User chatSender;

    private String message; // 메시지
    private LocalDateTime sendTime; // 채팅 발송 시간
    private Long codeId;
}