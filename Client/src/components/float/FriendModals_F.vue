<template>
  <!-- 친구 검색 모달 -->
  <div v-if="localAddModal" class="modal-overlay">
    <div class="friend_add_modal">
      <div class="friend_modal_head">
        <div class="friend_modal_text">친구추가</div>
        <div class="friend_modal_close" @click="closeAddModal">x</div>
      </div>
      <div class="search_wrap">
        <input class="search_friend_bar" v-model="localSearchQuery" type="text" placeholder="이름을 입력하세요.">
        <div class="search_btn" @click="searchFriends()">검색</div>
      </div>
      <ul class="friend_list_modal">
        <li v-for="user in searchResults" :key="user.id" class="searched-friend">
          <div class="modal-profilebox">
            <img :src="user.picture || require('../../assets/profile.svg')" width="50px" height="50px" style="border-radius: 25px;">
          </div>
          <div class="add-friend-infobox">
            <div class="addfriend-namebox">
              <span style="font-size: 18px; font-weight: bold; color: #575757; letter-spacing: 1px;">{{ user.name }}</span>
            </div>
            <div class="addfriend-emailbox">
              <span style="font-size: 14px; color: #575757;">{{ user.email }}</span>
            </div>
          </div>
          <button class="add-friend-btn" @click="sendFriendRequest(user.id)">+</button>
        </li>
      </ul>
    </div>
  </div>

  <!-- 친구 신청 알림 모달 -->
  <div v-if="friend_alarm_modal" class="modal-overlay">
    <div class="friend_alarm_modal">
      <div class="friend_modal_head">
        <div class="friend_modal_text">친구신청</div>
        <div class="friend_modal_close" @click="closeFriendAlarmModal">x</div>
      </div>
      <div class="friend_alarm_list">
        <ul class="alarm_list">
          <li v-for="(add_alarm, index) in add_alarm" :key="index" class="friend_alarm_item">
            <div class="friend_alarm_content">
              <div class="friend_profile"><img :src="add_alarm.profile || require('../../assets/profile.svg')" width="35px" height="35px"></div>
              <div class="friend_name">{{ add_alarm.name }}</div>
              <div class="accept_btn" @click="approveFriendship(add_alarm.accept)">수락</div>
              <div class="reject_btn" @click="rejectFriendship(add_alarm.reject)">거절</div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>

  <!-- 친구 보기 모달 -->
  <div v-if="localFriendViewModal" class="modal-overlay">
    <div class="friendview_modal">
      <div class="friendview_modal_close" @click="closeFriendViewModal">x</div>
      <div class="friendview_profilebox">
        <img :src="selectedFriend.picture || require('../../assets/profile.svg')" width="55px" height="55px" style="border-radius: 50px;">
      </div>
      <div class="friendview_namebox">
        <span style="font-size: 23px; font-weight: bold; color: #575757; letter-spacing: 1px;">{{ selectedFriend.name }}</span>
      </div>
      <div class="friendview_status_messagebox">
        <span style="color: #575757; font-size: 13px;">{{ selectedFriend.comment }}</span>
      </div>
      <div class="movechatting" @click="movePrivateChatting(selectedFriend.id)">
        <img src="../../assets/chat.svg" width="50px" height="50px" id="chaticon">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    add_modal: Boolean,
    friend_alarm_modal: Boolean,
    friendview_modal: Boolean,
    searchQuery: String,
    searchResults: Array,
    add_alarm: Array,
    selectedFriend: Object
  },
  data() {
    return {
      localAddModal: this.add_modal,
      localFriendViewModal: this.friendview_modal,
      localSearchQuery: this.searchQuery,
    };
  },
  watch: {
    add_modal(val) {
      this.localAddModal = val;
    },
    friendview_modal(val) {
      this.localFriendViewModal = val;
    },
    searchQuery(val) {
      this.localSearchQuery = val;
    }
  },
  methods: {
    searchFriends() {
      this.$emit('search-friends', this.localSearchQuery);
    },
    sendFriendRequest(userId) {
      this.$emit('send-friend-request', userId);
    },
    closeFriendAlarmModal() {
      this.$emit('close-friend-alarm-modal');
    },
    approveFriendship(accept) {
      this.$emit('approve-friendship', accept);
    },
    rejectFriendship(reject) {
      this.$emit('reject-friendship', reject);
    },
    closeAddModal() {
      this.$emit('close-add-modal');
      this.localAddModal = false;
    },
    closeFriendViewModal() {
      this.localFriendViewModal = false;
      this.$emit('update:friendview_modal', false);
    },
    movePrivateChatting(friendId) {
      this.$emit('move-private-chatting', friendId);
    },
    
  }
};
</script>

