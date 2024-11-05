<template>
  <ChoiceCodeModal v-if="isModalVisible" @close="closeChoiceModal" @selectAndClose="changeToUploadModal"/>
  <component :is="currentComponent" v-if="isCodeVisible" :selectedProjType="selectedProjType" @close="isCodeVisible = false" @closeWithSelection="handleFileSelected"></component>
  <div class="chatting-room">
    <div class="chatting_topbar">
      <button class="backbtn" @click="goBack">
        <img src="../../assets/arrow-left-short.svg" width="45px" height="45px" />
      </button>
      <span style="font-size: 33px; font-weight: bold; color: #515151; letter-spacing: 1px; float: left;">채팅방</span>
    </div>
    <div class="chatting-box" ref="chattingBox">
      <div v-for="(message, index) in messages" :key="index" :class="String(message.chatSender) === String(this.userId) ? 'chat2' : 'chat1'">
        <div v-if="String(message.chatSender) !== String(this.userId)" class="chat1_profile">
          <div class="person1">
            <img :src="message.senderPicture || '../../assets/profile.svg'" width="55px" height="55px" style="border-radius: 50px;"/>
          </div>
          <div class="person1_nick">
            {{ message.senderName }}
          </div>
        </div>
        <div v-if="message.codeId === null" :class="String(message.chatSender) === String(this.userId) ? 'chat2_box' : 'chat1_box'">
          {{ message.message }}
          <div :class="String(message.chatSender) === String(this.userId) ? 'chat2_timebox' : 'chat1_timebox'">{{ message.sendTime }}</div>
        </div>
        <div v-else-if="message.codeId != null" :class="String(message.chatSender) === String(this.userId) ? 'code2_box' : 'code1_box'">
          {{ message.title }}
          <img class="codeblock-btn" src="@/assets/code_blocks.svg" @click="linkToCodeEditor(message.codeId)">
          <div :class="String(message.chatSender) === String(this.userId) ? 'code2_timebox' : 'code1_timebox'">{{ message.sendTime }}</div>
        </div>
      </div>
    </div>
    <div class="texting-box">
      <div class="upload_code" @click="openCodeUpload">
        <img src="../../assets/code-square.svg" width="20px" height="20px" />
      </div>
      <div class="upload_file"><img src="../../assets/add-square.svg" width="20px" height="20px" /></div>
      <div class="input-text">
        <input type="text" v-model="newMessage" placeholder="채팅을 입력하세요." autocomplete="off" class="form-control" @keydown.enter="onEnterKey" @compositionstart="onCompositionStart" @compositionend="onCompositionEnd"/>
        <button type="submit" @click="sendMessage" class="primary" id="submit_btn">Send</button>
      </div>
    </div>
  </div>
  <DriveModal v-if="driveModalVisible" @close="driveModalVisible = false" :postData="postData"></DriveModal>
</template>

<script>
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import apiClient from '@/axios/axios'; 
import CodeUpload from '@/components/float/CodeUploadComponent_F.vue';
import DriveModal from '@/views/DriveModal_T.vue';
import { useAuthStore } from '@/store/auth';
import emitter from '@/eventBus/eventBus.js';
import ChoiceCodeModal from '@/components/float/ChoiceCodeUpload_F.vue';

