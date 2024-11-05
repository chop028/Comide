package com.klkk.CMD.friendShip.controller;

import com.klkk.CMD.friendShip.dto.FriendDto;
import com.klkk.CMD.friendShip.dto.WaitingFriendListDto;
import com.klkk.CMD.friendShip.entity.FriendShip;
import com.klkk.CMD.friendShip.service.FriendShipService;
import com.klkk.CMD.security.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friendship")
@RequiredArgsConstructor
public class FriendShipController {

    private static final Logger logger = LoggerFactory.getLogger(FriendShipController.class);
    private final FriendShipService friendShipService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam Long userId, @RequestParam String name) {
        List<User> users = friendShipService.searchUsersByName(userId, name);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/request")
    public ResponseEntity<FriendShip> sendFriendRequest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        FriendShip friendShip = friendShipService.sendFriendRequest(senderId, receiverId);
        if (friendShip == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(friendShip);
    }

    @GetMapping("/requests/{userId}")
    public ResponseEntity<List<WaitingFriendListDto>> getFriendRequests(@PathVariable Long userId) {
        List<FriendShip> friendRequests = friendShipService.getFriendRequests(userId);
        List<WaitingFriendListDto> dtos = friendRequests.stream().map(friendShip -> WaitingFriendListDto.builder()
                .friendShipId(friendShip.getId())
                .friendEmail(friendShip.getSender().getEmail())
                .friendName(friendShip.getSender().getName())
                .status(friendShip.getStatus())
                .picture(friendShip.getSender().getPicture())
                .build()).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/respond")
    public ResponseEntity<FriendShip> respondToFriendRequest(@RequestParam Long requestId, @RequestParam boolean accept) {
        FriendShip friendShip = friendShipService.respondToFriendRequest(requestId, accept);
        return ResponseEntity.ok(friendShip);
    }

    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<FriendDto>> getFriends(@PathVariable Long userId) {
        List<FriendShip> friends = friendShipService.getFriends(userId);
        List<FriendDto> dtos = friends.stream().map(friendShip -> {
            User friend = friendShip.getSender().getId().equals(userId) ? friendShip.getReceiver() : friendShip.getSender();
            return FriendDto.builder()
                    .id(friend.getId())
                    .name(friend.getName())
                    .picture(friend.getPicture())
                    .comment(friend.getComment())
                    .build();
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
