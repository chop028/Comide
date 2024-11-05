package com.klkk.CMD.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    public enum MessageType {
        ENTER, TALK, FILE, LEAVE;
    }

    private MessageType type; // 메시지 타입
    private String roomId; // 방 번호
    private Long chatSender; // 채팅을 보낸 사람의 ID
    private String senderName; // 채팅을 보낸 사람의 이름
    private String senderPicture; // 채팅을 보낸 사람의 프로필 사진
    private String message; // 메시지
    private String sendTime; // 채팅 발송 시간
    private Long codeId;
}