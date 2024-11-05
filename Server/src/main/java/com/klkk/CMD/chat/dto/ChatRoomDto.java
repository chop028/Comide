package com.klkk.CMD.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomDto {
    private String roomId; // 채팅방 아이디
    private String roomName; // 채팅방 이름
    private int userCount; // 채팅방 인원수
    private int maxUserCnt; // 채팅방 최대 인원 제한

    private boolean isPrivate;

    private String userPicture; // 상대방 프로필 사진
    private String userName; // 상대방 이름

    private String lastChat;
}