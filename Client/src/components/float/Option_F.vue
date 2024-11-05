<template>
  <div class="option_wrap">
    <div class="option_topbar">
      <span style="font-size: 33px; font-weight: bold; color: #515151; padding-left: 30px;">프로필</span>
    </div>
    <div class="optionlist_wrap">
      <div class="profile_wrap">
        <div class="profile_image">
          <img :src="userImg" width="80px" height="80px" style="border-radius: 50px;">
        </div>
        <div class="user_name">{{ userName }}</div>
        <div class="user_comment">{{ userComment }}</div>
        <div class="modify_btn" @click="linkToMypageinfo">정보 수정 하기</div>
      </div>

      <div class="alarm_wrap">
        <div class="alarm_topbar">
          <span style="font-size: 33px; font-weight: bold; color: #515151; padding-left: 30px;">알림설정</span>
        </div>

        <!-- 사이트 전체 알림 설정 -->
        <div class="alarm">
          <div class="alarm_left_wrap">
            <div class="alarm_name">사이트 알림</div>
            <div class="alarm_info">{{ AlarmMessage }}</div>
          </div>
          <div class="form-check form-switch" id="alarm_switch1">
            <input
              class="form-check-input"
              type="checkbox"
              style="width: 40px; height: 20px;"
              v-model="isChecked"
              @change="updateSiteAlarm"
            />
          </div>
        </div>

        <!-- 채팅 전체 알림 설정 -->
        <div class="alarm2">
          <div class="alarm_left_wrap"> 
            <div class="alarm_name">채팅 알림</div>
            <div class="alarm_info">{{ chatAlarmMessage }}</div>
          </div>
          <div class="form-check form-switch" id="alarm_switch2">
            <input
              class="form-check-input"
              type="checkbox"
              style="width: 40px; height: 20px; margin-top: 2.5em;"
              v-model="isChatAlramChecked"
              :disabled="!isChecked"
              @change="updateChatAlarm"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- 네비게이션 바 -->
    <div class="navigationbar">
      <div class="nbox" @click="movefriend">
        <img class="neviImg" src="../../assets/people-fill.svg" width="30px" height="30px">
      </div>
      <div class="nbox" @click="movechat">
        <img class="neviImg" src="../../assets/chat.svg" width="30px" height="30px">
      </div>
      <div class="nbox" @click="movegroup">
        <img class="neviImg" src="../../assets/wechat.svg" width="30px" height="30px">
      </div>
      <div class="nbox" @click="movedrive">
        <img class="neviImg" src="../../assets/storage.svg" width="30px" height="30px">
      </div>
      <div class="nbox" @click="moveoption">
        <img class="neviImg" src="../../assets/option.svg" width="30px" height="30px" style="filter: invert(76%) sepia(23%) saturate(6021%) hue-rotate(312deg) brightness(75%) contrast(105%);">
      </div>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from '@/store/auth';
import apiClient from '@/axios/axios';

