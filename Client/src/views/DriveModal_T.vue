<template>
    <div class="modal-wrap">
      <div class="modal-container">
        <div class="modal-top">이 파일 저장</div>
        <div class="folder-name">
          <span class="naming">이름</span>
          <input
            class="foldername"
            type="text"
            v-model="name"
            placeholder="파일 이름을 입력하세요."
          />
        </div>
        <div class="extension-box">
          <span class="naming">확장자</span>
          <select class="extension-combobox" v-model="extension">
            <option value="java" selected>java</option>
            <option value="txt">txt</option>
          </select>
        </div>
        <div class="location-box">
          <span class="naming">위치</span>
          <select class="location-combobox">
            <option value="파일1" selected>내 드라이브</option>
          </select>
        </div>
        <div class="modal-btn">
          <button class="savebtn" @click="saveAndClose()">저장</button>
          <button class="cancelbtn" @click="closePop()">취소</button>
        </div>
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
        name: '',
        extension: 'java',
      };
    },
    props: {
      postData: Object,
    },
    methods: {
      saveAndClose() {
        this.saveCode();
        this.$emit('close');
      },
      closePop() {
        this.$emit('close');
      },
      saveCode() {
        const saveData = {
          content: this.postData.content,
          createdTime: this.postData.createdTime,
          langId: this.postData.langId,
          userId: this.userId,
          name: this.name,
          extension: this.extension,
        };
        apiClient
          .post(`${process.env.VUE_APP_API_URL}/api/code-editor/save-code.do`, saveData)
          .then((response) => {
            if (!response.data.success) {
              console.error('get-board-list failed');
            }
          })
          .catch(function (error) {
            console.error(error);
          });
      },
    },
  };
  </script>  

<style scoped>
.modal-wrap {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
}

.modal-container {
  position: relative;
  margin-top: 80px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 900px;
  height: 770px;
  background: #fff;
  box-sizing: border-box;
}
.modal-top{
    padding-left: 50px;
    padding-top: 40px;
    width: 900px;
    height: 100px;
    font-weight: bold;
    font-size: 40px;
    letter-spacing: 2px;
    color: #515151;
}
.closeModal{
    float: right;
    width: 40px;
    height: 40px;
    font-size: 20px;
    background-color: transparent;
    border: none;
}
.folder-name {
    padding-left: 50px;
    margin-top: 30px;
    position: relative;
    width: 900px;
    height: 120px;
}
.naming{
    position: absolute;
    font-size: 28px;
    color: #515151;
    font-weight: bold;
}
.foldername {
    margin-top: 55px;
    width: 800px;
    height: 50px;
    font-size: 20px;
    font-weight: bold;
    border: 3px solid #9C9C9C;
}
.extension-box {
    padding-left: 50px;
    margin-top: 30px;
    position: relative;
    width: 900px;
    height: 120px;
}
.extension-combobox {
    margin-top: 55px;
    width: 150px;
    height: 50px;
    font-size: 20px;
    font-weight: bold;
    color: #515151;
    border: 3px solid #9C9C9C;
}
.location-box {
    padding-left: 50px;
    margin-top: 30px;
    position: relative;
    width: 900px;
    height: 160px;
}
.location-combobox {
    margin-top: 55px;
    width: 800px;
    height: 80px;
    font-size: 25px;
    font-weight: bold;
    color: #515151;
    border: 3px solid #9C9C9C;
}
.modal-btn {
    margin-right: 52px;
    margin-top: 60px;
    float: right;
    font-size: 25px;
}
.savebtn{
    width: 120px;
    height: 55px;
    border: none;
    border-radius: 5px;
    background-color: #FF9898;
    color: white;
    font-weight: bold;
}
.cancelbtn{
    margin-left: 30px;
    border-radius: 5px;
    border: 3px solid #9C9C9C;
    width: 120px;
    height: 55px;
    color: #9C9C9C;
    background-color: white;
    font-weight: bold;
}
.savebtn:hover{
    background-color: #f07a74;
}
.cancelbtn:hover{
    background-color: rgb(238, 238, 238);
}
</style>