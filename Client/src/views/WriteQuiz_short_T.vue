<template>
  <div>
    <div class="quiz_topbar">
      <div class="title">제목</div>
      <input type="text" v-model="title" class="input-title" placeholder="제목을 입력하세요.">
      <select class="combobox" v-model="category">
        <option value="c">C</option>
        <option value="cs">C#</option>
        <option value="java">Java</option>
        <option value="python">Python</option>
        <option value="js">Javacript</option>
      </select>
    </div>

    <div class="posting-wrap">
      <div class="writing-page">
        <button class="content-btn" @click="addInput('content')">주관식 추가</button>
        <div v-for="(input, index) in inputs" :key="index" class="input-section">
        <div class="makeshort_wrap">
          <div class="quiz_lan">문제 {{ index + 1 }}</div>
          <ContentInput v-if="input.type === 'content'" v-model="input.value" class="summernote"/>
          <div class="quiz_lan">정답</div>
          <ContentInput v-if="input.type === 'content'" v-model="input.value" class="summernote"/>
          <button class="remove-btn" @click="removeInput(index)">문제삭제</button>
        </div>
        </div>
      </div>
    </div>
    <div class="footer">
      <button class="wrtbtn" @click="writeQuiz">퀴즈등록</button>
    </div>
  </div>
  
</template>

<script>
import apiClient from '@/axios/axios';
import moment from 'moment';
import ContentInput from '@/views/ContentInput_T.vue';
import { useAuthStore } from '@/store/auth';

export default {
  data() {
    const authStore = useAuthStore();
    return {
      title: '',
      category: "==분류선택==",
      showbtn: false,
      changebtn: false,
      userId: authStore.userInfo?.id || '',
      inputs: [],
      quizId: '',
      quizContentList: [],
    };
  },
  methods: {
    handleModalClose(codeId) {
        apiClient
          .get(`${process.env.VUE_APP_API_URL}/api/code-editor/get-code-detail.do?codeId=${codeId}`)
          .then((response) => {
          if (response.data.success) {
              this.currentCodeId = codeId;
              this.currentCodeValue = response.data.result.content;
              this.inputs.push({ type: "code", codeId: this.currentCodeId, completed: false });
          } else {
              console.error("김민중 머리 부수기");
          }
        })
        .catch(function(error) {
        console.error(error);
        });
    },
    writeQuiz() {
      const createdDateFormatted = moment(new Date()).format('YYYY.MM.DD');
      const modifiedDateFormatted = moment(new Date()).format('YYYY.MM.DD');
      const postData = {
        category: this.category,
        title: this.title,
        content: "",
        //content: this.inputs.filter(input => input.type === 'content').map(input => input.value).join("\n"),
        //code: this.inputs.filter(input => input.type === 'code').map(input => input.value).join("\n"),
        created_date: createdDateFormatted,
        modified_date: modifiedDateFormatted,
        userId: this.userId,
        groupId: this.selGroup,
        viewCnt: 0,
        likes: 0,
      };
      apiClient.post(`${process.env.VUE_APP_API_URL}/api/board/post-board.do`, postData, {
        headers: {
          'Content-Type': 'application/json',
        }
      })
      .then(response => {
        if (response.data.success) {
          this.quizId = response.data.result;
          this.saveQuizContnent();
          this.$router.push('/quiz/main');
        } else {
          console.error("Post failed");
        }
      })
      .catch(error => {
        console.error(error);
      });
    },
    saveQuizContnent(){
      this.inputs.forEach(input => {
        let contentData = {
          category: input.type,
          quizId: this.quizId,
          codeId: null,
          content: null
        }
        if(input.type === "content") {
          contentData.content = input.value;
        } else {
          contentData.codeId = input.codeId;
        }
        this.quizContentList.push(contentData);
      });
      apiClient
        .post(`${process.env.VUE_APP_API_URL}/api/board/save-board-content.do`, this.quizContentList)
        .then((response) => {
        if (response.data.success) {
          console.error("get-board-list failed");
        }
        })
        .catch(function(error) {
        console.error(error);
        });
    },
    saveHashTag(){
      apiClient
        .post(`${process.env.VUE_APP_API_URL}/api/board/save-hashTag.do`, this.hashTagList)
        .then((response) => {
        if (response.data.success) {
          console.error("get-board-list failed");
        }
        })
        .catch(function(error) {
        console.error(error);
        });
    },
    addInput(type) { 
      if(type === "content") {
        this.inputs.push({ type: type, value: '', completed: false });
      } else {
        this.openCodeModal();
      }
      
    },
    completeInput(index) {
      this.inputs[index].completed = true;
    },
    removeInput(index) {
      this.inputs.splice(index, 1);
    },
    answerInput(index) {
      this.inputs.splice(index, 1);
    },
    
  },
  components: {
    ContentInput
  }
}
</script>

<style>
#test {
  position: absolute;
  top: 0;
}
.quiz_topbar {
  padding-left: 23px;
  padding-right: 25px;
  margin: auto;
  margin-top: 230px;
  width: 1450px;
  height: 90px;
  overflow: auto;
  display: flex;
  align-items: center;
}
.input-title {
  margin: auto;
  width: 1000px;
  height: 44px;
  margin-left: 30px;
  margin-right: 0px;
  font-size: 16px;
}
.title {
  width: 100px;
  font-size: 30px;
  font-weight: 700;
  float: left;
  color: #515151;
}
.combobox {
  width: 120px;
  height: 44px;
  text-align: center;
  margin-left: 20px;
}
.posting-box {
  padding-top: 10px;
  font-size: 25px;
  font-weight: bold;
  color: #515151;
}
.answer{
  font-size: 25px;
  font-weight: bold;
  color: #515151;
}
.posting-wrap {
  padding-top: 20px;
  margin: auto;
  width: 1400px;
}
.writing-page {
  width: 1400px;
}
.summernote {
  width: 1400px;
  height: 100px;
  margin: auto;
  margin-top: 20px;
  display: block;
}
.content-btn {
  margin-right: 10px;
  margin-top: 30px;
  width: 120px;
  height: 40px;
  background-color: #FF6969;
  border: none;
  border-radius: 25px;
  color: white;
  font-size: 18px;
  font-weight: bold;
}
.content-btn:hover {
  background-color: #c04949;
}
.footer {
  margin: auto;
  width: 1400px;
  height: 50px;
}
.wrtbtn {
  width: 120px;
  height: 45px;
  margin-left: 610px;
  margin-top: 2px;
}
.wrtbtn:hover {
  background-color: #d2d2d2;
  cursor: pointer;
}
.remove-btn {
  margin-top: 10px;
  margin-bottom: 7px;
  padding: 5px 10px;
  background-color: #ffa8a8;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}
.remove-btn:hover {
  background-color: #eb6060;
}
.answer-btn {
  margin-top: 10px;
  padding: 5px 10px;
  background-color: #ffa8a8;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}
.answer-btn:hover {
  background-color: #eb6060;
}
.quiz_lan{
  width: 500px;
  height: 35px;
  font-size: 30px;
  margin-top: 5px;
}
.makeshort_wrap{
  width: 1400px;
  height: auto;
  border-bottom: 3px solid black;
  margin-top: 20px;
}
</style>