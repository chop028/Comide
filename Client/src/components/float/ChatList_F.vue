<template>
  <div class="chat-listbox">
    <div class="chatList_topbar">
      <span style="font-size: 33px; font-weight: bold; color: #515151; padding-left: 30px;">채팅방</span>
      <div class="img_box1"><img src="../../assets/add-square.svg" width="30px" height="30px" style="cursor: pointer;"></div>
      <div class="img_box2"><img src="../../assets/search.png" width="31px" height="31px" style="cursor: pointer;"></div>
    </div>
    <div class="chat-list">
      <div v-for="(chat, index) in chatList" :key="index" class="friends" @click="movechatting(chat.roomId)">
        <div class="profilebox"><img :src="chat.userPicture || '../../assets/profile.svg'" width="55px" height="55px" style="border-radius: 50px;"></div>
        <div class="namebox"><span style="font-size: 22px; font-weight: bold; color: #575757; letter-spacing: 1px;">{{ chat.userName }}</span></div>
        <div class="status_messagebox"><span style="color: #575757; font-size: 13px;">{{ chat.lastChat }}</span></div>
      </div>
    </div>
    <div class="chat-navigationbar">
      <div class="nbox" @click="movefriend"><img class="neviImg" src="../../assets/people-fill.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="movechat"><img class="neviImg" src="../../assets/chat.svg" width="30px" height="30px" style="filter: invert(76%) sepia(23%) saturate(6021%) hue-rotate(312deg) brightness(75%) contrast(105%);"></div>
      <div class="nbox" @click="movegroup"><img class="neviImg" src="../../assets/wechat.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="movedrive"><img class="neviImg" src="../../assets/storage.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="moveoption"><img class="neviImg" src="../../assets/option.svg" width="30px" height="30px"></div>
    </div>
  </div>
</template>

<script>
import apiClient from '@/axios/axios'; 
import { useAuthStore } from '@/store/auth';

export default {
  data() {
    const authStore = useAuthStore();
    return {
      userId: authStore.userInfo?.id || '',
      chatList: []
    };
  },
  methods: {
    movefriend() {
        this.$emit('changeComponent', 'FriendList');
    },
    movechatting(chatRoomId) {
        this.$emit('changeComponent', 'ChattingPage', { chatRoomId: chatRoomId, previousPage: 'ChatList' });
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
    async fetchPrivateChatRooms() {
      const userId = this.userId;
      try {
        const response = await apiClient.get(`${process.env.VUE_APP_API_URL}/api/chat/private-rooms/${userId}`);
        this.chatList = response.data;
      } catch (error) {
        console.error('Error fetching private chat rooms:', error);
      }
    }
  },
  created() {
    this.fetchPrivateChatRooms();
  }
};
</script>

  <style>
  .neviImg:hover{
    filter: invert(76%) sepia(23%) saturate(6021%) hue-rotate(312deg) brightness(75%) contrast(105%);
  }
  .chat-listbox {
    position: relative; /* 부모 요소를 상대 위치로 설정 */
    width: 500px;
    height: 780px;
    overflow-y: auto; 
    background-color: white;
    border-radius: 25px;
    margin-bottom: 60px;
}
.chatList_topbar{
    width: 500px;
    height: 80px;
    padding-top: 25px;
    border-radius: 25px 25px 0 0;
  }
  .img_box1{
    float: right;
    margin-right: 30px;
    padding-top: 12px;
    padding-left: 8px;
    width: 50px;
    height: 50px;
  }
  .img_box2{
    float: right;
    margin-right: 30px;
    padding-top: 12px;
    padding-left: 20px;
    width: 50px;
    height: 50px;
  }
  .chat-list{
    width: 500px;
    height: 637px;
    overflow-y: auto; 
    position: relative;
    overflow: hidden;
    margin-bottom: 10px;
  }
  .profilebox{
    float: left;
    margin-left: 22px;
    padding-top: 20px;
    padding-left: 25px;
    width: 95px;
    height: 80px;
  }
  .namebox{
    display: flex;
    padding-top: 18px;
    margin-left: 120px;
  }
  .status_messagebox{
    display: flex;
    margin-top: 10px;
    margin-left: 120px;
  }
  .friends{
    margin-top: 5px;
    width: 500px;
    height: 80px;
    cursor: pointer;
  }
  .friends:hover{
    background-color: #E9E9EB;
  }
  .chat-navigationbar {
    width: 500px;
    height: 53px;
    border-radius: 0 0 25px 25px;
    background-color: #F0F0F0;
}
  </style>