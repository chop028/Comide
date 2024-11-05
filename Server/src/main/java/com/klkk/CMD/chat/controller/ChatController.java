package com.klkk.CMD.chat.controller;

import com.klkk.CMD.chat.dto.ChatDto;
import com.klkk.CMD.chat.entity.Chat;
import com.klkk.CMD.chat.entity.ChatRoom;
import com.klkk.CMD.chat.repository.ChatRoomRepository;
import com.klkk.CMD.chat.service.chat.ChatService;
import com.klkk.CMD.notification.service.NotificationService;
import com.klkk.CMD.notification.settings.entity.NotificationSettings;
import com.klkk.CMD.notification.settings.service.NotificationSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ChatController {

    private final SimpMessageSendingOperations template;

    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final NotificationService notificationService;
    private final NotificationSettingsService notificationSettingsService;


    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatDto chat, SimpMessageHeaderAccessor headerAccessor) {
        chatService.increaseUserCount(chat.getRoomId());

        Long userId = chat.getChatSender();
        chatService.addUser(chat.getRoomId(), userId, chat.getSenderName());

        headerAccessor.getSessionAttributes().put("userId", userId);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

        chat.setMessage(chat.getChatSender() + " 님 입장!!");
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
    }

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatDto chatDto) {
        // 채팅 메시지 처리
        Chat chat = chatService.processChatMessage(chatDto);

        // 채팅방 정보 가져오기
        ChatRoom chatRoom = chatRoomRepository.findById(chatDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        // 개인 채팅인 경우
        if (chatRoom.isPrivate()) {
            notificationService.sendPrivateChatNotification(chatDto.getRoomId(), chatDto.getSenderName(), chatDto.getChatSender(), chatDto.getMessage());
        } else {
            notificationService.sendGroupChatNotification(chatRoom.getRoomName(), chatDto.getRoomId(), chatDto.getChatSender(), chatDto.getMessage());
        }

        // 채팅 메시지를 해당 채팅방의 구독자들에게 전송
        template.convertAndSend("/sub/chat/room/" + chatDto.getRoomId(), chatDto);
    }


    @GetMapping("/chat/history")
    public ResponseEntity<List<ChatDto>> getChatHistory(@RequestParam String roomId) {
        List<ChatDto> chatHistory = chatService.getChatHistory(roomId);
        return ResponseEntity.ok(chatHistory);
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        if (roomId != null && userId != null) {
            try {
                chatService.decreaseUserCount(roomId);

                String username = chatService.getUserName(roomId, userId);
                chatService.removeUser(roomId, userId);

                if (username != null) {
                    ChatDto chat = ChatDto.builder()
                            .type(ChatDto.MessageType.LEAVE)
                            .chatSender(userId)
                            .message(username + " 님 퇴장!!")
                            .build();

                    template.convertAndSend("/sub/chat/room/" + roomId, chat);
                }
            } catch (Exception e) {
                System.err.println("Error handling disconnect event: " + e.getMessage());
            }
        } else {
            System.err.println("Error: userId or roomId is null on disconnect event.");
        }
    }

    @GetMapping("/chat/userlist")
    @ResponseBody
    public ArrayList<String> userList(String roomId) {
        return chatService.getUserList(roomId);
    }

    @GetMapping("/chat/duplicateName")
    @ResponseBody
    public String isDuplicateName(@RequestParam("roomId") String roomId, @RequestParam("username") String username) {
        String userName = chatService.ensureUniqueUserName(roomId, username);
        return userName;
    }

    @GetMapping("/chat/isPrivate")
    public ResponseEntity<Boolean> checkIfRoomIsPrivate(@RequestParam String roomId) {
        boolean isPrivate = chatService.isPrivateRoom(roomId);
        return ResponseEntity.ok(isPrivate);
    }
}