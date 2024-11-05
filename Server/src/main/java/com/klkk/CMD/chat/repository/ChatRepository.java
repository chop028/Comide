package com.klkk.CMD.chat.repository;

import com.klkk.CMD.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByChatRoom_RoomId(String roomId);

    @Query("SELECT c FROM Chat c WHERE c.chatRoom.roomId = :roomId ORDER BY c.sendTime DESC")
    List<Chat> findLastChatByRoomId(String roomId);

    Optional<Chat> findFirstByChatRoom_RoomIdOrderBySendTimeDesc(String roomId);
}