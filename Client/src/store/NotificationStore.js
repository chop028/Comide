import { defineStore } from 'pinia';
import SockJS from 'sockjs-client';
import { Client as Stomp } from '@stomp/stompjs';
import { useToast } from 'vue-toastification';
import { useAuthStore } from './auth'; // 사용자 정보를 가져오기 위한 auth 스토어

export const useNotificationStore = defineStore('notification', {
  state: () => ({
    notifications: [], // 모든 알림을 저장
    stompClient: null,  // WebSocket 연결 클라이언트
  }),

  actions: {
    // WebSocket 연결 및 구독 설정
    connectToWebSocket() {
      const authStore = useAuthStore(); // auth 스토어에서 사용자 정보를 가져옴
      const userId = authStore.userInfo?.id;

      if (!userId) {
        alert('사용자 정보가 없습니다.');
        return;
      }

      const socket = new SockJS(`${process.env.VUE_APP_WS_URL}`); // 서버 WebSocket URL
      this.stompClient = new Stomp({
        webSocketFactory: () => socket,
      });

      this.stompClient.onConnect = () => {

        this.stompClient.subscribe(`/sub/friend-request/${userId}`, (message) => {
          const notification = JSON.parse(message.body);
          this.addNotification(notification.id, notification.message, notification.notificationType);
        });

        this.stompClient.subscribe(`/sub/group_request/${userId}`, (message) => {
          const notification = JSON.parse(message.body);
          this.addGroupNotification(notification.id, notification.message, notification.notificationType, notification.groupId);
        });

        this.stompClient.subscribe(`/sub/comment/${userId}`, (message) => {
          const notification = JSON.parse(message.body);
          this.addNotification(notification.id, notification.message, notification.notificationType);
        });

        // 사용자 알림 구독 (일반 알림)
        this.stompClient.subscribe(`/sub/notification/${userId}`, (message) => {
          const notification = JSON.parse(message.body);
          this.addNotification(notification.id, notification.message, notification.notificationType);
        });

        // 개인 채팅 알림 구독 (휘발성 알림)
        this.stompClient.subscribe(`/sub/private-chat/${userId}`, (message) => {
          const notification = JSON.parse(message.body);
          this.showToastNotification(notification.message, 'CHAT');
        });

        // 그룹 채팅 알림 구독 (휘발성 알림)
        this.stompClient.subscribe(`/sub/group-chat/${userId}`, (message) => {
          const notification = JSON.parse(message.body);
          this.showToastNotification(notification.message, 'CHAT');
        });
      };

      this.stompClient.activate();
    },

    disconnectWebSocket() {
      if (this.stompClient) {
        this.stompClient.deactivate();
      }
    },

    // 실시간 알림 추가 및 관리
    addNotification(id, message, notificationType) {
      this.notifications.push({ id, message, notificationType, read: false });
      this.showToastNotification(message, notificationType);
    },

    // 실시간 알림 추가 및 관리
    addGroupNotification(id, message, notificationType, groupId = null) {
      this.notifications.push({
        id,
        message,
        notificationType,
        groupId,
        read: false
      });

      this.showToastNotification(message, notificationType);
    },

    removeNotification(id) {
      this.notifications = this.notifications.filter(n => n.id !== id);
    },

    // 토스트 알림 표시 (vue-toastification 사용)
    showToastNotification(message, notificationType) {
      const toast = useToast();

      // 알림 유형에 따라 스타일을 다르게 설정
      switch (notificationType) {
        case 'FRIEND_REQUEST':
          toast.info(`친구 요청: ${message}`, {
            timeout: 5000,
            closeOnClick: true,
            pauseOnHover: true,
            onClick: () => {
              window.location.href = `${process.env.VUE_APP_URL}/mypage/alarm`;
            },
          });
          break;
        case 'GROUP_REQUEST':
          toast.info(`그룹 신청: ${message}`, {
            timeout: 5000,
            closeOnClick: true,
            pauseOnHover: true,
            onClick: () => {
              window.location.href = `${process.env.VUE_APP_URL}/mypage/alarm`;
            },
          });
          break;
        case 'GROUP_JOINED':
        case 'COMMENT':
          toast.info(`댓글 알림: ${message}`, {
            timeout: 5000,
            closeOnClick: true,
            pauseOnHover: true,
            onClick: () => {
              window.location.href = `${process.env.VUE_APP_URL}/mypage/alarm`;
            },
          });
          break;
        case 'CHAT':
          toast.info(`${message}`, {
            timeout: 5000,
            closeOnClick: true,
            pauseOnHover: true,
          });
          break;
        default:
          toast.info(`알림: ${message}`, {
            timeout: 5000,
            closeOnClick: true,
            pauseOnHover: true,
          });
      }
    },
  },

  getters: {
    unreadNotifications: (state) => state.notifications.filter(n => !n.read),
  },
});