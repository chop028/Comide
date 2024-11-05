<template>
  <div class="modal-box">
    <div class="modalcontent">
      <div class="headbox">
        <span class="title-box">그룹 신청</span>
        <button class="close-modal-btn" @click="closeModal">X</button>
      </div>
      <div class="apply-container">
        <div class="apply-box" v-for="(apply, index) in applies" :key="index" @click="moveGroupApply(applies[index])">
          <span class="apply-name">{{ apply.userName || apply.userId }}</span>
        </div>
      </div>
    </div>
  </div>
  <component :is="currentComponent" v-if="isChangeVisible" :ap="crtData" @close="handleClose"></component>
</template>

<script>
import GroupApplication from '@/components/float/GroupApplication_F.vue';
import apiClient from '@/axios/axios';

export default {
  components: {
    GroupApplication
  },
  props: {
    groupId: {
      type: Number,
      required: true
    },
  },
  emits: ['close', 'closeAndLoad', 'newApplication'],
  data() {
    return {
      currentComponent: null,
      isChangeVisible: false,
      applies: [],
      crtData: null,
    };
  },
  methods: {
    closeModal() {
      this.$emit('close');
    },
    handleClose() {
      this.isChangeVisible = false;
      this.currentComponent = null;
      this.crtData = null;
      this.loadRequest();
    },
    async loadRequest() {
      try {
        const response = await apiClient.get(`${process.env.VUE_APP_API_URL}/api/group/get-group-request.do?groupId=${this.groupId}`);
        if (response.data.success) {
          this.applies = response.data.result.map(apply => ({
            userId: apply.userId,
            userName: '',
            apData: apply
          }));
          this.applies.forEach(apply => this.loadUserName(apply));
          if (this.applies.length > 0) {
            this.$emit('new-application');
          }
        } else {
          console.error("그룹 요청 데이터를 불러오는 중 오류 발생");
        }
      } catch (error) {
        console.error(error);
      }
    },
    async loadUserName(apply) {
      try {
        const response = await apiClient.get(`${process.env.VUE_APP_API_URL}/api/user/get-user-data`, {
          params: {
            userId: apply.userId
          }
        });

        if (response.data.success) {
          apply.userName = response.data.result.name;
        } else {
          console.error("사용자 이름을 불러오는 중 오류 발생: 성공하지 못했습니다.");
        }
      } catch (error) {
        console.error("에러 발생:", error);
      }
    },
    moveGroupApply(ap) {
      this.crtData = ap;
      this.isChangeVisible = true;
      this.currentComponent = 'GroupApplication';
    }
  },
  mounted() {
    this.loadRequest();
  }
};
</script>

<style scoped>
.modal-box {
  padding-top: 180px;
  position: fixed; 
  width: 500px;
  height: 780px;
  bottom: 0;
  border-radius: 25px;
  background: rgba(102, 102, 102, 0.5);
  z-index: 9998;
}

.modalcontent {
  width: 450px;
  height: 380px;
  border-radius: 20px;
  border: 1px solid #D4D4D4;
  background: linear-gradient(180deg, rgba(255, 225, 225, 1) 1%, rgba(255, 255, 255, 1) 60%);
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.5);
  margin-left: 25px;
}

.headbox {
  width: 450px;
  height: 70px;
  padding-top: 25px;
  border-radius: 25px 25px 0 0;
}

.title-box {
  margin-left: 30px;
  color: #515151;
  font-weight: bold;
  font-size: 25px;
  letter-spacing: 1px;
}

.apply-container {
  position: relative;
  overflow: scroll;
  margin: auto;
  width: 450px;
  height: 300px;
}

.apply-box {
  padding-top: 6px;
  margin-left: 25px;
  margin-top: 10px;
  border-radius: 8px;
  border: 1.5px solid #D4D4D4;
  background-color: white;
  width: 400px;
  height: 40px;
}
.apply-box:hover {
  border: 2px solid #FF9898;
  cursor: pointer;
}
.apply-name {
  letter-spacing: 1px;
  margin-left: 20px;
  color: #515151;
  font-weight: bold;
}

.close-modal-btn {
  margin-right: 10px;
  float: right;
  border: none;
  background-color: transparent;
}
</style>