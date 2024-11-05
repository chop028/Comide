<template>
  <body>
    <div class="option-title">{{ this.groupName }}</div>
  
      <!-- 멤버 섹션 -->
    <div class="member-wrap">
      <div class="info member">
        구성원 ( {{ currentMembers }} / {{ this.groupData.headCnt }} )
      </div>
  
      <div class="member-box">
        <div class="profile-box-wrap">
          <div v-for="(friend, index) in friends" :key="index" :class="`profile_big ${friend.className}`">
            <img :src="friend.image" class="profile-img" />
            <div class="profile-name">{{ friend.userName }}</div>
          </div>
        </div>
      </div>
  
      <div v-if="isEditingMembers" class="add-member-box">
        <input v-model="newMemberName" placeholder="친구 이름" class="add-member-input">
        <button @click="addMember" class="add-member-btn">추가</button>
      </div>
    </div>
  
      <!-- 작업 관리 섹션 -->
    <div class="task-wrap">
      <div class="info-task">
        작업 관리
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
      </div>
    </div>
    
  </body>
</template>
  
  <script>
  import apiClient from '@/axios/axios'; 
  
  export default {
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
        newTasks: [],
        newMemberName: '',
        backupTasks: [],
        backupFriends: [],
        showCheckImageMem: '',
        groupId: null,
        groupName: ''
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
      addMember() {
        if(this.newMemberName.trim() && this.friends.length < this.maxMembers) {
          this.friends.push({ name: this.newMemberName, className: `friend${this.friends.length}`, image: 'profile.svg' });
          this.newMemberName = '';
        }
      }
    },
    mounted() {
      this.groupId = this.$route.query.groupId;
      this.groupName = this.$route.query.groupName;
      this.getGroupData();
      this.getMemberData();
    },
  };
  </script>
  
  <style scoped>
  /* 전체 레이아웃 */
  body {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 170px;
    padding-bottom: 200px;
    font-family: Arial, sans-serif;
    color: #333;
    background-color: #f4f6f9;
  }
  
  /* 옵션 제목 */
  .option-title {
    letter-spacing: 1px;
    margin-top: 20px;
    font-size: 28px;
    font-weight: bold;
    margin-bottom: 20px;
    color: black;
  }
  
  /* 멤버 섹션 */
  .member-wrap {
    width: 80%;
    max-width: 600px;
    margin-bottom: 30px;
    background: #ffffff;
    border-radius: 8px;
    padding: 15px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  .info.member {
    text-align: center;
  }
  
  .member-wrap .info.member {
    font-weight: bold;
    font-size: 18px;
    margin-bottom: 10px;
  }
  .profile-box-wrap {
    text-align: center;
  }
  .profile_big {
    margin-right: 20px;
    display: inline-block;
    flex-direction: column;
    align-items: center;
    font-size: 14px;
    color: #555;
  }
  
  .profile-img {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-bottom: 5px;
  }
  
  .add-member-box {
    display: flex;
    margin-top: 10px;
  }
  
  .add-member-input {
    flex: 1;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
    margin-right: 5px;
  }
  
  /* 작업 관리 섹션 */
  .task-wrap {
    width: 80%;
    max-width: 600px;
    margin-bottom: 30px;
    background: #ffffff;
    border-radius: 8px;
    padding: 15px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  .info-task {
    text-align: center;
    font-weight: bold;
    font-size: 18px;
    margin-bottom: 10px;
    align-items: center;
  }
  
  .modify-complete {
    width: 20px;
    height: 20px;
    margin-left: 10px;
  }
  
  .task-option {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  
  .task-box {
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 6px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    transition: background-color 0.3s ease;
  }
  
  .task-box.clear {
    background-color: #e3f7d9;
  }
  
  .modify-task-box,
  .crtProgress-change {
    width: 100%;
    border: 1px solid #ddd;
    border-radius: 4px;
    padding: 5px;
  }
  
  /* 타임라인 섹션 */
  .timeline-wrap {
    width: 80%;
    max-width: 600px;
    background: #ffffff;
    border-radius: 8px;
    padding: 15px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  .info.timeline {
    text-align: center;
    font-weight: bold;
    font-size: 18px;
    margin-bottom: 5px;
  }
  
  .startline,
  .deadline {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
  }
  
  .date-box {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  </style>
  