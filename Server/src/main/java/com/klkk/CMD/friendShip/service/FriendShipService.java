package com.klkk.CMD.friendShip.service;

import com.klkk.CMD.friendShip.dto.enums.FriendShipStatus;
import com.klkk.CMD.friendShip.entity.FriendShip;
import com.klkk.CMD.friendShip.repository.FriendShipRepository;
import com.klkk.CMD.notification.service.NotificationService;
import com.klkk.CMD.security.entity.User;
import com.klkk.CMD.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendShipService {

    private final FriendShipRepository friendShipRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public List<User> searchUsersByName(Long userId, String name) {
        Optional<User> currentUser = userRepository.findById(userId);
        if (currentUser.isEmpty()) {
            return List.of();
        }

        List<User> users = userRepository.findByNameContaining(name);
        return users.stream()
                .filter(user -> !user.getId().equals(userId) && !isFriendOrPending(userId, user.getId()))
                .toList();
    }

    @Transactional
    public FriendShip sendFriendRequest(Long senderId, Long receiverId) {
        // 자기 자신에게 요청 또는 이미 친구 상태 여부 확인
        if (senderId.equals(receiverId) || isFriendOrPending(senderId, receiverId)) {
            return null;
        }

        // 사용자 정보 조회
        Optional<User> sender = userRepository.findById(senderId);
        Optional<User> receiver = userRepository.findById(receiverId);

        if (sender.isPresent() && receiver.isPresent()) {
            // 친구 요청 생성 및 저장
            FriendShip friendShip = FriendShip.builder()
                    .sender(sender.get())
                    .receiver(receiver.get())
                    .status(FriendShipStatus.PENDING)
                    .build();
            FriendShip savedFriendShip = friendShipRepository.save(friendShip);

            // 친구 요청 알림 전송
            notificationService.createFriendRequestNotification("새로운 친구 요청이 도착했습니다.", receiverId);

            return savedFriendShip;
        }
        return null;
    }

    public List<FriendShip> getFriendRequests(Long userId) {
        Optional<User> receiver = userRepository.findById(userId);
        return receiver.map(user -> friendShipRepository.findByReceiverAndStatus(user, FriendShipStatus.PENDING))
                .orElse(Collections.emptyList());
    }

    @Transactional
    public FriendShip respondToFriendRequest(Long requestId, boolean accept) {
        Optional<FriendShip> friendShip = friendShipRepository.findById(requestId);

        if (friendShip.isPresent()) {
            FriendShip fs = friendShip.get();

            if (accept) {
                // 요청을 수락한 경우, 상태를 ACCEPTED로 업데이트합니다.
                fs.setStatus(FriendShipStatus.ACCEPTED);
                return friendShipRepository.save(fs);
            } else {
                // 요청을 거절한 경우, FriendShip 엔티티를 삭제합니다.
                friendShipRepository.delete(fs);
                return null;  // 삭제되었으므로 반환할 엔티티가 없습니다.
            }
        }

        return null;
    }

    public List<FriendShip> getFriends(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User member = user.get();
            List<FriendShip> sentFriends = friendShipRepository.findBySenderAndStatus(member, FriendShipStatus.ACCEPTED);
            List<FriendShip> receivedFriends = friendShipRepository.findByReceiverAndStatus(member, FriendShipStatus.ACCEPTED);

            List<FriendShip> allFriends = new ArrayList<>();
            allFriends.addAll(sentFriends);
            allFriends.addAll(receivedFriends);

            return allFriends;
        }
        return new ArrayList<>();
    }

    private boolean isFriendOrPending(Long userId, Long potentialFriendId) {
        User user = userRepository.getById(userId);
        List<FriendShip> sentRequests = friendShipRepository.findBySenderOrReceiverAndStatus(user, user, FriendShipStatus.PENDING);
        List<FriendShip> acceptedFriends = friendShipRepository.findBySenderOrReceiverAndStatus(user, user, FriendShipStatus.ACCEPTED);

        return sentRequests.stream().anyMatch(f -> f.getSender().getId().equals(potentialFriendId) || f.getReceiver().getId().equals(potentialFriendId)) ||
                acceptedFriends.stream().anyMatch(f -> f.getSender().getId().equals(potentialFriendId) || f.getReceiver().getId().equals(potentialFriendId));
    }
}