export default {
  components: {
    CodeUpload,
    DriveModal,
    ChoiceCodeModal,
  },
  props: {
    chatRoomId: {
      type: String,
      required: true
    },
    previousPage: {
      type: String,
      required: false,
      default: 'FriendList'
    }
  },
  emits: ['changeComponent'],
  data() {
    return {
      newMessage: "",
      messages: [],
      stompClient: null,
      userId: '',
      userName: '',
      isComposing: false,
      localChatRoomId: this.chatRoomId,
      isCodeVisible: false,
      currentComponent: null,
      driveModalVisible: false,
      postData: null,
      isModalVisible: false,
      isPrivateChatRoom: true,
      selectedProjType: true,
    };
  },
  mounted() {
    this.connectToWebSocket();
    this.loadChatHistory();
    this.checkIsPrivate()
  },
  created() {
    const authStore = useAuthStore();
    if (authStore.userInfo) {
      this.userId = authStore.userInfo.id;
      this.userName = authStore.userInfo.name;
    } else {
      this.loadUserInfo();
    }
  },
  methods: {
    closeChoiceModal() {
      this.isModalVisible = false;
    },
    movechat() {
      this.$emit("changeComponent", "ChatList");
    },
    moveGroupPage() {
      this.$emit("changeComponent", "MyGroupPage");
    },
    goBack() {
      this.$emit("changeComponent", this.previousPage);
      this.localChatRoomId = null;
    },
    async loadUserInfo() {
      const authStore = useAuthStore();
      await authStore.fetchUserInfo();
      this.userId = authStore.userInfo.id;
      this.userName = authStore.userInfo.name;
    },
    checkIsPrivate() {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/chat/isPrivate?roomId=${this.chatRoomId}`)
        .then(response => {
          if (response) {
            this.isPrivateChatRoom = response.data;
          }
        })
        .catch(error => {
          console.error('게시물 데이터를 가져오는 중 오류 발생:', error);
        });
    },
    sendMessage() {
      if (this.newMessage.trim() !== "" && this.stompClient && this.stompClient.connected) {
        const message = {
          roomId: this.localChatRoomId,
          chatSender: this.userId,
          senderName: this.userName,
          message: this.newMessage,
          sendTime: new Date().toLocaleTimeString('en-GB', { hour: '2-digit', minute: '2-digit' }),
          type: 'TALK',
          codeId: null
        };

        this.stompClient.publish({
          destination: "/pub/chat/sendMessage",
          body: JSON.stringify(message)
        });
        this.newMessage = "";
      }
    },
    connectToWebSocket() {
      const socket = new SockJS(`${process.env.VUE_APP_WS_URL}`);
      this.stompClient = new Client({
        webSocketFactory: () => socket,
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
      });

      this.stompClient.onConnect = () => {
        this.stompClient.subscribe('/sub/chat/room/' + this.localChatRoomId, message => {
          const chatMessage = JSON.parse(message.body);

          if (chatMessage.codeId !== null) {
            this.fetchCodeData(chatMessage).then(() => {
              this.messages.push(chatMessage);
              this.$nextTick(() => {
                const chattingBox = this.$refs.chattingBox;
                if (chattingBox) {
                  chattingBox.scrollTop = chattingBox.scrollHeight;
                }
              });
            });
          } else {
            this.messages.push(chatMessage);
            this.$nextTick(() => {
              const chattingBox = this.$refs.chattingBox;
              if (chattingBox) {
                chattingBox.scrollTop = chattingBox.scrollHeight;
              }
            });
          }
        });
      };

      this.stompClient.activate();
    },
    fetchCodeData(item) {
      return apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/code-editor/get-code-detail.do?codeId=${item.codeId}`)
        .then(response => {
          const title = response.data.result.name;
          item.title = title;
          this.$forceUpdate();
        })
        .catch(error => {
          console.error('코드 데이터를 가져오는 중 오류 발생:', error);
        });
    },
    async loadChatHistory() {
      try {
        const response = await apiClient.get(`${process.env.VUE_APP_API_URL}/api/chat/history?roomId=${this.localChatRoomId}`);
        this.messages = response.data;
        await Promise.all(this.messages.map(item => {
          if (item.codeId !== null) {
            return this.fetchCodeData(item);
          }
        }));
        this.$nextTick(() => {
          const chattingBox = this.$refs.chattingBox;
          if (chattingBox) {
            chattingBox.scrollTop = chattingBox.scrollHeight;
          }
        });
      } catch (error) {
        console.error('Error loading chat history:', error);
      }
    },
    onCompositionStart() {
      this.isComposing = true;
    },
    onCompositionEnd() {
      this.isComposing = false;
    },
    onEnterKey() {
      if (!this.isComposing) {
        this.sendMessage();
      }
    },
    openCodeUpload() {
      if(this.isPrivateChatRoom) {
        this.changeToUploadModal(true);
      } else {
        this.isModalVisible = true;
      }
    },
    changeToUploadModal(isPrivateProj) {
      this.selectedProjType = isPrivateProj;
      this.isCodeVisible = true;
      this.currentComponent = 'CodeUpload';
    },
    handleFileSelected(codeId) {
      const message = {
        roomId: this.localChatRoomId,
        chatSender: this.userId,
        senderName: this.userName,
        message: null,
        sendTime: new Date().toLocaleTimeString('en-GB', { hour: '2-digit', minute: '2-digit' }),
        type: 'FILE',
        codeId: codeId,
      };
      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.publish({
          destination: "/pub/chat/sendMessage",
          body: JSON.stringify(message)
        });
      } else {
        console.error('Stomp client is not connected.');
      }
      this.isCodeVisible = false;
    },
    linkToCodeEditor(codeId) {
      emitter.emit('open-code');
      const routeData = this.$router.resolve({
        name: 'codeedit',
        query: { 
          codeId: codeId,
          openCode: true,
         }
      });
      window.open(routeData.href, '_blank');
    }
  },
};
</script>

<style scoped>
.chatting-room {
  width: 500px;
  height: 780px;
  background-color: rgb(255, 225, 225);
  border-radius: 25px;
  display: flex;
  flex-direction: column;
}

