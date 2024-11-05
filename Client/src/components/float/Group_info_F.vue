<template>
  <component :is="currentComponent" v-if="isModalVisible" @close="isModalVisible = false" @closeAndReload="closeAndReload" :groupId="groupData.id" :groupName="groupData.name"/>
  <div class="group_information">
    <div class="group_name">
      <span class="g-name">{{ groupData.name }}</span>
      <img v-if="role==='LEADER'" src="../../assets/option.svg" class="option-icon" @click="changeOption">
      <div class="apply-group-container" style="position: relative;">
        <img v-if="role==='LEADER'" class="apply-group" src="../../assets/thisperson.svg" @click="showGroupModal">
        <span v-if="groupRequestCount > 0" class="badge">{{ groupRequestCount }}</span>
      </div>
    </div>
    <div class="progress">
      <div class="block1">
        <span class="all-progress">전체 진행도</span>
      </div>
      <div class="block2">
        <progress-bar :progress="calculateProgress" :max="maxValue" @click="showProgressModal" style="cursor: pointer;"></progress-bar>
      </div>
    </div>
    <div class="week-goal">
      <span class="goal">현재 진행 단계</span>
      <div class="goal-list">
        <ul>
          <li v-if="weeklyGoals">{{ weeklyGoals }}</li>
        </ul>
      </div>
    </div>
    <div class="button-box">
      <button class="chattingbtn" @click="moveToChatRoom">채팅방으로 이동</button>
      <button class="manageprjbtn" @click="movemanage">그룹 프로젝트</button>
    </div>
  </div>
</template>

<script>
import apiClient from '@/axios/axios';
import eventBus from '@/eventBus/eventBus';
import GroupModal from '@/components/float/GroupApplicationModal_F.vue';
import ProgressModal from '@/components/float/ProgressModal_F.vue';
import ProgressBar from '@/components/float/ProgressBar_F.vue';
import ChangeOption from '@/components/float/ChangeGroupOption_F.vue';
import { useNotificationStore } from '@/store/NotificationStore';

export default {
  emits: ['changeComponent', 'moveToChatRoom', 'refresh'],
  components: {
    ProgressBar,
    ProgressModal,
    GroupModal,
    ChangeOption,
  },
  props: {
    role: {
      type: String,
      required: true,
    },
    groupData: {
      type: Object,
      required: true,
    }
  },
  data() {
    return {
      weeklyGoals: "",
      overall: 0,
      crtProgress: 0,
      maxValue: 100,
      currentComponent: 'GroupModal',
      isModalVisible: false,
      stompClient: null
    };
  },
  methods: {
    async getMyGroupList(groupId) {
      try {
        const response = await apiClient.get(`${process.env.VUE_APP_API_URL}/api/group/get-group-task-list.do?groupId=${groupId}`);
        if (response.data.success) {
          this.overall = 0;
          this.crtProgress = 0;
          response.data.result.forEach(obj => {
            this.overall += obj.overall;
            this.crtProgress += obj.crtProgress;
          });
          for (const obj of response.data.result) {
            if (obj.crtProgress / obj.overall !== 1) {
              this.weeklyGoals = obj.taskName;
              break;
            }
          }
        }
      } catch (error) {
        console.error(error);
      }
    },
    handleChangeComponent(componentName) {
      this.currentComponent = componentName;
    },
    showGroupModal() {
      this.isModalVisible = true;
      this.currentComponent = 'GroupModal';

      const notificationStore = useNotificationStore();

      notificationStore.unreadNotifications.forEach(n => {
        if (n.notificationType === 'GROUP_REQUEST') {
          notificationStore.removeNotification(n.id);
        }
      });
    },
    showProgressModal() {
      this.isModalVisible = true;
      this.currentComponent = 'ProgressModal';
    },
    changeOption() {
      this.isModalVisible = true;
      this.currentComponent = 'ChangeOption';
    },
    moveToChatRoom() {
      eventBus.emit('moveToChatRoom', this.groupData.chatRoomId);
    },
    closeAndReload() {
      this.isModalVisible = false;
      this.getMyGroupList(this.groupData.id);
    },
    movemanage() {
      this.$emit('changeComponent', 'ProjectManage', null, this.groupData.id, this.groupData.name);
    },
  },
  created() {
    this.getMyGroupList(this.groupData.id);
  },
  computed: {
    calculateProgress() {
      if (this.overall === 0) return 0;
      return (this.crtProgress / this.overall) * 100;
    },
    groupRequestCount() {
      const notificationStore = useNotificationStore();

      return notificationStore.unreadNotifications.filter(
        (n) => n.notificationType === 'GROUP_REQUEST' && n.groupId === this.groupData.id
      ).length;
    },
  }
}
</script>

