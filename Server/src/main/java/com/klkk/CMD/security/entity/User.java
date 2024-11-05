package com.klkk.CMD.security.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.klkk.CMD.chat.entity.Chat;
import com.klkk.CMD.friendShip.entity.FriendShip;
import com.klkk.CMD.group.entity.GroupMember;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String googleId; // OAuth2 사용자 ID (sub)
    private String name;
    private String email;
    private String picture;

    @Builder.Default
    private String comment = ""; // 상태 메시지의 기본값은 빈 값

    @Enumerated(EnumType.STRING)
    private Role role; // 사용자 권한 (ADMIN, USER)

    // 친구 요청 보낸 목록
    @OneToMany(mappedBy = "sender")
    @JsonManagedReference
    private List<FriendShip> sentFriendRequests = new ArrayList<>();

    // 친구 요청 받은 목록
    @OneToMany(mappedBy = "receiver")
    @JsonManagedReference
    private List<FriendShip> receivedFriendRequests = new ArrayList<>();

    // 사용자가 보낸 채팅 메시지
    @OneToMany(mappedBy = "chatSender")
    @JsonManagedReference
    private List<Chat> messages = new ArrayList<>();

    // 사용자가 속한 그룹 멤버 목록
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<GroupMember> groupMembers = new ArrayList<>();
}