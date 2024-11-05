<template>
    <div class="make-page">
      <div class="header-bar">
        그룹 생성
      </div>
      <div class="making-body">
        <div class="make_name">
          <span style="font-size: 22px; font-weight: bold; color: #515151; padding-left: 18px;">그룹 이름</span><br>
          <input class="groupname" type="text" v-model="groupName">
        </div>
        <div class="Description">
          <span style="font-size: 22px; font-weight: bold; color: #515151; padding-left: 18px;">그룹 설명</span><br>
          <input class="groupDescription" type="text" v-model="groupDescription">
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
            <span style="font-size: 22px; font-weight: bold; color: #515151; text-align: center;">기술 스택</span><br>
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
    </div>
  </template>
  
  <script>
import apiClient from '@/axios/axios';
  import { useAuthStore } from '@/store/auth';

  export default {
    data() {
      const authStore = useAuthStore();
      return {
        userId: authStore.userInfo?.id || '',
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
        this.$emit('changeComponent', 'GroupList');
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
      saveInput() {
        const groupData = {
          name: this.groupName,
          introComment: this.groupDescription,
          headCnt: this.count,
          startDate: this.startline,
          endDate: this.deadline,
          userId: this.userId,
        };
        apiClient
          .post(`${process.env.VUE_APP_API_URL}/api/group/create-group.do`, groupData)
          .then((response) => {
            if (response.data.success) {
              this.saveGroupTask(response.data.result);
              this.saveGroupLang(response.data.result);
              alert('그룹이 생성되었습니다.');
              this.$router.push({ name: 'main', params: { category: "main" } });
            } else {
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
    }
  };
  </script>
  
  <style scoped>
  .make-page{
    margin: auto;
    margin-top: 190px;
    width: 1000px;
    border-radius: 25px;
    margin-bottom: 60px;
  }
  .header-bar{
    text-align: center;
    width: 1000px;
    height: 80px;
    padding-top: 25px;
    padding-left: 40px;
    border-radius: 25px 25px 0 0;
    letter-spacing: 1px;
    font-size: 35px;
    font-weight: bold;
    color: #515151;
  }
  .making-body{
      position: relative;
      width: 1000px;
      height: 648px;
  }
  .make_name{
    text-align: center;
      margin: auto;
      margin-top: 20px;
      padding-top: 10px;
      width: 1000px;
      height: 100px;
  }
  .groupname{
      margin-top: 8px;
      margin-left: 15px;
      width: 800px;
      height: 35px;
      border: 2px solid #D4D4D4;
      border-radius: 8px;
  }
  .Description{
    text-align: center;
      margin: auto;
      margin-top: 10px;
      padding-top: 10px;
      width: 1000px;
      height: 180px;
  }
  .groupDescription{
      margin-top: 8px;
      margin-left: 15px;
      width: 800px;
      height: 150px;
      border: 2px solid #D4D4D4;
      border-radius: 8px;
  }
  .group_num{
    text-align: center;
    margin: auto;
    margin-top: 50px;
    padding-top: 10px;
    width: 1000px;
    height: 100px;
  }
  .num_setting{
    margin: auto;
    margin-top: 20px;
    width: 800px;
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
      margin-left: 370px;
      font-weight: bold;
      color: #515151;
      float: left;
  }
  .task_management{
    text-align: center;
      margin: auto;
      margin-top: 20px;
      margin-top: 10px;
      padding-top: 10px;
      width: 1000px;
  }
  .task1{
    margin: auto;
      margin-top: 8px;
      width: 752px;
      height: 45px;
      color: #515151;
      border: 2px solid #D4D4D4;
      border-radius: 8px;
      background-color: white;
  }
  .task2{
    margin: auto;
      margin-top: 8px;
      margin-left: 8px;
      width: 40px;
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
      width: 800px;
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
    text-align: center;
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
}
  .plus_stack{
      margin-top: 8px;
      margin-left: 15px;
      width: 100px;
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
    text-align: center;
      margin: auto;
      margin-top: 20px;
      padding-top: 10px;
      width: 800px;
      height: 100px;
  }
  .deadline{
    text-align: center;
      margin: auto;
      margin-top: 10px;
      padding-top: 10px;
      width: 800px;
      height: 100px;
  }
  .date-box{
      margin-top: 8px;
      margin-left: 15px;
      width: 800px;
      height: 45px;
      border: 2px solid #D4D4D4;
      border-radius: 8px;
      text-align: center;
      background-color: white;
  }
  .finishbtn{
    margin: auto;
      margin-top: 20px;
      margin-bottom: 30px;
      width: 1000px;
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
  .navigationbar {
      width: 500px;
      height: 52px;
      border-radius: 0 0 25px 25px;
      background-color: #F0F0F0;
  }
  </style>