<template>
  <FriendModals
      :add_modal="add_modal"
      :friend_alarm_modal="friend_alarm_modal"
      :friendview_modal="friendview_modal"
      :searchQuery="searchQuery"
      :searchResults="searchResults"
      :add_alarm="add_alarm"
      :selectedFriend="selectedFriend"
      @search-friends="searchFriends"
      @send-friend-request="sendFriendRequest"
      @close-friend-alarm-modal="closeFriendAlarmModal"
      @approve-friendship="approveFriendship"
      @reject-friendship="rejectFriendship"
      @move-private-chatting="movePrivateChatting"
      @close-add-modal="closeAddModal"
    />
  <div class="friend-listbox">
    <div class="topbar">
      <span style="font-size: 33px; font-weight: bold; color: #515151; padding-left: 30px;">친구</span>
      <div class="addFriends" @click="add_modal = true"><img src="../../assets/add.svg" width="30px" height="30px" style="cursor: pointer;"></div>
      <div class="acceptFriendsRequest" @click="openFriendAlarmModal">
        <img src="../../assets/person.svg" width="25px" height="25px" style="cursor: pointer;">
        <span v-if="friendRequestCount > 0" class="badge">{{ friendRequestCount }}</span>
      </div>
    </div>
    <div class="userbox">
      <div class="profilebox">
        <img :src="userInfo.picture || '../../assets/profile.svg'" width="55px" height="55px" style="border-radius: 25px; display: block;">
      </div>
      <div class="namebox"><span style="font-size: 25px; font-weight: bold; color: #575757; letter-spacing: 1px;">{{ userInfo.name }}</span></div>
      <div class="status_messagebox"><span style="color: #575757; font-size: 13px;">{{ userInfo.comment }}</span></div>
    </div>
    <!-- <div class="divbox1"><span style="color: #7D7D7D; font-size: 13px;">즐겨찾기</span></div>
    <div class="favorite_listbox">
      <div class="profile profile1"><img src="../../assets/profile.svg" width="55px" height="55px"></div>
      <div class="profile profile2"><img src="../../assets/profile.svg" width="55px" height="55px"></div>
      <div class="profile profile3"><img src="../../assets/profile.svg" width="55px" height="55px"></div>
      <div class="profile profile4"><img src="../../assets/profile.svg" width="55px" height="55px"></div>
      <div class="profile profile5"><img src="../../assets/profile.svg" width="55px" height="55px"></div>
    </div> -->
    <div class="divbox2"><span style="color: #7D7D7D; font-size: 13px;">친구 {{ friend_list.length }}</span></div>
    <div class="friend-list">
      <ul>
        <li v-for="(friend, index) in friend_list" :key="index" class="friend" @click="showFriendDetails(friend)">
          <div class="profilebox"><img :src="friend.picture || '../../assets/profile.svg'" width="55px" height="55px" style="border-radius: 25px;"></div>
          <div class="namebox"><span style="font-size: 22px; font-weight: bold; color: #575757; letter-spacing: 1px;">{{ friend.name }}</span></div>
          <div class="status_messagebox"><span style="color: #575757; font-size: 14px;">{{ friend.comment }}</span></div>
        </li>
      </ul>
    </div>
    <div class="navigationbar">
      <div class="nbox" @click="movefriend"><img class="neviImg" src="../../assets/people-fill.svg" width="30px" height="30px" style="filter: invert(76%) sepia(23%) saturate(6021%) hue-rotate(312deg) brightness(75%) contrast(105%);"></div>
      <div class="nbox" @click="movechat"><img class="neviImg" src="../../assets/chat.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="movegroup"><img class="neviImg" src="../../assets/wechat.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="movedrive"><img class="neviImg" src="../../assets/storage.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="moveoption"><img class="neviImg" src="../../assets/option.svg" width="30px" height="30px"></div>
    </div>
  </div>
</template>

<script>
import apiClient from '@/axios/axios';
import FriendModals from './FriendModals_F.vue';
import { useAuthStore } from '@/store/auth';
import { useNotificationStore } from '@/store/NotificationStore';

