<template>
  <div class="make-page">
    <div class="header-bar">
      그룹 생성
      <button class="zoom-inbtn" @click="zoomIn"><img src="@/assets/zoom_in.svg" width="30px" height="30px"></button>
    </div>
    <div class="making-body">
      <div class="make_name">
        <span style="font-size: 22px; font-weight: bold; color: #515151; padding-left: 18px;">그룹 이름</span><br>
        <input class="groupname" type="text" v-model="groupName">
      </div>
      <div class="Description">
        <span style="font-size: 22px; font-weight: bold; color: #515151; padding-left: 18px;">그룹 설명</span><br>
        <textarea class="groupDescription" type="text" v-model="groupDescription"></textarea>
      </div>
      <div class="group_num">
        <span style="font-size: 22px; font-weight: bold; color: #515151; padding-left: 18px;">그룹 인원</span><br>
        <div class="num_setting">
          <button class="btn1" @click="decreaseCount">-</button>
          <div class='num_result'>{{ count }}명</div>
          <button class="btn2" @click="increaseCount">+</button>
        </div>
      </div>
      <div class="task_management">
        <span style="font-size: 22px; font-weight: bold; color: #515151; padding-left: 18px;">작업 관리</span><br>
        <div v-for="(task, index) in tasks" :key="index" class="task-row">
          <input class="task1" type="text" v-model="tasks[index][0]">
          <input class="task2" type="text" v-model="tasks[index][1]">
        </div>
        <button class="plus_task" @click="addTask">+</button>
      </div>
      <div class="stack-box">
        <span style="font-size: 22px; font-weight: bold; color: #515151; padding-left: 18px;">기술 스택</span><br>
        <div v-for="(stack, index) in stacks" :key="index">
          <select v-model="stacks[index]" class="stack">
            <option v-for="option in availableStacks" :key="option.value" :value="option.value">{{ option.label }}</option>
          </select>
        </div>
        <button class="plus_stack" @click="addStack">+</button>
      </div>
      <div class="startline">
        <span style="font-size: 22px; font-weight: bold; color: #515151; padding-left: 18px;">시작일</span><br>
        <input class="date-box" type="date" v-model="startline">
      </div>
      <div class="deadline">
        <span style="font-size: 22px; font-weight: bold; color: #515151; padding-left: 18px;">마감일</span><br>
        <input class="date-box" type="date" v-model="deadline">
      </div>
      <button class="finishbtn" @click="saveInput">생성 완료</button>
    </div>
    <div class="navigationbar">
      <div class="nbox" @click="movefriend"><img src="../../assets/people-fill.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="movechat"><img src="../../assets/chat.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="movegroup"><img src="../../assets/wechat.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="movedrive"><img src="../../assets/storage.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="moveoption"><img src="../../assets/option.svg" width="30px" height="30px"></div>
    </div>
  </div>
</template>

<script>
import apiClient from '@/axios/axios';
import { useAuthStore } from '@/store/auth';

export default {
  data() {
    return {
      count: 0,
      tasks: [],
      stacks: [],
      availableStacks: [
        { value: 'c', label: 'C' },
        { value: 'cpp', label: 'C++' },
        { value: 'cs', label: 'C#' },
        { value: 'java', label: 'JAVA' },
        { value: 'spring', label: 'Spring' },
        { value: 'html', label: 'HTML' },
        { value: 'css', label: 'CSS' },
        { value: 'js', label: 'Javacript'},
      ],
      groupName: '',
      groupDescription: '',
      startline: '',
      deadline: ''
    };
  },
  methods: {
    movefriend() {
      this.$emit('changeComponent', 'FriendList');
    },
    movechat() {
      this.$emit('changeComponent', 'ChatList');
    },
    movegroup() {
      this.$emit('changeComponent', 'MyGroupPage');
    },
    moveoption() {
        this.$emit('changeComponent', 'OptionPage');
    },
    movedrive() {
        this.$emit('changeComponent', 'DrivePage');
    },
    increaseCount() {
      if (this.count < 99) {
        this.count++;
      }
    },
    decreaseCount() {
      if (this.count > 0) {
        this.count--;
      }
    },
    addTask() {
      this.tasks.push(['', 1]);
    },
    addStack() {
      this.stacks.push('');
    },
    zoomIn() {
      this.$router.push({ name: 'makegroup' });
      this.$emit('changeComponent', 'MyGroupPage');
    },
    saveGroupTask(gId) {
      const groupTaskList = [];
      this.tasks.forEach(task => {
        const taskName = task[0];
        const overall = task[1];

        // groupTask 객체 생성
        const groupTask = {
          groupId: gId,
          taskName: taskName,
          overall: overall,
          crtProcess: 0
        };
        groupTaskList.push(groupTask);
      });
      apiClient
        .post(`${process.env.VUE_APP_API_URL}/api/group/save-group-task.do`, groupTaskList)
        .then((response) => {
          if (!response.data.success) {
            console.error("get-board-list failed");
          }
        })
        .catch(function (error) {
          console.error(error);
        });
    },
    saveGroupLang(gId){
      const groupLangList = [];
      // eslint-disable-next-line
      this.stacks.forEach(stack=>{
        const groupLang = {
          langCode: stack,
          groupId: gId,
        };
        groupLangList.push(groupLang);
      });
      apiClient
        .post(`${process.env.VUE_APP_API_URL}/api/group/save-group-lang.do`, groupLangList)
        .then((response) => {
          if (!response.data.success) {
            console.error("get-board-list failed");
          }
        })
        .catch(function (error) {
          console.error(error);
        });
    },
    validateGroupInput() {
      if (!this.groupName || !this.count || !this.startline || !this.deadline) {
        let missingFields = [];
        if (!this.groupName) missingFields.push('그룹 이름');
        if (!this.count) missingFields.push('그룹 인원');
        if (!this.startline) missingFields.push('시작일');
        if (!this.deadline) missingFields.push('종료일');
        alert(`${missingFields.join(', ')}을 설정해 주세요.`);
        return false;
      }
      return true;
    },
    saveInput() {
      if (!this.validateGroupInput()) {
        return;
      }
      const authStore = useAuthStore();

      const groupData = {
        name: this.groupName,
        introComment: this.groupDescription,
        headCnt: this.count,
        startDate: this.startline,
        endDate: this.deadline,
        userId: authStore.userInfo?.id,
      };
      apiClient
        .post(`${process.env.VUE_APP_API_URL}/api/group/create-group.do`, groupData)
        .then((response) => {
          if (response.data.success) {
            this.saveGroupTask(response.data.result);
            this.saveGroupLang(response.data.result);
            alert('그룹이 생성되었습니다.');
            this.$emit('changeComponent', 'MyGroupPage');
          } else {
            console.error("get-board-list failed");
          }
        })
        .catch(function (error) {
          console.error(error);
        });
    },
  }
};
</script>

