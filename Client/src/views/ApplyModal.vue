<template>
  <div class="modal-wrap">
    <div class="modal-container">
      <div class="modal-top">협업 지원</div>
      <div class="inform-container">
        <div class="info field">
          <span class="info-title">분야</span>
          <div class="check-box">
            <div v-for="(field, index) in fields" :key="index" class="form-check">
              <input class="form-check-input" type="checkbox" :value="field" :id="'flexCheckDefault' + index" v-model="selectedFields[index]">
              <label class="form-check-label" :for="'flexCheckDefault' + index">{{ field }}</label>
            </div>
            <input v-if="selectedFields[3]" v-model="otherField" placeholder="내용 입력" class="form-control">
          </div>
        </div>
        <div class="info lang">
          <span class="info-title">사용 언어 및 프레임워크</span>
          <input v-model="language" class="langbox" type="text" placeholder="내용 입력">
        </div>
        <div class="info self-intro">
          <span class="info-title">간단한 자기소개</span>
          <input v-model="selfIntroduction" class="selfbox" type="text" placeholder="내용 입력">
        </div>
      </div>
      <div class="modal-btn">
        <button class="submitbtn" @click="submitForm">제출</button>
        <button class="cancelbtn" @click="closePop">취소</button>
      </div>
    </div>
  </div>
</template>

<script>
import apiClient from '@/axios/axios';
export default {
  props: {
    userId: {
      type: String,
      required: true
    },
    groupId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      selectedFields: [null, null, null, null],
      fields: ['프론트엔드', '백엔드', '디자이너', '기타'],
      otherField: null,
      language: '',
      selfIntroduction: ''
    };
  },
  methods: {
    closePop() {
      this.$emit('close');
    },
    submitForm() {
      this.saveRequest();
      this.$emit('applicationSubmitted');
      this.$emit('close');
    },
    saveRequest() {
      const postData = {
        userId: this.userId, 
        groupId: this.groupId,
        opt1: this.selectedFields[0],
        opt2: this.selectedFields[1],
        opt3: this.selectedFields[2],
        opt4: this.otherField,
        techStack: this.language,
        comment: this.selfIntroduction,
        permit: false,
      };
      
      apiClient
        .post(`${process.env.VUE_APP_API_URL}/api/group/save-group-request.do`, postData)
        .then((response) => {
        if (!response.data.success) {
          alert("이미 그룹 신청을 하였습니다.");
        }
        })
        .catch(function(error) {
        console.error(error);
        });


    }
  }
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

.modal-top {
  padding-left: 50px;
  padding-top: 40px;
  width: 900px;
  height: 100px;
  font-weight: bold;
  font-size: 40px;
  letter-spacing: 2px;
  color: #515151;
}

.inform-container {
  padding-left: 45px;
  width: 900px;
  height: 580px;
  position: relative;
  overflow-y: auto;
}

.info {
  margin-top: 30px;
  padding-top: 20px;
  width: 800px;
  height: 150px;
  border: 2px solid #d4d4d4;
  border-radius: 8px;
  background-color: white;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.3);
}

.info-title {
  padding-left: 15px;
  width: 900px;
  height: 100px;
  font-weight: bold;
  font-size: 30px;
  letter-spacing: 1px;
  color: #515151;
}

.check-box {
  padding-bottom: 10px;
  padding-top: 25px;
  width: 790px;
  height: 70px;
}

.form-check {
  margin-left: 15px;
  margin-right: 20px;
  float: left;
  font-size: 20px;
}

.form-control {
  width: 270px;
  height: 40px;
}

.langbox,
.selfbox {
  margin-top: 30px;
  margin-left: 15px;
  width: 770px;
  border-width: 0 0 2px;
}

.modal-btn {
  margin-right: 52px;
  margin-top: 10px;
  float: right;
  font-size: 25px;
}

.submitbtn {
  width: 120px;
  height: 55px;
  border: none;
  border-radius: 5px;
  background-color: #ff9898;
  color: white;
  font-weight: bold;
}

.cancelbtn {
  margin-left: 30px;
  border-radius: 5px;
  border: 3px solid #9c9c9c;
  width: 120px;
  height: 55px;
  color: #9c9c9c;
  background-color: white;
  font-weight: bold;
}

.submitbtn:hover {
  background-color: #f07a74;
}

.cancelbtn:hover {
  background-color: rgb(238, 238, 238);
}
</style>  