export default {
  data() {
    return {
      userId: useAuthStore().userInfo?.id || '',
      userName: useAuthStore().userInfo?.name || '',
      userImg: useAuthStore().userInfo?.picture || '',
      userComment: useAuthStore().userInfo?.comment || '',
      isChatAlramChecked: true,  // 채팅 알림 상태
      isChecked: true,           // 사이트 알림 상태
      previousChatAlramChecked: true,  // 이전 채팅 알림 상태
    };
  },
  computed: {
    chatAlarmMessage() {
      return this.isChatAlramChecked ? '채팅 알림을 받습니다.' : '채팅 알림을 받지 않습니다.';
    },
    AlarmMessage() {
      return this.isChecked ? '사이트 우상단에 알림을 표시합니다.' : '사이트 우상단에 알림을 표시하지 않습니다.';
    },
  },
  methods: {
    // 사이트 알림 변경 시 호출
    async updateSiteAlarm() {
      if (!this.isChecked) {
        // 사이트 알림을 끌 때 이전 채팅 알림을 저장한 후 채팅 알림을 끔
        this.previousChatAlramChecked = this.isChatAlramChecked;  // 채팅 알림 상태 저장
        this.isChatAlramChecked = false;  // 채팅 알림 끔
      } else {
        // 사이트 알림을 켤 때 이전 상태로 복원
        this.isChatAlramChecked = this.previousChatAlramChecked;
      }

      try {
        await apiClient.put(`${process.env.VUE_APP_API_URL}/api/notifications/settings/${this.userId}`, {
          siteAlertsEnabled: this.isChecked,
          chatAlertsEnabled: this.isChatAlramChecked,
        });
      } catch (error) {
        alert('사이트 알림 설정 저장에 실패했습니다.');
      }
    },
    // 채팅 알림 변경 시 호출
    async updateChatAlarm() {
      // 채팅 알림을 끄거나 켜는 경우에만 이전 상태를 업데이트
      this.previousChatAlramChecked = this.isChatAlramChecked;

      try {
        await apiClient.put(`${process.env.VUE_APP_API_URL}/api/notifications/settings/${this.userId}`, {
          siteAlertsEnabled: this.isChecked,
          chatAlertsEnabled: this.isChatAlramChecked,
        });
      } catch (error) {
        alert('채팅 알림 설정 저장에 실패했습니다.');
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
    movedrive() {
      this.$emit('changeComponent', 'DrivePage');
    },
    moveoption() {
      this.$emit('changeComponent', 'OptionPage');
    },
    linkToMypageinfo() {
      this.$router.push({ name: 'mypageuserinfo', params: { category: 'mypage' } });
    },
    // 알림 설정을 서버에서 가져와서 초기화
    async loadNotificationSettings() {
      try {
        const response = await apiClient.get(`${process.env.VUE_APP_API_URL}/api/notifications/settings/${this.userId}`);
        const { siteAlertsEnabled, chatAlertsEnabled } = response.data;

        // 서버에서 받은 값으로 슬라이드 버튼 초기화
        this.isChecked = siteAlertsEnabled;
        this.isChatAlramChecked = chatAlertsEnabled;
        this.previousChatAlramChecked = chatAlertsEnabled;  // 초기 상태 저장
      } catch (error) {
        alert('알림 설정을 불러오는 데 실패했습니다.');
      }
    },
  },
  mounted() {
    // 페이지가 로드될 때 서버에서 알림 설정을 가져와서 상태 초기화
    this.loadNotificationSettings();
  },
};
</script>

<style>
  .neviImg:hover{
    filter: invert(76%) sepia(23%) saturate(6021%) hue-rotate(312deg) brightness(75%) contrast(105%);
  }
  .option_wrap {
    position: relative;
    width: 500px;
    height: 780px;
    overflow-y: auto; 
    background-color: white;
    border-radius: 25px;
    margin-bottom: 60px;
  }
  .option_topbar{
    width: 500px;
    height: 80px;
    padding-top: 25px;
    border-radius: 25px 25px 0 0;
  }
  .alarm_topbar{
    width: 470px;
    height: 80px;
    padding-top: 25px;
    border-radius: 25px 25px 0 0;
  }
  .optionlist_wrap{
    width: 500px;
    height: 647px;
    position: relative; 
    overflow-y: auto;
  }
  .profile_wrap{
    width: 328px;
    height: 299px;
    border-radius: 15px;
    border: 1px solid #D4D4D4;
    margin: auto;
    margin-top: 5px;
    box-shadow: 0px 5px 5px -2px gray;
  }
  .navigationbar {
    width: 500px;
    height: 53px;
    border-radius: 0 0 25px 25px;
    background-color: #F0F0F0;
  }
  .profile_image{
    display: block;
    width: 80px;
    height: 80px;
    margin: auto;
    margin-top: 40px;
  }
  .user_name{
    display: block;
    overflow: hidden;
    height: 33px;
    font-weight: bold;
    font-size: 25px;
    color: #515151;
    margin-top: 10px;
    text-align: center;
  }
  .user_comment{
    height: 20px;
    display: block;
    overflow: hidden;
    font-weight: medium;
    font-size: 15px;
    color: #515151;
    margin-top: 10px;
    text-align: center;
  }
  .modify_btn{
    width: 130px;
    height: 44px;
    line-height: 44px;
    border: none;
    border-radius: 10px;
    box-shadow: 0 5px 5px #888686;
    background-color: #FFA0A0;
    color: #FFFFFF;
    font-size: 18px;
    font-weight: 800;
    margin: auto;
    margin-top: 30px;
    text-align: center;
    cursor: pointer;
    transition: background-color 0.2s;
  }
  .modify_btn:hover{
    background-color: #fd9696;
  }
  .alarm_wrap{
    width:470px;
    height: 340px;
    overflow-y: auto;
  }
  .alarm{
    display: flex;
    align-content : space-between;
    width: 328px;
    height: 63px;
    border: 1px solid #D4D4D4;
    border-radius: 10px;
    background-color: #FFFFFF;
    box-shadow: 0px 5px 5px -2px gray;
    margin: auto;
    margin-top: 10px;
  }
  .alarm2{
    display: flex;
    align-content : space-between;
    width: 328px;
    height: 63px;
    border: 1px solid #D4D4D4;
    border-radius: 10px;
    background-color: #FFFFFF;
    box-shadow: 0px 5px 5px -2px gray;
    margin: auto;
    margin-top: 20px;
  }
  .alarm_left_wrap{
    width: 250px;
    height: 63px;
    float: left;
  }
  .alarm_name{
    width: 163px;
    max-width: 230px;
    overflow: hidden;
    display: inline-block;
    height: 21px;
    font-weight: blod;
    font-size: 16px;
    color: #4A4A4A;
    margin-top: 3px;
    margin-left: 10px;
  }
  .alarm_info{
    width: 250px;
    display: inline-block;
    height: 20px;
    font-weight: bold;
    font-size: 12px;
    color: #757575;
    margin-top: 2px;
    margin-left: 10px;
  }
  #on_switch{
    margin-top: 15px;
    margin-left: 16px;
  }
  #off_switch{
    margin-top: 15px;
    margin-left: 16px;
  }
  .alarm_savebtn{
    width: 110px;
    height: 34px;
    line-height: 34px;
    border-radius: 8px;
    background-color: #FF9898;
    color: #FFFFFF;
    font-size: 16dpx;
    margin: auto;
    margin-top: 30px;
    margin-right: 10px;
    text-align: center;
    cursor: pointer;
  }
  .alarm_savebtn:hover{
    background-color: #f07a74;
  }
  #alarm_switch1{
    margin-top: 17px;
    margin-left: 20px;
  }
  #alarm_switch2{
    margin-top: -19px;
    margin-left: 20px;
  }
</style>