<style scoped>
.badge {
  position: absolute;
  top: -8px;
  right: -8px;
  padding: 3px 6px;
  border-radius: 50%;
  background-color: red;
  color: white;
  font-size: 11px;
}
.group_information {
  margin-top: 25px;
  margin-left: 52px;
  width: 400px;
  border: 2px solid #D4D4D4;
  border-radius: 20px;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.3);
  z-index: 5;
}
.option-icon {
  width: 25px;
  height: 25px;
  float: right;
  margin-right: 30px;
  margin-top: 12px;
  cursor: pointer;
}
.apply-group {
  width: 30px;
  height: 30px;
  position: relative;
  filter: invert(29%) sepia(10%) saturate(11%) hue-rotate(317deg) brightness(97%) contrast(88%);
}
.apply-group-wrapper {
  position: relative;
  display: inline-block;
}
.apply-group-container {
  position: relative;
  float: right;
  margin-right: 10px;
  margin-top: 10px;
  width: 30px;
  height: 30px;
  cursor: pointer;
}
.notification-badge {
  position: absolute;
  top: 0;
  right: 0;
  width: 15px;
  height: 15px;
  background-color: red;
  border-radius: 50%;
  color: white;
  font-size: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.group_name {
  padding-left: 30px;
  padding-top: 20px;
  margin: auto;
  width: 395px;
  height: 70px;
  border-radius: 20px 20px 0 0;
}
.g-name {
  color: #5E5E5E;
  font-weight: bold;
  font-size: 30px;
  letter-spacing: 1px;
}
.progress {
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin: auto;
  padding-left: 30px;
  width: 395px;
  height: 130px;
  background-color: white;
}
.all-progress {
  font-size: 20px;
  font-weight: bold;
  letter-spacing: 1px;
  color: #5E5E5E;
}
.block1 {
  margin-top: 10px;
  width: 300px;
  height: 30px;
}
.block2 {
  margin-top: 5px;
  max-width: 350px;
  height: 50px;
}
.week-goal {
  padding-top: 5px;
  padding-left: 30px;
  width: 395px;
}
.goal {
  font-size: 20px;
  font-weight: bold;
  color: #5E5E5E;
  letter-spacing: 1px;
}
.goal-list {
  padding: 0 0 0 25px;
  margin-top: 5px;
  font-weight: bold;
  color: #5E5E5E;
  width: 350px;
}
.week-goal ul li {
  list-style-type: circle;
}
.button-box {
  padding-top: 10px;
  width: 395px;
  height: 90px;
}
.chattingbtn {
  margin-left: 20px;
  width: 170px;
  height: 55px;
  color: white;
  font-size: 20px;
  font-weight: bold;
  letter-spacing: 1px;
  background-color: #FF9898;
  border-radius: 8px;
  border: none;
}
.manageprjbtn {
  margin-left: 10px;
  width: 170px;
  height: 55px;
  color: white;
  font-size: 20px;
  font-weight: bold;
  letter-spacing: 1px;
  background-color: #969696;
  border-radius: 8px;
  border: none;
}
.chattingbtn:hover {
  background-color: #f98585;
}
.manageprjbtn:hover {
  background-color: #848484;
}
</style>