export default {
  components: {
    FriendModals
  },
  data() {
    return {
      add_modal: false,
      friend_alarm_modal: false,
      friendview_modal: false,
      searchQuery: '',
      searchResults: [],
      add_alarm: [],
      friend_list: [],
      selectedFriend: {
        id: '',
        name: '',
        picture: '',
        comment: ''
      }
    };
  },
  computed: {
    userInfo() {
      const authStore = useAuthStore();
      return authStore.userInfo;
    },
    friendRequestCount() {
      const notificationStore = useNotificationStore();

      return notificationStore.unreadNotifications.filter(n => n.notificationType === 'FRIEND_REQUEST').length;
    },
  },
  emits: ['changeComponent'],
  methods: {
    async fetchFriends() {
      const userId = this.userInfo.id;
      try {
        const response = await apiClient.get(`${process.env.VUE_APP_API_URL}/api/friendship/friends/${userId}`);
        this.friend_list = response.data.map((friend) => ({
          id: friend.id || '',
          name: friend.name || 'Unknown',
          picture: friend.picture || '../../assets/profile.svg',
          comment: friend.comment || '',
        }));
      } catch (error) {
        console.error('친구 목록을 가져오는 중 오류 발생:', error);
      }
    },
    async searchFriends() {
      const userId = this.userInfo.id;
      if (!userId) {
        alert('로그인해야 친구를 검색할 수 있습니다.');
        return;
      }

      const response = await apiClient.get(`${process.env.VUE_APP_API_URL}/api/friendship/search`, {
        params: { userId, name: this.searchQuery }
      });
      this.searchResults = response.data;
    },
    async sendFriendRequest(receiverId) {
      const userId = this.userInfo.id;
      if (!userId) {
        alert('로그인해야 친구 요청을 보낼 수 있습니다.');
        return;
      }
      try {
        const response = await apiClient.post(`${process.env.VUE_APP_API_URL}/api/friendship/request`, null, {
          params: { senderId: userId, receiverId }
        });
        if (response.status === 200) {
          alert('친구 요청이 전송되었습니다!');
          this.searchQuery = ''; // 입력창 초기화
        } else {
          alert('이 사용자에게 친구 요청을 보낼 수 없습니다.');
        }
      } catch (error) {
        console.error('친구 요청을 보내는 중 오류 발생:', error);
        alert('친구 요청을 보내는 중 오류가 발생했습니다.');
      }
    },
    async friendsRequstList() {
      const userId = this.userInfo.id;
      try {
        const response = await apiClient.get(`${process.env.VUE_APP_API_URL}/api/friendship/requests/${userId}`);

        this.add_alarm = response.data.map((request) => {
          return {
            profile: request.picture || '../../assets/profile.svg',
            name: request.friendName || 'Unknown',
            accept: request.friendShipId,
            reject: request.friendShipId,
          };
        });
      } catch (error) {
        console.error('친구 요청 목록을 가져오는 중 오류 발생:', error);
      }
    },
    async approveFriendship(friendshipId) {
      try {
        const response = await apiClient.post(`${process.env.VUE_APP_API_URL}/api/friendship/respond`, null, {
          params: { requestId: friendshipId, accept: true }
        });
        if (response.status === 200) {
          this.removeAcceptedFriendship(friendshipId);
          this.fetchFriends();
        }
      } catch (error) {
        console.error('친구 요청 수락 중 오류 발생:', error);
      }
    },
    async rejectFriendship(friendshipId) {
      try {
        const response = await apiClient.post(`${process.env.VUE_APP_API_URL}/api/friendship/respond`, null, {
          params: { requestId: friendshipId, accept: false }
        });
        if (response.status === 200) {
          this.removeAcceptedFriendship(friendshipId);
          this.fetchFriends();
        }
      } catch (error) {
        console.error('친구 요청 거절 중 오류 발생:', error);
      }
    },
    removeAcceptedFriendship(friendshipId) {
      this.add_alarm = this.add_alarm.filter((item) => item.accept !== friendshipId);
    },
    closeAddModal() {
      this.add_modal = false;
    },
    closeFriendAlarmModal() {
      this.friend_alarm_modal = false;
      this.fetchFriends();
    },
    openFriendAlarmModal() {
      this.friend_alarm_modal = true;
      this.friendsRequstList();
      const notificationStore = useNotificationStore();

      notificationStore.unreadNotifications.forEach(n => {
        if (n.notificationType === 'FRIEND_REQUEST') {
          notificationStore.removeNotification(n.id);
        }
      });
    },
    showFriendDetails(friend) {
      this.selectedFriend = friend;
      this.friendview_modal = true;
    },
    async movePrivateChatting(friendId) {
      const userId = this.userInfo.id;
      try {
        const response = await apiClient.post(`${process.env.VUE_APP_API_URL}/api/chat/private`, null, {
          params: { userId1: userId, userId2: friendId }
        });
        const chatRoomId = response.data.roomId;
        this.$emit('changeComponent', 'ChattingPage', { chatRoomId: chatRoomId, previousPage: 'ChatList' });
      } catch (error) {
        console.error('Error creating or fetching private chat room:', error);
      }
    },
    movefriend() {
      this.$emit('changeComponent', 'FriendList');
    },
    movechat() {
      this.$emit('changeComponent', 'ChatList');
    },
    movegroup() {
      this.$emit('changeComponent', 'MyGroupPage');
    },
    moveoption() {
      this.$emit('changeComponent', 'OptionPage');
    },
    movedrive() {
      this.$emit('changeComponent', 'DrivePage');
    },
  },
  created() {
    this.fetchFriends();
  },
};
</script>

