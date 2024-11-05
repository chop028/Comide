package com.klkk.CMD.chat.repository;

import com.klkk.CMD.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    ChatRoom findByRoomName(String roomName);

    @Query("SELECT cr FROM ChatRoom cr JOIN cr.userlist ul WHERE KEY(ul) = :userId AND cr.isPrivate = true")
    List<ChatRoom> findPrivateRoomsForUser(@Param("userId") Long userId);
}