.chatting_topbar {
  width: 500px;
  height: 80px;
  padding-top: 25px;
  border-radius: 25px 25px 0 0;
}

.room-name {
  float: left;
  width: 400px;
  height: 50px;
}

.backbtn {
  float: left;
  margin-left: 20px;
  margin-top: 3px;
  margin-right: 15px;
  background-color: transparent;
  border: none;
}

.chatting-box {
  width: 500px;
  flex: 1;
  background: rgb(255, 225, 225);
  background: linear-gradient(180deg, rgba(255, 225, 225, 1) 37%, rgba(255, 255, 255, 1) 100%);
  overflow-y: auto;
  padding-top: 20px; /* 채팅이 위에서 시작하도록 패딩 추가 */
}

.chat1, .chat2 {
  display: flex;
  align-items: flex-start;
  text-align: left;
  margin: 10px 20px; /* 모든 채팅 간격 통일 */
}

.chat1_profile {
  width: 55px;
  height: 55px;
  margin-right: 10px; /* 프로필과 채팅 메시지 간격 */
}
.person1_nick {
  display: inlinebox;
  width: 200px;
  max-width: 300px;
  font-size: 16px;
  font-weight: bold;
  margin-left: 65px;
  margin-top: -60px;
}
.chat1_box {
  display: inline-block;
  position: relative;
  max-width: 300px;
  border-radius: 8px;
  font-size: 13px;
  color: #515151;
  border: 1px solid #d4d4d4;
  padding: 7px 12px;
  background-color: #ffffff;
  margin-top: 25px;
}

.chat2_box {
  display: inline-block;
  position: relative;
  max-width: 300px;
  border-radius: 8px;
  font-size: 13px;
  color: #515151;
  border: 1px solid #d4d4d4;
  padding: 7px 12px;
  background-color: #ffffff;
  margin-left: auto; /* 내 메시지 오른쪽 정렬 */
  margin-right: 15px; /* 내 메시지 오른쪽 간격 */
}
.code1_box{
  display: inline-block;
  position: relative;
  max-width: 300px;
  border-radius: 8px;
  font-size: 20px;
  font-weight: bold;
  color: #515151;
  border: 1px solid #d4d4d4;
  padding: 15px;
  background-color: #ffffff;
  margin-top: 25px;
}
.code2_box {
  display: inline-block;
  position: relative;
  max-width: 300px;
  border-radius: 8px;
  font-size: 20px;
  font-weight: bold;
  color: #515151;
  border: 1px solid #d4d4d4;
  padding: 15px;
  background-color: #ffffff;
  margin-left: auto; /* 내 코드 블록 오른쪽 정렬 */
  margin-right: 15px; /* 내 코드 블록 오른쪽 간격 */
}

.chat1_timebox, .code1_timebox {
  display: inline-block;
  position: absolute;
  margin-left: -32px;
  bottom: -1px; /* 시간 위치 조정 */
  color: #a6a6a6;
  font-size: 10px;
  right: -30px; /* 상대방 메시지/코드 시간 오른쪽 정렬 */
}

.chat2_timebox, .code2_timebox {
  display: inline-block;
  position: absolute;
  margin-left: -32px;
  bottom: -1px; /* 시간 위치 조정 */
  color: #a6a6a6;
  font-size: 10px;
  left: 0; /* 내 메시지/코드 시간 왼쪽 정렬 */
}

.texting-box {
  width: 500px;
  height: 53px;
  border-radius: 0 0 25px 25px;
  background-color: #f0f0f0;
}

.upload_code {
  float: left;
  margin-left: 28px;
  margin-top: 15px;
  width: 50px;
  height: 50px;
  cursor: pointer;
}

.upload_file {
  float: left;
  margin-top: 15px;
  width: 50px;
  height: 50px;
  cursor: pointer;
}

.input-text {
  float: left;
  display: flex;
  align-content: space-between;
  width: 350px;
  height: 40px;
  margin-top: 7px;
  margin-right: 5px;
}

#message {
  width: 350px;
  height: 37px;
  margin-top: 5px;
  border-radius: 10px;
  border: 0.6px solid #bababa;
}

#submit_btn {
  width: 50px;
  height: 40px;
  margin-left: 5px;
  border-radius: 8px;
  border: 0.6px solid #bababa;
}
.codeblock-btn{
  float: right;
  display: block;
  width: 25px;
  height: 25px;
  margin-left: 10px;
  margin-top: 3px;
}
.codeblock-btn:hover{
    filter: invert(76%) sepia(23%) saturate(6021%) hue-rotate(312deg) brightness(75%) contrast(105%);
    cursor: pointer;
}
</style>