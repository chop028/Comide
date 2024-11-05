package com.klkk.CMD.friendShip.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.klkk.CMD.friendShip.dto.enums.FriendShipStatus;
import com.klkk.CMD.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "friendship")  // 테이블 이름 소문자 처리 권장
public class FriendShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonBackReference
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    @JsonBackReference
    private User receiver;

    @Enumerated(EnumType.STRING)
    private FriendShipStatus status;
}
