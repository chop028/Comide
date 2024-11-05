<template>
  <div class="apply-wrap">
    <div class="header-bar">
      협업 지원서
      <img src="@/assets/x.svg" class="refusal-box" @click="confirmRequest(false)"/>
      <img src="@/assets/check-lg.svg" class="accept-box" @click="confirmRequest(true)"/>
    </div>
    <div class="making-body-box">
      <div class="get-name">
        <span class="application-title">닉네임</span><br />
        <div class="get-applyname">{{ ap.userName }}</div>
      </div>
      <div class="application-field">
        <span class="application-title">신청 분야</span><br />
        <div v-if="ap.apData.opt1 != null" class="field-list">
          프론트엔드
        </div>
        <div v-if="ap.apData.opt2 != null" class="field-list">
          백엔드
        </div>
        <div v-if="ap.apData.opt3 != null" class="field-list">
          디자이너
        </div>
        <div v-if="ap.apData.opt4 != null" class="field-list">
          {{ ap.apData.opt4 }}
        </div>
      </div>
      <div class="lang-and-frame">
        <span class="application-title">사용가능 언어 및 프레임워크</span><br/>
        <div class="lang-frame-box">{{ ap.apData.techStack }}</div>
      </div>
      <div class="self-introduction">
        <span class="application-title">간단한 자기소개</span><br />
        <div class="introduction-box">{{ ap.apData.comment }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import apiClient from '@/axios/axios';

export default {
  data(){
    return{
      options: [],
    }
  },
  props: {
    ap: {
      type: Object,
      required: true,
    },
  },
  methods: {
    confirmRequest(app) {
      const confirmData = {
        groupRequestDto: this.ap.apData,
        approval: app
      }
      apiClient
        .post(`${process.env.VUE_APP_API_URL}/api/group/confirm-group-request.do`, confirmData)
        .then((response) => {
        if (response.data.success) {
            this.$emit('close');
        }else{
            console.error("get-board-list failed");
            this.$emit('close');
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
  },
};
</script>

<style scoped>
.apply-wrap {
  position: fixed;
  width: 500px;
  height: 780px;
  bottom: 0;
  border-radius: 25px;
  background-color: white;
  z-index: 9999;
}

.header-bar {
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

.accept-box {
  float: right;
  margin-top: 5px;
  width: 40px;
  height: 40px;
  cursor: pointer;
}

.refusal-box {
  float: right;
  margin-right: 40px;
  margin-left: 20px;
  width: 50px;
  height: 50px;
  cursor: pointer;
}

.making-body-box {
  position: relative;
  width: 500px;
  height: 648px;
}

.get-name {
  margin: auto;
  margin-top: 20px;
  padding-top: 10px;
  width: 450px;
  height: 100px;
}

.get-applyname {
  margin-top: 8px;
  width: 450px;
  height: 35px;
  border: 2px solid #d4d4d4;
  border-radius: 8px;
  color: #515151;
}

.application-field {
  margin: auto;
  padding-top: 10px;
  width: 450px;
  height: 130px;
  overflow-y: auto;
}

.field-list {
  letter-spacing: 1px;
  padding-top: 15px;
  color: #515151;
  font-weight: bold;
  float: left;
  margin-top: 8px;
  width: 95px;
  height: 60px;
  border: 2px solid #d4d4d4;
  border-radius: 8px;
  text-align: center;
}

.lang-and-frame {
  margin: auto;
  padding-top: 10px;
  width: 450px;
  height: 170px;
}

.lang-frame-box {
  margin-top: 8px;
  width: 450px;
  height: 100px;
  border: 2px solid #d4d4d4;
  border-radius: 8px;
}

.self-introduction {
  margin: auto;
  padding-top: 10px;
  width: 450px;
  height: 200px;
}

.introduction-box {
  margin-top: 8px;
  width: 450px;
  height: 140px;
  border: 2px solid #d4d4d4;
  border-radius: 8px;
}

.navigationbar {
  width: 500px;
  height: 52px;
  border-radius: 0 0 25px 25px;
  background-color: #f0f0f0;
}

.application-title {
  font-size: 22px;
  font-weight: bold;
  color: #515151;
  padding-left: 18px;
}

.nbox {
  cursor: pointer;
}
</style>