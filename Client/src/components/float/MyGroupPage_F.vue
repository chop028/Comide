<template>
  <div class="mygroup_page">
    <div class="header-top">
      그룹
      <img src="../../assets/search.png" style="width: 40px; height: 40px; margin-top: 4px; float: right; margin-right: 40px; cursor: pointer;">
    </div>
    <div class="group-box">
      <div v-for="(group, index) in myGroupList" :key="index">
        <group_information :role="group.role" :groupData="group.groupInfo" @changeComponent="changeComponent" @moveToChatRoom="moveToChatRoom" @refresh="refreshPage"></group_information>
      </div>
      <button class="plus_group" @click="movemakegroup">+</button>
    </div>

    <div class="navigationbar">
      <div class="nbox" @click="movefriend"><img class="neviImg" src="../../assets/people-fill.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="movechat"><img class="neviImg" src="../../assets/chat.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="movegroup"><img class="neviImg" src="../../assets/wechat.svg" width="30px" height="30px"  style="filter: invert(76%) sepia(23%) saturate(6021%) hue-rotate(312deg) brightness(75%) contrast(105%);"></div>
      <div class="nbox" @click="movedrive"><img class="neviImg" src="../../assets/storage.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="moveoption"><img class="neviImg" src="../../assets/option.svg" width="30px" height="30px"></div>
    </div>
  </div>
</template>

<script>
import group_information from '@/components/float/Group_info_F.vue';
import apiClient from '@/axios/axios';
import { useAuthStore } from '@/store/auth';

export default {
  emits: ['changeComponent'],
  components: {
    group_information,
  },
  data() {
    return {
      myGroupList: [],
    };
  },
  methods: {
    getMyGroupList(userId) {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/group/get-group-list.do?userId=${userId}`)
        .then((response) => {
          if (response.data.success) {
            this.myGroupList = response.data.result;
          }
        })
        .catch(function(error) {
          console.error(error);
        });
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
    movechatting() {
      this.$emit('changeComponent', 'ChattingPage');
    },
    moveoption() {
      this.$emit('changeComponent', 'OptionPage');
    },
    movedrive() {
      this.$emit('changeComponent', 'DrivePage');
    },
    movemakegroup() {
      this.$emit('changeComponent', 'MakeGroup');
    },
    moveToChatRoom(chatRoomId) {
      this.$emit('changeComponent', 'ChattingPage', { chatRoomId });
    },
    changeComponent(component, payload, groupId, groupName) {
      this.$emit('changeComponent', component, payload, groupId, groupName);
    },
    refreshPage() {
      window.location.reload();
    }
  },
  mounted() {
    const authStore = useAuthStore();
    const userId = authStore.userInfo?.id;

    if (userId) {
      this.getMyGroupList(userId);
    }
  }
};
</script>

<style scoped>
.neviImg:hover{
    filter: invert(76%) sepia(23%) saturate(6021%) hue-rotate(312deg) brightness(75%) contrast(105%);
  }
.mygroup_page {
  width: 500px;
  height: 780px;
  background-color: white;
  border-radius: 25px;
  margin-bottom: 60px;
}
.header-top {
  width: 500px;
  height: 80px;
  padding-top: 25px;
  padding-left: 40px;
  border-radius: 25px 25px 0 0;
  letter-spacing: 1px;
  font-size: 35px;
  font-weight: bold;
  color: #515151;
}
.group-box {
  position: relative;
  overflow-y: auto;
  width: 500px;
  height: 647px;
  border-radius: 20px 20px 0 0;
}
.plus_group {
  margin-top: 40px;
  margin-left: 52px;
  margin-bottom: 20px;
  width: 400px;
  height: 80px;
  border: 2px solid #d4d4d4;
  border-radius: 20px;
  font-size: 30px;
  font-weight: bold;
  color: #515151;
  background-color: white;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.3);
}
.plus_group:hover {
  background-color: #e9e9e9;
}
.navigationbar {
  width: 500px;
  height: 53px;
  border-radius: 0 0 25px 25px;
  background-color: #f0f0f0;
}
</style>
