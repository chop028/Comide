<template>
  <div class="float_wrapper" ref="floatWrapper">
    <button class="fltbtn" ref="fltbtn" @click="toggleChat">
      <img src="@/assets/Logo.svg" style="width: 50px; height: 50px;">
      <span v-if="totalRequestCount > 0" class="fltbadge">{{ totalRequestCount }}</span>
    </button>
    <div v-if="showList" class="float-container" :class="{ active: showList }">
      <!-- 여러 컴포넌트 중 currentComponent에 따라 렌더링 -->
      <component
        v-if="['ChattingPage', 'MyGroupPage', 'ChatList'].includes(currentComponent)"
        :is="currentComponent"
        :chatRoomId="chatRoomId"
        :previousPage="previousPage"
        @changeComponent="changeComponent"
      />
      <component
        v-else-if="currentComponent === 'ProjectManage'"
        :is="currentComponent"
        :gId="gId"
        :gName="gName"
        @changeComponent="changeComponent"
      />
      <component
        v-else
        :is="currentComponent"
        @changeComponent="changeComponent"
      />
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import eventBus from '@/eventBus/eventBus';
import ChatList from '@/components/float/ChatList_F.vue';
import FriendList from '@/components/float/FriendList_F.vue';
import ChattingPage from '@/components/float/ChattingPage_F.vue';
import MyGroupPage from '@/components/float/MyGroupPage_F.vue';
import MakeGroup from '@/components/float/MakeGroup_F.vue';
import group_information from '@/components/float/Group_info_F.vue';
import ProgressModal from '@/components/float/ProgressModal_F.vue';
import GroupApplication from '@/components/float/GroupApplication_F.vue';
import OptionPage from '@/components/float/Option_F.vue';
import DrivePage from '@/components/float/PersonalDrivePage_F.vue';
import ProjectManage from '@/components/float/ProjectManagePage_F.vue';
import { useNotificationStore } from '@/store/NotificationStore';

export default {
  data() { 
    return {
      groupId: null,
      groupName: null,
    };
  },
  components: {
    ChatList,
    FriendList,
    ChattingPage,
    MyGroupPage,
    MakeGroup,
    group_information,
    ProgressModal,
    GroupApplication,
    OptionPage,
    DrivePage,
    ProjectManage,
  },
  computed: {

    friendRequestCount() {
      const notificationStore = useNotificationStore();
      return notificationStore.unreadNotifications.filter(n => n.notificationType === 'FRIEND_REQUEST').length;
    },

    groupRequestCount() {
      const notificationStore = useNotificationStore();
      return notificationStore.unreadNotifications.filter(n => n.notificationType === 'GROUP_REQUEST').length;
    },

    totalRequestCount() {
      return this.friendRequestCount + this.groupRequestCount;
    },
  },
  emits: ['changeComponent'],
  setup() {
    const showList = ref(false); // 플로팅 화면 표시 여부
    const currentComponent = ref('FriendList');
    const chatRoomId = ref(null);
    const gId = ref(null);
    const gName = ref(null);
    const previousPage = ref(null); // 이전 페이지 정보 저장
    const floatWrapper = ref(null);
    const fltbtn = ref(null);

    function toggleChat() {
      showList.value = !showList.value;
    }

    function changeComponent(component, payload, groupId, groupName) {
      if (payload) {
        if (payload.chatRoomId) {
          chatRoomId.value = payload.chatRoomId;
        }
        if (payload.previousPage) {
          previousPage.value = payload.previousPage;
        }
      }
      
      if(groupId) {
        gId.value = groupId;
      }
      if(groupName) {
        gName.value = groupName;
      }
      currentComponent.value = component;
    }

    function moveToChatRoom(chatRoomIdValue) {
      chatRoomId.value = chatRoomIdValue;
      previousPage.value = currentComponent.value; // 이전 페이지 설정
      currentComponent.value = 'ChattingPage';
    }

    function openFloatingPage(pageName) {
      currentComponent.value = pageName; // 전달된 페이지로 컴포넌트 변경
      showList.value = true; // 플로팅 페이지가 열리도록 showList 활성화
    }

    onMounted(() => {
      eventBus.on('moveToChatRoom', (chatRoomIdValue) => {
        moveToChatRoom(chatRoomIdValue);
      });
      eventBus.on('openFloatingPage', openFloatingPage); // Header.vue에서 발생한 이벤트를 수신
    });

    return {
      showList,
      toggleChat,
      currentComponent,
      changeComponent,
      moveToChatRoom,
      floatWrapper,
      fltbtn,
      chatRoomId,
      previousPage,
      gId,
      gName,
    };
  },
  beforeUnmount() {
    // 컴포넌트가 제거될 때 이벤트 리스너를 해제
    eventBus.off('openFloatingPage', this.openFloatingPage);
    eventBus.off('moveToChatRoom', this.moveToChatRoom);
  }
};
</script>

<style scoped>
.fltbadge {
  position: absolute;
  top: -5px;
  right: -5px;
  padding: 5px 10px;
  border-radius: 50%;
  background-color: red;
  color: white;
  font-size: 14px;
  z-index: 10001;
}

.float_wrapper {
  position: fixed;
  bottom: 40px;
  right: 40px;
  z-index: 9999;
}

.fltbtn {
  width: 80px;
  height: 80px;
  border: none;
  border-radius: 35px;
  background-color: #FF5353;
  position: relative;
  z-index: 10000;
  color: white;
  font-size: 20px;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.4);
  cursor: pointer;
  transition: transform 0.3s ease-in-out;
}

.fltbtn:hover {
  transform: scale(1.1); /* 버튼에 호버 시 살짝 확대 */
}

@keyframes slideInUp {
  0% {
    opacity: 0;
    transform: translateY(50px) scale(0.95);
  }
  100% {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes slideOutDown {
  0% {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
  100% {
    opacity: 0;
    transform: translateY(50px) scale(0.95);
  }
}

.float-container {
  position: absolute;
  width: 500px;
  height: 780px;
  border-radius: 25px;
  bottom: 110px;
  right: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  opacity: 0;
  background-color: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  animation: slideOutDown 0.5s forwards
}

.float-container.active {
  animation: slideInUp 0.5s forwards;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
}
</style>