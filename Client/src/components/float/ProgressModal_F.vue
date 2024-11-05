<template>
  <div class="modal-box">
    <div class="modalcontent">
      <div class="headbar">
        <span class="head-title">진행도</span>
        <button class="close-modal-btn" @click="closeModal">X</button>
      </div>
      <div class="progress-container">
        <div v-for="(goal, index) in goals" :key="index" :class="{ 'goal-box': true, 'clear': isGoalCompleted(goal) }">
          <span class="goal-name">{{ goal.taskName }}</span>
          <div class="goal-num">{{ goal.crtProgress }} / {{ goal.overall }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import apiClient from '@/axios/axios';
export default {
  props: {
    groupId: {
      type: Number,
      required: true
    },
  },
  data() {
    return {
      goals: []
    };
  },
  methods: {
    closeModal() {
      this.$emit('close');
    },
    isGoalCompleted(goal) {
      return goal.crtProgress === goal.overall;
    },
    getGroupTask(){
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/group/get-group-task-list.do?groupId=${this.groupId}`)
        .then((response) => {
          if (response.data.success) {
            this.goals = response.data.result;
          } else {
            console.error("그룹 정보 받아오기 실패!");
          }
        })
        .catch(function(error) {
          console.error(error);
        });
    },
  },
  mounted(){
    this.getGroupTask();
  },
};
</script>

<style>
.modal-box {
  padding-top: 180px;
  position: fixed; 
  width: 500px;
  height: 780px;
  bottom: 0;
  border-radius: 25px;
  background: rgba(102, 102, 102, 0.5);
  z-index: 9999;
}

.modalcontent {
  width: 450px;
  height: 380px;
  border-radius: 20px;
  border: 1px solid #D4D4D4;
  background: linear-gradient(180deg, rgba(255,225,225,1) 1%, rgba(255,255,255,1) 60%);
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.5);
  margin-left: 25px;
}

.headbar {
  width: 450px;
  height: 70px;
  padding-top: 25px;
  border-radius: 25px 25px 0 0;
}

.head-title {
  margin-left: 30px;
  color: #515151;
  font-weight: bold;
  font-size: 25px;
}

.progress-container {
  position: relative;
  overflow: scroll;
  margin: auto;
  width: 450px;
  height: 300px;
}

.goal-box {
  padding-top: 6px;
  margin-left: 25px;
  margin-top: 10px;
  border-radius: 8px;
  border: 1.5px solid #D4D4D4;
  background-color: white;
  width: 400px;
  height: 40px;
}

.goal-name {
  letter-spacing: 1px;
  margin-left: 20px;
  color: #515151;
  font-weight: bold;
}

.goal-num {
  letter-spacing: 1px;
  margin-right: 10px;
  float: right;
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
.clear {
  border-color: #FFB9B9;
}
</style>