<style scoped>
.make-page{
    width: 500px;
    height: 780px;
    background-color: white;
    border-radius: 25px;
    margin-bottom: 60px;
}
.header-bar{
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
.zoom-inbtn {
  margin-right: 20px;
  float: right;
  width: 50px;
  height: 50px;
  border: none;
  filter: invert(27%) sepia(4%) saturate(231%) hue-rotate(320deg) brightness(106%) contrast(86%);
  background-color: transparent;
}
.making-body{
    position: relative;
    overflow-y: auto;
    width: 500px;
    height: 648px;
}
.make_name{
    margin: auto;
    margin-top: 20px;
    padding-top: 10px;
    width: 450px;
    height: 100px;
}
.groupname{
    margin-top: 8px;
    margin-left: 15px;
    width: 430px;
    height: 35px;
    border: 2px solid #D4D4D4;
    border-radius: 8px;
}
.Description{
    margin: auto;
    margin-top: 10px;
    padding-top: 10px;
    width: 450px;
    height: 180px;
}
.groupDescription {
    margin-top: 8px;
    margin-left: 15px;
    width: 430px;
    height: 110px;
    border: 2px solid #D4D4D4;
    border-radius: 8px;
    padding: 10px;
    resize: none;
    box-sizing: border-box;
}

.group_num{
    margin: auto;
    margin-top: 10px;
    padding-top: 10px;
    width: 450px;
    height: 100px;
}
.num_setting{
    margin-top: 8px;
    margin-left: 15px;
    width: 430px;
    height: 40px;
    border: 2px solid #D4D4D4;
    border-radius: 8px;
    background-color: white;
}
.btn1{
    color: #515151;
    margin-top: 5px;
    margin-left: 5px;
    float: left;
    border: none;
    background-color: white;
}
.btn2{
    color: #515151;
    margin-top: 5px;
    margin-right: 5px;
    float: right;
    border: none;
    background-color: white;
}
.num_result{
    margin-top: 7px;
    margin-left: 175px;
    font-weight: bold;
    color: #515151;
    float: left;
}
.task_management{
    margin: auto;
    margin-top: 10px;
    padding-top: 10px;
    width: 450px;
}
.task1{
    margin-top: 8px;
    margin-left: 15px;
    width: 377px;
    height: 45px;
    color: #515151;
    border: 2px solid #D4D4D4;
    border-radius: 8px;
    background-color: white;
    float: left;
}
.task2{
    margin-top: 8px;
    margin-left: 8px;
    width: 45px;
    height: 45px;
    color: #515151;
    border: 2px solid #D4D4D4;
    border-radius: 8px;
    background-color: white;
    display: inline-block;
    text-align: center;
  }
.plus_task{
    margin-top: 8px;
    margin-left: 15px;
    width: 430px;
    height: 45px;
    font-weight: bold;
    font-size: 18px;
    color: #515151;
    border: 2px solid #D4D4D4;
    border-radius: 8px;
    background-color: white;
}
.plus_task:hover{
  background-color: #e9e9e9;
}
.stack-box{
    position: relative;
    overflow-x: auto;
    margin: auto;
    margin-top: 20px;
    padding-top: 10px;
    width: 450px;
}
.stack{
    margin-top: 8px;
    margin-left: 15px;
    width: 95px;
    height: 50px;
    font-weight: bold;
    font-size: 18px;
    color: #515151;
    border: 2px solid #D4D4D4;
    border-radius: 8px;
    background-color: white;
    display: block;
    float: left;
    appearance:none;
    text-align: center;
}
.plus_stack{
    margin-top: 8px;
    margin-left: 15px;
    width: 95px;
    height: 50px;
    font-weight: bold;
    font-size: 18px;
    color: #515151;
    border: 2px solid #D4D4D4;
    border-radius: 8px;
    background-color: white;
}
.plus_stack:hover{
  background-color: #e9e9e9;
}
.startline{
    margin: auto;
    margin-top: 20px;
    padding-top: 10px;
    width: 450px;
    height: 100px;
}
.deadline{
    margin: auto;
    margin-top: 10px;
    padding-top: 10px;
    width: 450px;
    height: 100px;
}
.date-box{
    margin-top: 8px;
    margin-left: 15px;
    width: 430px;
    height: 45px;
    border: 2px solid #D4D4D4;
    border-radius: 8px;
    text-align: center;
    background-color: white;
}
.finishbtn{
    margin-top: 20px;
    margin-left: 30px;
    margin-bottom: 30px;
    width: 430px;
    height: 50px;
    border: 2px solid #D4D4D4;
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