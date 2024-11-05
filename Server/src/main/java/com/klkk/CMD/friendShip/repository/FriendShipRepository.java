package com.klkk.CMD.friendShip.repository;

import com.klkk.CMD.friendShip.dto.enums.FriendShipStatus;
import com.klkk.CMD.friendShip.entity.FriendShip;
import com.klkk.CMD.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {
    List<FriendShip> findByReceiverAndStatus(User receiver, FriendShipStatus status);
    List<FriendShip> findBySenderAndStatus(User sender, FriendShipStatus status);
    List<FriendShip> findBySenderOrReceiverAndStatus(User sender, User receiver, FriendShipStatus status);
}