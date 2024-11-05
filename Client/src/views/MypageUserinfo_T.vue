<template>
<RouterView></RouterView>
<div class="mypage_wrap">
    <div class="mypage_menubar">
        <div class="mypage_topic">마이페이지</div>
        <div class="mypage_sub" @click="linkToinfo" id="nowpage">회원정보</div>
        <div class="mypage_sub" @click="linkToact">활동내역</div>
        <div class="mypage_sub" @click="linkToquiz">내 퀴즈</div>
        <div class="mypage_sub" @click="linkToAlarm">알림</div>
    </div>
  <div class="mypage_right">
    <div class="info_change">회원정보 변경</div>
    <div class="info_line1"></div>
    <div class="email_wrap">
      <div class="email_text">이메일</div>
      <div class="email_blank">{{ userEmail }}</div>
    </div>
    <div class="info_line2"></div>
    <div class="nick_wrap">
      <div class="nick_text">닉네임</div>
      <input v-model="userName" type="text" class="nick_blank">
      <div class="nick_con">한글, 영문, 숫자 혼용가능 (한글 기준 10자 이내)</div>
    </div>
    <div class="info_line3"></div>
    <div class="introduce_wrap">
      <div class="introduce_text">상태 메시지</div>
      <textarea v-model="userComment" type="text" class="introduce_blank"></textarea>
    </div>
    <div class="info_line4"></div>
    <div class="profile_wrap1">
      <div class="profile_text">프로필이미지</div>
      <img class="profile_image" :src="this.userImg">
      <div class="profile_upbtn">등록</div>
      <div class="profile_delbtn">삭제</div>
      <div class="profile_con">프로필 이미지는 가로 161px 썸네일로 생성됩니다</div>
    </div>
    
    <div class="info_line5"></div>
    <button class="profile_savebtn" @click="updateValue"><span style="color:white">수정</span></button>
  </div>
</div>
</template>

<script>
import { useAuthStore } from '@/store/auth';
import { computed } from 'vue';
import apiClient from '@/axios/axios'; 

