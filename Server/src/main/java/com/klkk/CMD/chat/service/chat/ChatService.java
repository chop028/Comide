package com.klkk.CMD.chat.service.chat;

import com.klkk.CMD.chat.dto.ChatDto;
import com.klkk.CMD.chat.dto.ChatRoomDto;
import com.klkk.CMD.chat.entity.Chat;
import com.klkk.CMD.chat.entity.ChatRoom;
import com.klkk.CMD.chat.repository.ChatRepository;
import com.klkk.CMD.chat.repository.ChatRoomRepository;
import com.klkk.CMD.security.entity.User;
import com.klkk.CMD.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRoomRepository chatRoomRepository, ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    // 전체 채팅방 조회
    public List<ChatRoomDto> findAllRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        Collections.reverse(chatRooms);
        return chatRooms.stream().map(this::convertToDto).toList();
    }

    // roomID 기준으로 채팅방 찾기
    public ChatRoomDto findRoomById(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
        return convertToDto(chatRoom);
    }

    // roomName으로 채팅방 만들기
    public ChatRoomDto createChatRoom(String roomName, int maxUserCnt) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(roomName)
                .userlist(new HashMap<Long, String>())
                .userCount(0) // 채팅방 참여 인원수
                .maxUserCnt(maxUserCnt) // 최대 인원수 제한
                .build();

        chatRoom = chatRoomRepository.save(chatRoom);
        return convertToDto(chatRoom);
    }

    @Transactional
    public ChatRoomDto findOrCreatePrivateRoom(Long userId1, Long userId2) {
        // 사용자 ID를 정렬하여 일관된 채팅방 이름 생성
        String roomName = userId1 < userId2 ? userId1 + "-" + userId2 : userId2 + "-" + userId1;
        ChatRoom chatRoom = chatRoomRepository.findByRoomName(roomName);

        if (chatRoom == null) {
            chatRoom = ChatRoom.builder()
                    .roomName(roomName)
                    .userlist(new HashMap<Long, String>())
                    .userCount(2)
                    .maxUserCnt(2)
                    .isPrivate(true)  // 1대1 채팅방을 나타내는 속성 추가
                    .build();
            chatRoom = chatRoomRepository.save(chatRoom);

            // 두 사용자를 채팅방에 추가
            addUser(chatRoom.getRoomId(), userId1, getUserNameById(userId1));
            addUser(chatRoom.getRoomId(), userId2, getUserNameById(userId2));
        }

        return convertToDto(chatRoom);
    }

    public String getUserNameById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        return user.getName();
    }

    public List<ChatRoomDto> getPrivateChatRoomsForUser(Long userId) {
        List<ChatRoom> privateRooms = chatRoomRepository.findPrivateRoomsForUser(userId);
        return privateRooms.stream().map(chatRoom -> {
            // 상대방 ID 가져오기
            Long otherUserId = chatRoom.getUserlist().keySet().stream().filter(id -> !id.equals(userId)).findFirst().orElse(null);
            if (otherUserId != null) {
                User otherUser = userRepository.findById(otherUserId).orElse(null);
                if (otherUser != null) {
                    Chat lastChat = chatRepository.findFirstByChatRoom_RoomIdOrderBySendTimeDesc(chatRoom.getRoomId()).orElse(null);
                    String lastMessage = (lastChat != null && lastChat.getMessage() != null) ? lastChat.getMessage() : "";

                    return ChatRoomDto.builder()
                            .roomId(chatRoom.getRoomId())
                            .roomName(chatRoom.getRoomName())
                            .userCount(chatRoom.getUserCount())
                            .maxUserCnt(chatRoom.getMaxUserCnt())
                            .isPrivate(chatRoom.isPrivate())
                            .userPicture(otherUser.getPicture())
                            .userName(chatRoom.getUserlist().get(otherUserId))
                            .lastChat(lastMessage.length() > 10 ? lastMessage.substring(0, 10) + "..." : lastMessage)
                            .build();
                }
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }


    // 채팅방 인원 +1
    public void increaseUserCount(String roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        room.setUserCount(room.getUserCount() + 1);
        chatRoomRepository.save(room);
    }

    // 채팅방 인원 -1
    public void decreaseUserCount(String roomId) {
        if (roomId == null) {
            throw new IllegalArgumentException("roomId must not be null");
        }

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        room.setUserCount(room.getUserCount() - 1);
        chatRoomRepository.save(room);
    }

    // maxUserCnt에 따른 채팅방 입장 여부 확인
    public boolean isRoomUserCountValid(String roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        return room.getUserCount() + 1 <= room.getMaxUserCnt();
    }

    // 채팅방 유저 추가
    public void addUser(String roomId, Long userId, String userName) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        room.getUserlist().put(userId, userName);
        chatRoomRepository.save(room);
    }

    // 채팅방 유저 이름 중복 확인 및 수정
    public String ensureUniqueUserName(String roomId, String username) {
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Chat room not found"));
        String tmp = username;
        while (room.getUserlist().containsValue(tmp)) {
            int ranNum = new Random().nextInt(100) + 1;
            tmp = username + ranNum;
        }
        return tmp;
    }

    // 채팅방 유저 삭제
    public void removeUser(String roomId, Long userId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        room.getUserlist().remove(userId);
        chatRoomRepository.save(room);
    }

    // 채팅방 유저 이름 조회
    public String getUserName(String roomId, Long userId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        return room.getUserlist().get(userId);
    }

    // 채팅방 전체 유저 리스트 조회
    public ArrayList<String> getUserList(String roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        return new ArrayList<>(room.getUserlist().values());
    }

    // 유저 ID 리스트 가져오기
    public List<Long> getUserIdList(String roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        // userlist의 키셋(Long값)을 ArrayList로 변환하여 반환
        Set<Long> userIds = room.getUserlist().keySet();
        return new ArrayList<>(userIds);
    }

    @Transactional
    public Chat processChatMessage(ChatDto chatDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 없습니다."));

        User sender = userRepository.findById(chatDto.getChatSender())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Chat chat = Chat.builder()
                .type(chatDto.getType())
                .chatSender(sender)
                .message(chatDto.getMessage())
                .sendTime(LocalDateTime.now())
                .chatRoom(chatRoom)
                .codeId(chatDto.getCodeId())
                .build();

        chatRepository.save(chat);

        chatDto.setSenderName(sender.getName());
        chatDto.setSenderPicture(sender.getPicture());

        return chat;
    }

    public List<ChatDto> getChatHistory(String roomId) {
        List<Chat> chatHistory = chatRepository.findByChatRoom_RoomId(roomId);
        return chatHistory.stream().map(chat -> {
            User sender = userRepository.findById(chat.getChatSender().getId())
                    .orElse(null);

            return ChatDto.builder()
                    .roomId(chat.getChatRoom().getRoomId())
                    .chatSender(chat.getChatSender().getId()) // ID만 반환
                    .senderName(sender != null ? sender.getName() : "Unknown")
                    .senderPicture(sender != null ? sender.getPicture() : null)
                    .message(chat.getMessage())
                    .sendTime(chat.getSendTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                    .type(chat.getType())
                    .codeId(chat.getCodeId())
                    .build();
        }).collect(Collectors.toList());
    }

    // 엔티티를 DTO로 변환
    private ChatRoomDto convertToDto(ChatRoom chatRoom) {
        if (chatRoom == null) {
            return null;
        }
        return ChatRoomDto.builder()
                .roomId(chatRoom.getRoomId())
                .roomName(chatRoom.getRoomName())
                .userCount(chatRoom.getUserCount())
                .maxUserCnt(chatRoom.getMaxUserCnt())
                .isPrivate(chatRoom.isPrivate())
                .build();
    }

    @Transactional
    public boolean isPrivateRoom(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid roomId: " + roomId));
        return chatRoom.isPrivate();
    }
}