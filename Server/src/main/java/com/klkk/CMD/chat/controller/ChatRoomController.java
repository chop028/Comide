package com.klkk.CMD.chat.controller;

import com.klkk.CMD.chat.dto.ChatRoomDto;
import com.klkk.CMD.chat.service.chat.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatRoomController {

    private final ChatService chatService; // ChatService 주입

    public ChatRoomController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public ResponseEntity<List<ChatRoomDto>> goChatRooms() {
        List<ChatRoomDto> chatRooms = chatService.findAllRooms();

        return ResponseEntity.ok().body(chatRooms);
    }

    @PostMapping("/chat/createroom")
    public ResponseEntity<ChatRoomDto> createRoom(@RequestParam("roomName") String name, @RequestParam(value = "maxUserCnt", defaultValue = "100") int maxUserCnt) {
        ChatRoomDto room = chatService.createChatRoom(name, maxUserCnt);

        return ResponseEntity.ok(room);
    }

    @PostMapping("/chat/private")
    public ResponseEntity<ChatRoomDto> getOrCreatePrivateRoom(@RequestParam Long userId1, @RequestParam Long userId2) {
        ChatRoomDto room = chatService.findOrCreatePrivateRoom(userId1, userId2);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/chat/private-rooms/{userId}")
    public ResponseEntity<List<ChatRoomDto>> getPrivateChatRooms(@PathVariable Long userId) {
        List<ChatRoomDto> privateRooms = chatService.getPrivateChatRoomsForUser(userId);
        return ResponseEntity.ok(privateRooms);
    }

    @GetMapping("/chat/room")
    public ResponseEntity<ChatRoomDto> roomDetail(@RequestParam String roomId) {

        ChatRoomDto room = chatService.findRoomById(roomId);

        if (room != null) {
            return ResponseEntity.ok().body(room);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/chat/chkUserCnt")
    public ResponseEntity<Boolean> checkUserCount(@RequestParam String roomId) {
        boolean canUserJoin = chatService.isRoomUserCountValid(roomId);

        return ResponseEntity.ok().body(canUserJoin);
    }
}