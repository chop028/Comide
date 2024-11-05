<template>
    <div class="projectmodal-overlay">
      <div class="projectmodal-content">
        <p class="projectmodaltitle">새 프로젝트</p>
        <div class="radio-group">
        <label>
          <input type="radio" v-model="selectedLanguage" value="JAVA" /> JAVA
        </label>
        <label>
          <input type="radio" v-model="selectedLanguage" value="HTML" /> HTML
        </label>
      </div>
        <div class="project-detail">
          <input class="project-name" v-model="projectName" placeholder="프로젝트 명" />
        </div>
        <div class="projectmodal-actions">
          <button class="project-btn" @click="createProject">생성</button>
          <button class="project-btn" @click="close">취소</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { useAuthStore } from '@/store/auth';
  export default {
    props: {
      isOpen: {
        type: Boolean,
        required: true,
      },
      isGroup: {
        type: Boolean,
        required: true
      },
    },
    data() {
      return {
        projectName: "",
        selectedLanguage: "JAVA",
        userId: null,
      };
    },
    methods: {
    createProject() {
      if(this.projectName == '') {
          alert('프로젝트명을 입력해주세요.');
          return
        }
      if(this.isGroup == false) {
        this.userId = useAuthStore().userInfo.id;
      }
      const project = {
        name: this.projectName,
        language: this.selectedLanguage,
        ownerId: this.userId,
      };
    this.$emit('create-project', project);
    this.close();
  },
  close() {
    this.$emit("close-modal");
  }
}
  };
  </script>
  
  <style scoped>
  .projectmodal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .projectmodal-content {
    background-color: white;
    padding: 20px;
    border-radius: 5px;
    width: 450px;
    height: 270px;
    text-align: center;
  }
  .projectmodaltitle {
    font-size: 30px;
    font-weight: bo;
  }
  .radio-group {
    display: flex;
    gap: 10px;
    justify-content: center;
    margin-bottom: 25px;
}
  .project-detail {
    display: flex;
    flex-direction: column;
}
  .project-name,
  .project-num {
    width: 50%;
    height: 40px;
    margin: auto;
    margin-bottom: 15px;
    text-align: center;
  }
  .projectmodal-actions {
    margin-top: 20px;
    display: flex;
    justify-content: space-around;
  }
  .project-btn {
    border: none;
    border-radius: 5px;
    width: 70px;
    height: 40px;
    background-color: #FAE9E9;
  }
  .project-btn:hover {
    background-color: #ffdcdc;
  }
  </style>  