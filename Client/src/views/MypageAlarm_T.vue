<template>
  <RouterView></RouterView>
  <div class="mypage_wrap">
    <div class="mypage_menubar">
      <div class="mypage_topic">마이페이지</div>
      <div class="mypage_sub" @click="linkToinfo">회원정보</div>
      <div class="mypage_sub" @click="linkToact" id="nowpage">활동내역</div>
      <div class="mypage_sub" @click="linkToquiz">내 퀴즈</div>
      <div class="mypage_sub" @click="linkToAlarm">알림</div>
    </div>
    <div class="mypage_right">
      <div class="Alarm">알림내역</div>
      <div class="act_line1"></div>
      <div class="alarm-list">
        <table>
          <tr
            v-for="(alarm, index) in notifications"
            :key="index"
            class="alarm-item"
            @click="handleAlarmClick(alarm.id, alarm.type)"
          >
            <td class="alarm-status">
              <span :class="{'alarm-dot': true, 'unread': !alarm.read}"></span>
            </td>
            <td class="alarm-content-wrapper">
              <div class="alarm-content">{{ alarm.message }}</div>
              <div class="alarm-time">{{ formatTime(alarm.createdAt) }}</div>
            </td>
          </tr>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import apiClient from '@/axios/axios';
import { useAuthStore } from '@/store/auth';

export default {
  data() {
    return {
      notifications: [], // 모든 알림 데이터
    };
  },
  computed: {
    // 읽지 않은 알림 필터링
    unreadNotifications() {
      return this.notifications.filter(n => !n.read);
    },
    // 읽은 알림 필터링
    readNotifications() {
      return this.notifications.filter(n => n.read);
    },
  },
  mounted() {
    this.fetchNotifications(); // 컴포넌트가 마운트되면 모든 알림 가져오기
  },
  methods: {
    // 모든 알림 가져오기
    async fetchNotifications() {
      const authStore = useAuthStore();
      const userId = authStore.userInfo?.id;

      if (!userId) {
        console.error('사용자 정보가 없습니다.');
        return;
      }

      try {
        // 모든 알림 가져오기
        const response = await apiClient.get(`${process.env.VUE_APP_API_URL}/api/notifications/all/${userId}`);
        this.notifications = response.data; // 서버에서 받은 모든 알림 저장
      } catch (error) {
        console.error('알림을 가져오는 중 오류 발생:', error);
      }
    },

    // 알림 클릭 시 읽음 처리
    async handleAlarmClick(notificationId, notificationType) {
      try {
        // 알림 읽음 처리
        await apiClient.post(`${process.env.VUE_APP_API_URL}/api/notifications/read/${notificationId}?notificationType=${notificationType}`);
        const notification = this.notifications.find(n => n.id === notificationId);
        if (notification) {
          notification.read = true; // 읽음 처리 후 read 상태를 true로 변경
        }
      } catch (error) {
        console.error('알림 읽음 처리 중 오류 발생:', error);
      }
    },

    // 시간 포맷팅 함수
    formatTime(alarmTime) {
      const now = new Date();
      const alarmDate = new Date(alarmTime);
      const diffInSeconds = Math.floor((now - alarmDate) / 1000);
      const diffInMinutes = Math.floor(diffInSeconds / 60);
      const diffInHours = Math.floor(diffInMinutes / 60);

      if (diffInSeconds < 60) {
        return '방금';
      } else if (diffInMinutes < 60) {
        return `${diffInMinutes}분 전`;
      } else if (diffInHours < 24) {
        return `${diffInHours}시간 전`;
      } else {
        const month = alarmDate.getMonth() + 1;
        const day = alarmDate.getDate();
        return `${month}월 ${day}일`;
      }
    },
    linkToinfo() {
      this.$router.push({ name: 'mypageuserinfo', params: { category: 'mypage' } });
    },
    linkToact() {
      this.$router.push({ name: 'mypageactivity', params: { category: 'mypage' } });
    },
    linkToquiz() {
      this.$router.push({ name: 'mypagequiz', params: { category: 'mypage' } });
    },
    linkToAlarm() {
      this.$router.push({ name: 'mypagealarm', params: { category: 'mypage' } });
    },
  },
};
</script>

<style scoped>
.mypage_wrap{
    width: 1810px;
    height: 3000px;
    margin: 0 auto;
}
.mypage_menubar{
    float: left;
    margin-left: 100px;
    margin-top: 230px;
    width: 200px;
    height: 600px;
}
.mypage_topic {
  width: 200px;
  height: 50px;
  font-size: 32px;
  font-weight: 700;
  color: #000000;
  border-bottom: 2px solid #888686;
}
.mypage_sub:hover {
  color: black;
  font-weight: bold;
  cursor: pointer;
}
.mypage_sub {
  width: 200px;
  font-size: 22px;
  padding-top: 10px;
  line-height: 50px;
  color: #888686;
  transition: color 0.2s, font-weight 0.2s;
}
#nowpage{
    color:#000000;
}
.mypage_right{
    float: left;
    width: 1210px;
    height: 1100px;
}
.Alarm{
  font-size: 32px;
  color: black;
  margin-top: 220px;
  margin-left: 140px;
}
.act_line1{
  width: 1199px;
  border: 3px solid #FFBEBA;
  margin-left: 125px;
  margin-top: 20px;
}
.alarm-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  display: inline-block;
  background-color: black;
  cursor: pointer;
  margin-left: 20px;
}
.alarm-dot.unread {
  background-color: red;
}
.alarm-list{
  margin-left: 125px;
}
.alarm-item{
  display: flex;
  width: 1199px;
  height: 70px;
  background-color: #f9e9e9;
  margin-top: 5px;
  font-size: 20px;
  text-align: center;
  line-height: 70px;
  cursor: pointer;
}
.alarm-item:hover{
  background-color: #FAE0E0;
}
.alarm-content-wrapper {
  display: flex;
  justify-content: space-between;
  width: 100%;
}
.alarm-content {
  text-align: left;
  flex: 1; 
  margin-left: 20px;
}
.alarm-time {
  margin-right: 20px;
}
</style>