<style>
.badge {
  position: absolute;
  top: -5px;
  right: -5px;
  padding: 5px 10px;
  border-radius: 50%;
  background-color: red;
  color: white;
  font-size: 12px;
}

.neviImg:hover {
  filter: invert(76%) sepia(23%) saturate(6021%) hue-rotate(312deg) brightness(75%) contrast(105%);
}

.friend_profile {
  width: 50px;
  height: 30px;
  line-height: 30px;
}

.friend_name {
  width: 50px;
  max-width: 200px;
  overflow: hidden;
  font-size: 18px;
  color: #515151;
  line-height: 30px;
}
.friend-listbox {
  position: relative;
  width: 500px;
  height: 780px;
  overflow-y: auto;
  background-color: white;
  border-radius: 25px;
  margin-bottom: 60px;
  z-index: 2;
}

.topbar {
  width: 500px;
  height: 80px;
  padding-top: 25px;
  border-radius: 25px 25px 0 0;
}

.addFriends {
  float: right;
  padding-top: 12px;
  padding-left: 8px;
  width: 50px;
  height: 50px;
}

.acceptFriendsRequest {
  float: right;
  padding-top: 12px;
  padding-left: 20px;
  width: 50px;
  height: 50px;
  position: relative;
}

.userbox {
  width: 500px;
  height: 100px;
  cursor: pointer;
}

.profilebox{
  float: left;
  margin-left: 12px;
  height: 80px;
}



.status_messagebox{
  margin-top: 10px;
  margin-left: 110px;
  width: 250px;
  height: 20px;
}

.divbox1 {
  margin-top: 8px;
  margin-left: 25px;
  width: 450px;
  height: 20px;
  border-top: 1px solid #7d7d7d;
}

.favorite_listbox {
  width: 500px;
  height: 100px;
}

.profile {
  float: left;
  margin-left: 40px;
  margin-top: 17px;
  width: 50px;
  height: 50px;
  cursor: pointer;
}

.divbox2 {
  margin-left: 25px;
  width: 450px;
  height: 20px;
  border-top: 1px solid #7d7d7d;
}

.friend-list {
  width: 500px;
  height: 527px;
  position: relative;
  overflow-y: auto;
}

.friend {
  align-items: center;
  width: 500px;
  height: 80px;
  cursor: pointer;
}

.namebox {
  margin-left: 0;
  padding-top: 18px;
  width: 250px;
  height: 40px;
}

.status_messagebox {
  margin-left: 0;
  margin-top: 10px;
  width: 250px;
  height: 20px;
}
.emailbox {
  font-size: 14px;
  color: #575757;
  margin-left: 10px;
}

.friend button {
  margin-left: auto;
  padding: 5px 10px;
  border: none;
  background-color: #ffbbbb;
  color: #ffffff;
  border-radius: 5px;
  cursor: pointer;
}

.navigationbar {
  width: 500px;
  height: 53px;
  border-radius: 0 0 25px 25px;
  background-color: #f0f0f0;
}

.nbox {
  float: left;
  padding-top: 10px;
  padding-left: 15px;
  margin-left: 34px;
  width: 60px;
  height: 40px;
  cursor: pointer;
}

.movechatting {
  padding-top: 10px;
  padding-left: 200px;
  margin-left: 12px;
  width: 60px;
  height: 40px;
  cursor: pointer;
}
.ul {
  padding: 20px;
}

#chaticon {
  margin-left: 9px;
}

</style>