<style scoped>
.modal-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 25px;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  z-index: 9999;
}
.black_bg {
  position: fixed;
  width: 500px;
  height: 780px;
  border-radius: 25px;
  margin-bottom: 60px;
  background: rgba(0, 0, 0, 0.5);
  z-index: 3;
}

.friend_add_modal {
  width: 360px;
  height: 600px;
  border-radius: 20px;
  border: 1px solid #d4d4d4;
  background: linear-gradient(180deg, rgba(255, 225, 225, 1) 1%, rgba(255, 255, 255, 1) 60%);
  margin-left: 70px;
  margin-top: 90px;
}
.friend_modal_head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 359px;
  height: 80px;
}

.friend_modal_text {
  width: 150px;
  height: 40px;
  font-size: 25px;
  font-weight: bold;
  color: #515151;
  margin-left: 20px;
  margin-top: 10px;
}

.friend_modal_close {
  text-align: center;
  width: 30px;
  height: 30px;
  font-size: 20px;
  font-weight: bold;
  color: #515151;
  cursor: pointer;
  margin-right: 15px;
}
.search_wrap {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 359px;
  height: 50px;
  margin: auto;
}

.search_friend_bar {
  width: 240px;
  height: 30px;
  border-radius: 10px;
  border: 2px solid #999292;
  background-color: #ffffff;
  margin-left: 20px;
}

.search_btn {
  width: 50px;
  height: 30px;
  background-color: #ffbbbb;
  font-size: 20px;
  font-weight: 800;
  color: #ffffff;
  text-align: center;
  line-height: 30px;
  cursor: pointer;
  border-radius: 10px;
  margin-left: 10px;
  margin-right: 20px;
}

.friend_list_modal {
  width: 360px;
  height: 450px;
  overflow-y: auto;
  padding-left: 20px;
  cursor: auto;
}
.searched-friend {
  padding-top: 20px;
  width: 340px;
  height: 90px;
  cursor: pointer;
}
.modal-profilebox {
  padding-top: 5px;
  margin-right: 15px;
  float: left;
  width: 50px;
  height: 50px;
}
.add-friend-infobox {
  float: left;
  width: 240px;
  height: 60px;
}
.addfriend-namebox {
  padding-top: 5px;
}
.add-friend-btn {
  font-weight: bold;
  font-size: 25px;
  margin-top: 10px;
  background-color: transparent;
  border: none;
}
.friend_alarm_modal {
  width: 359px;
  height: 450px;
  border-radius: 20px;
  border: 1px solid #d4d4d4;
  background-color: white;
  margin-left: 70px;
  margin-top: 90px;
}

.friend_alarm_list {
  width: 359px;
  height: 350px;
  margin-top: 10px;
}

.alarm_list {
  max-height: 300px;
  overflow-y: auto;
}

.friend_alarm_content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 320px;
  height: 50px;
}

.friend_alarm_item {
  padding: 20px;
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

.accept_btn {
  width: 50px;
  height: 30px;
  border-radius: 10px;
  border: 0.5px solid #515151;
  background-color: #FF9898;
  color: black;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  margin-left: 100px;
  text-align: center;
  line-height: 30px;
}

.reject_btn {
  width: 50px;
  height: 30px;
  border-radius: 10px;
  border: 1px solid #515151;
  background-color: #969696;
  color: white;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  margin-left: 10px;
  text-align: center;
  line-height: 30px;
}
.accept_btn:hover {
  background-color: #f98585;
}
.reject_btn:hover {
  background-color: #848484;
}
.friendview_modal {
  width: 500px;
  height: 300px;
  background: linear-gradient(180deg, rgba(255, 225, 225, 1) 1%, rgba(255, 255, 255, 1) 60%);
  margin-top: 480px;
}

.friendview_profilebox {
  margin-left: 12px;
  padding-top: 20px;
  padding-left: 200px;
  width: 95px;
  height: 80px;
}

.friendview_namebox {
  margin-left: 120px;
  width: 250px;
  height: 40px;
  text-align: center;
}

.friendview_status_messagebox {
  margin-top: 5px;
  margin-left: 120px;
  width: 250px;
  height: 20px;
  text-align: center;
  overflow: hidden;
}

.friendview_modal_close {
  width: 50px;
  height: 50px;
  font-size: 20px;
  font-weight: bold;
  color: #515151;
  margin-left: 470px;
  margin-top: 10px;
  cursor: pointer;
}
</style>
