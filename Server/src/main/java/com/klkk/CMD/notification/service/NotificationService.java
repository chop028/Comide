package com.klkk.CMD.notification.service;

import com.klkk.CMD.chat.service.chat.ChatService;
import com.klkk.CMD.notification.nums.NotificationType;
import com.klkk.CMD.notification.entity.*;
import com.klkk.CMD.notification.repository.*;
import com.klkk.CMD.notification.settings.entity.NotificationSettings;
import com.klkk.CMD.notification.settings.service.NotificationSettingsService;
import com.klkk.CMD.notification.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FriendRequestNotificationRepository friendRequestNotificationRepository;
    private final GroupRequestNotificationRepository groupRequestNotificationRepository;
    private final GroupJoinedNotificationRepository groupJoinedNotificationRepository;
    private final CommentNotificationRepository commentNotificationRepository;
    private final NotificationSettingsService notificationSettingsService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    // 친구 요청 알림 생성 및 실시간 전송
    public void createFriendRequestNotification(String message, Long recipientId) {
        NotificationSettings settings = notificationSettingsService.getNotificationSettings(recipientId);
        if (settings.isSiteAlertsEnabled()) { // 사이트 알림이 켜져 있는 경우에만
            FriendRequestNotification notification = new FriendRequestNotification();
            notification.setMessage(message);
            notification.setRecipientId(recipientId);
            notification.setType(NotificationType.FRIEND_REQUEST);
            friendRequestNotificationRepository.save(notification);

            // WebSocket을 통해 알림 전송 시 id 값을 포함하여 전송
            messagingTemplate.convertAndSend("/sub/friend-request/" + recipientId,
                    new NotificationDto(notification.getId(), message, NotificationType.FRIEND_REQUEST));
        }
    }

    // 그룹 요청 알림 생성 및 실시간 전송
    public void createGroupRequestNotification(String message, Long recipientId, Long groupId) {
        NotificationSettings settings = notificationSettingsService.getNotificationSettings(recipientId);
        if (settings.isSiteAlertsEnabled()) { // 사이트 알림이 켜져 있는 경우에만
            GroupRequestNotification notification = new GroupRequestNotification();
            notification.setMessage(message);
            notification.setRecipientId(recipientId);
            notification.setType(NotificationType.GROUP_REQUEST);
            notification.setGroupId(groupId);
            groupRequestNotificationRepository.save(notification);

            // WebSocket을 통해 실시간 전송
            messagingTemplate.convertAndSend("/sub/group_request/" + recipientId,
                    new NotificationDto(notification.getId(), message, NotificationType.GROUP_REQUEST, groupId)); // 그룹 ID 포함
        }
    }

    // 그룹 가입 알림 생성 및 실시간 전송
    public void createGroupJoinedNotification(String message, Long recipientId) {
        NotificationSettings settings = notificationSettingsService.getNotificationSettings(recipientId);
        if (settings.isSiteAlertsEnabled()) { // 사이트 알림이 켜져 있는 경우에만
            GroupJoinedNotification notification = new GroupJoinedNotification();
            notification.setMessage(message);
            notification.setRecipientId(recipientId);
            notification.setType(NotificationType.GROUP_JOINED);
            groupJoinedNotificationRepository.save(notification);

            // WebSocket을 통해 실시간 전송
            messagingTemplate.convertAndSend("/sub/notification/" + recipientId,
                    new NotificationDto(notification.getId(), message, NotificationType.GROUP_JOINED));
        }
    }

    // 댓글 알림 생성 및 실시간 전송
    public void createCommentNotification(String message, Long recipientId) {
        NotificationSettings settings = notificationSettingsService.getNotificationSettings(recipientId);
        if (settings.isSiteAlertsEnabled()) { // 사이트 알림이 켜져 있는 경우에만
            CommentNotification notification = new CommentNotification();
            notification.setMessage(message);
            notification.setRecipientId(recipientId);
            notification.setType(NotificationType.COMMENT);
            commentNotificationRepository.save(notification);

            // WebSocket을 통해 실시간 전송
            messagingTemplate.convertAndSend("/sub/comment/" + recipientId,
                    new NotificationDto(notification.getId(), message, NotificationType.COMMENT));
        }
    }

    // 실시간 개인 채팅 알림
    public void sendPrivateChatNotification(String roomId, String senderName, Long senderId, String message) {
        // 채팅방에 속한 유저들의 ID를 가져오기 (개인 채팅방)
        List<Long> userIds = chatService.getUserIdList(roomId);

        // 보낸 사람을 제외한 나머지 유저들에게만 알림을 전송
        String chatMessage = senderName + " : " + message;

        // 채팅방에 속한 각 유저에게 알림 전송 (보낸 사람 제외)
        for (Long userId : userIds) {
            if (!userId.equals(senderId)) { // 보낸 사람을 제외하고 전송
                NotificationSettings settings = notificationSettingsService.getNotificationSettings(userId);

                // 사이트 알림과 채팅 알림이 모두 활성화된 경우에만 알림 전송
                if (settings.isSiteAlertsEnabled() && settings.isChatAlertsEnabled()) {
                    messagingTemplate.convertAndSend("/sub/private-chat/" + userId,
                            new NotificationDto(chatMessage, NotificationType.CHAT));
                }
            }
        }
    }

    // 실시간 그룹 채팅 알림
    public void sendGroupChatNotification(String groupName, String roomId, Long senderId, String message) {
        // 채팅방에 포함된 유저들의 ID 가져오기
        List<Long> userIds = chatService.getUserIdList(roomId);

        String chatMessage = groupName + " : " + message;

        // 채팅방에 속한 각 유저에게 알림 전송 (보낸 사람 제외)
        for (Long userId : userIds) {
            if (!userId.equals(senderId)) { // 보낸 사람을 제외하고 전송
                NotificationSettings settings = notificationSettingsService.getNotificationSettings(userId);

                // 사이트 알림과 채팅 알림이 모두 활성화된 경우에만 알림 전송
                if (settings.isSiteAlertsEnabled() && settings.isChatAlertsEnabled()) {
                    messagingTemplate.convertAndSend("/sub/group-chat/" + userId,
                            new NotificationDto(chatMessage, NotificationType.CHAT));
                }
            }
        }
    }

    public List<? extends Notification> getAllNotificationsByUserId(Long userId) {
        List<FriendRequestNotification> friendRequests = friendRequestNotificationRepository.findByRecipientId(userId);
        List<GroupRequestNotification> groupRequests = groupRequestNotificationRepository.findByRecipientId(userId);
        List<GroupJoinedNotification> groupJoined = groupJoinedNotificationRepository.findByRecipientId(userId);
        List<CommentNotification> comments = commentNotificationRepository.findByRecipientId(userId);

        // 모든 알림을 하나의 리스트로 합친 후, 최신순으로 정렬
        return Stream.of(friendRequests, groupRequests, groupJoined, comments)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())  // 최신순 정렬
                .collect(Collectors.toList());
    }

    // 알림 읽음 처리
    public void markNotificationAsRead(Long notificationId, NotificationType notificationType) {
        Optional<? extends Notification> notification = findNotificationById(notificationId, notificationType);

        notification.ifPresent(n -> {
            if (!n.isRead()) {
                n.setRead(true);
                saveNotification(n, notificationType);  // 알림 저장
            }
        });
    }

    // 알림을 ID와 타입으로 조회
    private Optional<? extends Notification> findNotificationById(Long notificationId, NotificationType type) {
        switch (type) {
            case FRIEND_REQUEST:
                return friendRequestNotificationRepository.findById(notificationId);
            case GROUP_REQUEST:
                return groupRequestNotificationRepository.findById(notificationId);
            case GROUP_JOINED:
                return groupJoinedNotificationRepository.findById(notificationId);
            case COMMENT:
                return commentNotificationRepository.findById(notificationId);
            default:
                throw new IllegalArgumentException("알 수 없는 알림 타입입니다.");
        }
    }

    // 알림 저장
    private void saveNotification(Notification notification, NotificationType type) {
        switch (type) {
            case FRIEND_REQUEST:
                friendRequestNotificationRepository.save((FriendRequestNotification) notification);
                break;
            case GROUP_REQUEST:
                groupRequestNotificationRepository.save((GroupRequestNotification) notification);
                break;
            case GROUP_JOINED:
                groupJoinedNotificationRepository.save((GroupJoinedNotification) notification);
                break;
            case COMMENT:
                commentNotificationRepository.save((CommentNotification) notification);
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 알림 타입입니다.");
        }
    }
}