export default {
  setup() {
    const authStore = useAuthStore();
    const user = computed(() => authStore.userInfo);

    return {
      user,
      authStore,
    };
  },
  data() {
    const authStore = useAuthStore();
    return {
      userEmail: authStore.userInfo?.email || '',
      userName: authStore.userInfo?.name || '',
      userImg: authStore.userInfo?.picture || '',
      userComment: authStore.userInfo?.comment || '',
      originalUserName: authStore.userInfo?.name || '',
      originalUserComment: authStore.userInfo?.comment || '',
    };
  },
  methods: {
    linkToinfo() {
      this.$router.push({ name: 'mypageuserinfo', params: { category: "mypage" } });
    },
    linkToact() {
      this.$router.push({ name: 'mypageactivity', params: { category: "mypage" } });
    },
    linkToquiz() {
      this.$router.push({ name: 'mypagequiz', params: { category: "mypage" } });
    },
    linkTooption() {
      this.$router.push({ name: 'mypageoption', params: { category: "mypage" } });
    },
    linkToAlarm() {
      this.$router.push({ name: 'mypagealarm', params: { category: "mypage" } });
    },
    async updateValue() {
      try {
        const response = await apiClient.put(`${process.env.VUE_APP_API_URL}/api/user/update`, {
          name: this.userName,
          comment: this.userComment
        });
        const updatedInfo = response.data;
        this.authStore.setAuth(this.authStore.token, { ...this.authStore.userInfo, ...updatedInfo });

        alert("유저 정보가 성공적으로 수정되었습니다.");
      } catch (error) {
        alert("유저 정보 수정에 실패했습니다.");
      }
    }
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
.info_change{
  font-size: 32px;
  color: black;
  margin-top: 220px;
  margin-left: 140px;
}
.info_line1{
  width: 1199px;
  border: 3px solid #FFBEBA;
  margin-left: 125px;
  margin-top: 20px;
}
.email_wrap{
  display: flex;
  align-content : space-between;
  width: 1190px;
  height: 107px;
  margin-left: 125px;
}
.email_text{
  width: 55px;
  height: 26px;
  margin-top: 40px;
  font-size: 16px;
}
.email_blank{
  width: 252px;
  height: 44px;
  margin-top: 30px;
  margin-left: 38px;
  background-color: #FFBEBA;
  border-radius: 5px;
  font-size: 16px;
  color: #5D5D5D;
  line-height: 44px;
  text-align: center;
}
.info_line2{
  width: 1199px;
  border: 3px solid #FAE9E9;
  margin-left: 125px;
  margin-top: 0;
}
.nick_wrap{
  display: flex;
  align-content : space-between;
  width: 1190px;
  height: 107px;
  margin-left: 125px;
}
.nick_text{
  width: 55px;
  height: 26px;
  margin-top: 40px;
  font-size: 16px;
}
.nick_blank{
  width: 252px;
  height: 44px;
  margin-top: 30px;
  margin-left: 38px;
  background-color: #FAE9E9;
  border-radius: 5px;
  border: none;
  font-size: 16px;
  color: #000000;
  line-height: 44px;
  text-align: center;
}
.nick_con{
  width: 350px;
  height: 26px;
  margin-top: 40px;
  margin-left: 130px;
  font-size: 16px;
}
.info_line3{
  width: 1199px;
  border: 3px solid #FAE9E9;
  margin-left: 125px;
  margin-top: 0;
}
.introduce_wrap{
  display: flex;
  align-content : space-between;
  width: 1190px;
  height: 107px;
  margin-left: 125px;
}
.introduce_text{
  width: 70px;
  height: 26px;
  margin-top: 20px;
  font-size: 16px;
  white-space: nowrap;
}
.introduce_blank{
  width: 350px;
  height: 218px;
  margin-top: 20px;
  margin-left: 25px;
  background-color: #FFFFFF;
  border: 0.5px solid #FF9A95;
  font-size: 14px;
  color: #000000;
  text-align: top;
  padding: 10px;
  resize: none;
  box-sizing: border-box;
}
.info_line4{
  width: 1199px;
  border: 3px solid #FAE9E9;
  margin-left: 125px;
  margin-top: 160px;
}
.profile_wrap1{
  display: flex;
  align-content : space-between;
  width: 1190px;
  height: 107px;
  margin-left: 125px;
}
.profile_text{
  width: 100px;
  height: 26px;
  margin-top: 20px;
  font-size: 16px;
}
.profile_image{
  width: 198px;
  height: 198px;
  margin-top: 20px;
  margin-left: 25px;
  border: 1px solid black;
}
.profile_upbtn{
  width: 48px;
  height: 25px;
  margin-left: 22px;
  margin-top: 30px;
  border-radius: 30px;
  border: 1px solid #D0D0D0;
  text-align: center;
  font-size: 12px;
  cursor: pointer;
  line-height: 25px;
}
.profile_delbtn{
  width: 48px;
  height: 25px;
  margin-left: 12px;
  margin-top: 30px;
  border-radius: 30px;
  border: 1px solid #D0D0D0;
  text-align: center;
  font-size: 12px;
  cursor: pointer;
  line-height: 25px;
}
.profile_con{
  width: 276px;
  height: 860px;
  margin-top: 76px;
  margin-left: -106px;
  color: #979797;
  font-size: 12px;
}
.info_line5{
  width: 1199px;
  border: 3px solid #FAE9E9;
  margin-left: 125px;
  margin-top: 140px;
}
.profile_savebtn{
    margin-top: 30px;
    margin-left: 1210px;
    background-color: #FFA0A0;
    width: 111px;
    height: 47px;
    border: none;
    border-radius: 10px;
    transition: background-color 0.2s;
    font-size: 18px;
    font-weight: 800;
    box-shadow: 0 5px 5px #888686;
}
.profile_savebtn:hover{
  background-color: #fd9696;
}
</style>