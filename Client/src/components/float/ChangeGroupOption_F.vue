<template>
  <div class="option-wrap">
    <button class="backbtn" @click="closeModal">
      <img src="@/assets/arrow-left-short.svg" width="45px" height="45px" />
    </button>
    <div class="option-title">{{ this.groupName }}
      <button class="zoom-inbtn" @click="zoomIn"><img src="@/assets/zoom_in.svg" width="30px" height="30px"></button>
    </div>

    <!-- 멤버 섹션 -->
    <div class="member-wrap">
      <div class="info member">
        구성원 ( {{ currentMembers }} / {{ this.groupData.headCnt }} )
        <img class="modify-progress" src="@/assets/pencil.svg" @click="toggleEditMembers">
        <img v-if="showCheckImageMembers" class="modify-complete" src="@/assets/check-lg.svg" @click="saveMembers">
      </div>

      <div class="member-box">
        <div
          v-for="(friend, index) in friends"
          :key="index"
          :class="`profile ${friend.className}`"
        >
          <img :src="friend.image" class="profile-img" />
          <div class="profile-name">{{ friend.userName }}</div>
          <button v-if="isEditingMembers" @click="removeMember(index)" class="remove-btn"><img src="@/assets/minus.svg"></button>
        </div>
      </div>

      <div v-if="isEditingMembers" class="add-member-box">
          <input v-model="newMemberName" placeholder="친구 이름" class="add-member-input">
          <button @click="addMember" class="add-member-btn">추가</button>
      </div>
    </div>

    <!-- 작업 관리 섹션 -->
    <div class="task-wrap">
      <div class="info-task" style="margin-left: 27px;">
        작업 관리
        <img class="modify-task" src="@/assets/pencil.svg" @click="toggleAdditionalTasks">
        <img v-if="showCheckImage" class="modify-complete" src="@/assets/check-lg.svg" @click="saveTasks">
      </div>
      <div class="task-option">
        <div v-for="(task, index) in tasks" :key="index" :class="{ 'task-box': true, 'clear': isTaskCompleted(task) }">
          <div v-if="!isEditingTasks">
            <span class="task-name">{{ task.taskName }}</span>
            <div class="task-num">{{ task.crtProgress }} / {{ task.overall }}</div>
          </div>
          <div v-else>
            <input v-model="task.taskName" class="modify-task-box">
            <div class="task-num">
              <input class="crtProgress-change" type="range" min="0" :max="task.overall" step="1" v-model="task.crtProgress">  
              {{ task.crtProgress }}
            </div>
          </div>
        </div>
        <div v-if="showAdditionalTasks">
          <div v-for="(newTask, index) in newTasks" :key="index" class="add-task">
            <input v-model="newTask.name" class="task-box">
          </div>
          <button class="plus-task" @click="addTask">+</button>
        </div>
      </div>
    </div>

    <!-- 진행일 섹션 -->
    <div class="timeline-wrap">
        <div class="info timeline" style="margin-left: 27px;">
          시작일
        </div>
        <div class="timeline-option">
          <div class="startline">
            <input class="date-box" type="date" v-model="groupData.startDate">
          </div>
          <div class="info timeline" style="margin-left: 27px;">
            마감일
          </div>
          <div class="deadline">
            <input class="date-box" type="date" v-model="groupData.endDate">
          </div>
          <button class="finishbtn" @click="saveInput">수정 완료</button>
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
    groupName: {
      type: String,
      require: true
    }
  },
  data() {
    return {
      currentMembers: 0,
      groupData: '',
      memberData: [],
      friends: [],
      tasks: [],
      showAdditionalTasks: false,
      showCheckImage: false,
      showCheckImageMembers: false, // 변수 초기화 추가
      isEditingTasks: false,
      isEditingMembers: false,
      newTasks: [],
      newMemberName: '',
      backupTasks: [],
      backupFriends: [],
      showCheckImageMem: ''
    };
  },
  methods: {
    saveInput(){
      apiClient
        .post(`${process.env.VUE_APP_API_URL}/api/group/update-group-task.do`, this.tasks)
        .then((response) => {
        if (response.data.success) {
            this.$emit('closeAndReload');
        }else{
            console.error("get-board-list failed");
        }
        })
        .catch(function(error) {
        console.error(error);
        });
    },
    getGroupTask(){
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/group/get-group-task-list.do?groupId=${this.groupId}`)
        .then((response) => {
          if (response.data.success) {
            this.tasks = response.data.result;
          } else {
            console.error("받아오기 실패!");
          }
        })
        .catch(function(error) {
          console.error(error);
        });
    },
    getGroupData(){
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/group/get-group-detail.do?groupId=${this.groupId}`)
        .then((response) => {
          if (response.data.success) {
            this.groupData = response.data.result;
            this.getGroupTask();
          } else {
            console.error("그룹 데이터 받아오기 실패!");
          }
        })
        .catch(function(error) {
          console.error(error);
        });
    },
    getMemberData() {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/group/get-group-member.do?groupId=${this.groupId}`)
        .then((response) => {
          if (response.data.success) {
            this.friends = response.data.result.map(userData => ({
              userId: userData.userId,
              userName: '',
              image: '',
            }));
            this.friends.forEach(userData => this.loadUser(userData));
            this.currentMembers = this.friends.length;
          } else {
            console.error("멤버 데이터 받아오기 실패!");
          }
        })
        .catch(function(error) {
          console.error(error);
        });

    },
    async loadUser(userData) {
      try {
        const response = await apiClient.get(`${process.env.VUE_APP_API_URL}/api/user/get-user-data?userId=${userData.userId}`);
        
        if (response.data.success) {
          userData.userName = response.data.result.name;
          userData.image = response.data.result.picture;
        } else {
          console.error("사용자 데이터를 불러오는 중 오류 발생");
        }
      } catch (error) {
        console.error("에러 발생:", error);
      }
    },
    closeModal() {
      this.$emit('close');
    },
    isTaskCompleted(task) {
      return task.overall !== 0 && task.crtProgress === task.overall;
    },
    getImagePath(image) {
      return require(`@/assets/${image}`);
    },
    addTask() {
      this.newTasks.push({ taskName: '', crtProgress: 0, overall: 0 });
    },
    toggleAdditionalTasks() {
      if (this.isEditingTasks) {
        this.tasks = JSON.parse(JSON.stringify(this.backupTasks));
        this.showCheckImage = false;
        this.showAdditionalTasks = false;
      } else {
        this.backupTasks = JSON.parse(JSON.stringify(this.tasks));
        this.showCheckImage = true;
        this.showAdditionalTasks = true;
      }
      this.isEditingTasks = !this.isEditingTasks;
    },
    saveTasks() {
      this.isEditingTasks = false;
      this.showCheckImage = false;
      this.showAdditionalTasks = false;
    },
    validateTaskProgress(task) {
      if (task.crtProgress > task.overall) {
        task.crtProgress = task.overall;
      }
    },
    toggleEditMembers() {
      if (this.isEditingMembers) {
        this.friends = JSON.parse(JSON.stringify(this.backupFriends));
        this.showCheckImageMembers = false;
      } else {
        this.backupFriends = JSON.parse(JSON.stringify(this.friends));
        this.showCheckImageMembers = true;
      }
      this.isEditingMembers = !this.isEditingMembers;
    },
    addMember() {
      if(this.newMemberName.trim() && this.friends.length < this.maxMembers) {
        this.friends.push({ name: this.newMemberName, className: `friend${this.friends.length}`, image: 'profile.svg' });
        this.newMemberName = '';
      }
    },
    removeMember(index) {
      this.friends.splice(index, 1);
    },
    saveMembers() {
      this.isEditingMembers = false;
      this.showCheckImageMembers = false;
    },
    zoomIn() {
      this.$router.push({
          name: 'changegroup_option',
          query: {
              groupId: this.groupId,
              groupName: this.groupName
          }
      });
      this.$emit('changeComponent', 'MyGroupPage');
    }
  },
  mounted() {
    this.getGroupData();
    this.getMemberData();
  },
};
</script>

<style scoped>
.crtProgress-change {
  accent-color: #FF9898;
}
.option-wrap {
    position: fixed;
    width: 500px;
    height: 780px;
    border-radius: 25px;
    bottom: 0;
    background-color: white;
    z-index: 100;
    overflow: auto;
}
.option-title {
    width: 500px;
    height: 80px;
    padding-top: 25px;
    padding-left: 40px;
    border-radius: 25px 25px 0 0;
    letter-spacing: 2px;
    font-size: 35px;
    font-weight: bold;
    color: #515151;
}
.backbtn {
  float: left;
  margin-left: 20px;
  margin-top: 30px;
  margin-right: 15px;
  background-color: transparent;
  border: none;
}
.zoom-inbtn {
  margin-right: 20px;
  float: right;
  width: 50px;
  height: 50px;
  border: none;
  filter: invert(27%) sepia(4%) saturate(231%) hue-rotate(320deg) brightness(106%) contrast(86%);
  background-color: transparent;
}
.member-wrap {
    margin: auto;
    width: 450px;
    height: 220px;
}
.info {
    padding-top: 10px;
    margin-top: 20px;
    width: 450px;
    height: 50px;
    font-weight: bold;
    color: #515151;
    font-size: 25px;
    background-color: transparent;
}
.member-box {
  width: 450px;
    margin-top: 10px;
    float: left;
    height: 120px;
    background-color: white;
    border: 2px solid #D4D4D4;
    border-radius: 8px;
    overflow: auto;
    white-space: nowrap;
}
.profile {
    margin-left: 10px;
    margin-top: 10px;
    width: 100px;
    height: 100px;
    position: relative;
}
.profile-img {
  border-radius: 25px;
  margin-top: 10px;
  margin-left: 23px;
  width: 50px;
  height: 50px;
}
.profile-name {
  text-align: center;
  margin-top: 5px;
  margin-right: 5px;
  color: #515151;
  font-weight: bold;
  font-size: 22px;
}
.remove-btn {
    position: absolute;
    top: 0;
    right: 0;
    border: none;
    background-color: transparent;
    color: red;
    font-size: 20px;
    cursor: pointer;
}
.add-member-box {
    align-items: center;
}
.add-member-input {
  width: 390px;
    flex: 1;
    margin-right: 10px;
    margin-top: 15px;
    padding: 5px;
    border: 1px solid #d4d4d4;
    border-radius: 8px;
}
.add-member-btn {
    padding: 5px 10px;
    background-color: #ff9898;
    color: white;
    border: none;
    border-radius: 8px;
    cursor: pointer;
}
.add-member-btn:hover {
  background-color: #ff8989;
}
.task-wrap {
    width: 500px;
}
.task-input {
  height: 22px;
  color: #515151;
  font-weight: bold;
}
.level-wrap {
    width: 500px;
}
.stack-wrap {
  width: 500px;
}
.timeline-wrap {
  width: 500px;
}
.info-task {
    padding-top: 10px;
    margin-top: 20px;
    width: 450px;
    height: 50px;
    font-weight: bold;
    color: #515151;
    font-size: 25px;
    background-color: transparent;
}
.task-option {  
  width: 500px;
}
.progress-input {
  text-align: center;
  width: 40px;
  height: 20px;
  border: none;
  font-weight: bold;
  color: #515151;
}
.total-input {
  text-align: center;
  width: 40px;
  height: 20px;
  border: none;
  font-weight: bold;
  color: #515151;
}
.modify-task {
  margin-bottom: 5px;
  margin-left: 10px;
  width: 20px;
  height: 20px;
  cursor: pointer;
}
.modify-progress {
  margin-bottom: 5px;
  margin-left: 10px;
  width: 20px;
  height: 20px;
  cursor: pointer;
}
.modify-complete {
  margin-top: 12px;
  float: right;
  width: 25px;
  height: 25px;
  cursor: pointer;  
}
.delete-task {
  position: relative;
  float: right;
  width: 20px;
  height: 20px;
  cursor: pointer;
}
.level-option {  
  width: 500px;
}
.description-option {  
  width: 500px;
}
.timeline-option {  
  width: 500px;
}
.description-box{
    margin-top: 8px;
    margin-left: 25px;
    width: 450px;
    height: 110px;
    border: 2px solid #D4D4D4;
    border-radius: 8px;
}
.groupDescription-box{
    margin-left: 25px;
    width: 450px;
    height: 110px;
    border: 1.5px solid #D4D4D4;
    border-radius: 8px;
}
.stack-option {  
  width: 500px;
  background-color: aquamarine;
}
.task-box {
  padding-top: 6px;
  margin-top: 10px;
  margin-left: 25px;
  border-radius: 8px;
  border: 1.5px solid #D4D4D4;
  background-color: white;
  width: 450px;
  height: 40px;
}
.modify-task-box {
  padding-bottom: 5px;
  margin-left: 20px;
  border-radius: 8px;
  background-color: white;
  height: 30px;
  border: none;
  color: #515151;
  font-weight: bold;
}
.task-name {
  letter-spacing: 1px;
  margin-left: 20px;
  color: #515151;
  font-weight: bold;
}
.task-num {
  letter-spacing: 1px;
  margin-right: 10px;
  float: right;
  margin-left: 20px;
  color: #515151;
  font-weight: bold;
}
.plus-task{
    margin-top: 10px;
    margin-left: 25px;
    width: 450px;
    height: 40px;
    font-weight: bold;
    font-size: 18px;
    color: #515151;
    border: 1.5px solid #D4D4D4;
    border-radius: 8px;
    background-color: white;
}
.plus_task:hover{
  background-color: #e9e9e9;
}
.clear {
  border-color: #FFB9B9;
}
.level-box {
  margin-top: 10px;
  margin-left: 25px;
  padding-top: 6px;
  border-radius: 8px;
  border: 1.5px solid #D4D4D4;
  background-color: white;
  width: 450px;
  height: 40px;
}
.level-name {
  letter-spacing: 1px;
  margin-left: 20px;
  color: #515151;
  font-weight: bold;
}
.stack-box{
    margin-top: 8px;
    margin-left: 15px;
    width: 80px;
    height: 50px;
    font-weight: bold;
    font-size: 18px;
    color: #515151;
    border: 2px solid #D4D4D4;
    border-radius: 8px;
    background-color: white;
}
.plus-stack{
    margin-top: 8px;
    margin-left: 15px;
    width: 80px;
    height: 50px;
    font-weight: bold;
    font-size: 18px;
    color: #515151;
    border: 2px solid #D4D4D4;
    border-radius: 8px;
    background-color: white;
}
.plus-stack:hover{
  background-color: #e9e9e9;
}
.task-input {
  width: 100%;
  border: none;
  padding: 5px;
  font-size: 14px;
}
.level-input {
  width: 100%;
  border: none;
  font-size: 14px;
  color: #515151;
  font-weight: bold;
}
.startline{
    margin: auto;
    padding-top: 10px;
    width: 450px;
    height: 60px;
}
.deadline{
    margin: auto;
    padding-top: 10px;
    width: 450px;
    height: 100px;
}
.date-box{
    width: 450px;
    height: 45px;
    border: 1.5px solid #D4D4D4;
    border-radius: 8px;
    text-align: center;
    background-color: white;
}
.finishbtn{
    margin-top: 10px;
    margin-left: 25px;
    margin-bottom: 30px;
    width: 450px;
    height: 50px;
    border: 1.5px solid #D4D4D4;
    border-radius: 8px;
    text-align: center;
    font-weight: bold;
    letter-spacing: 1px;
    color: white;
    background-color: #FF9898;
}
.finishbtn:hover{
  background-color: #ff8989;
}
</style>