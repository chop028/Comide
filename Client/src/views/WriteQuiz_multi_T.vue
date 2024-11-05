<template>
  <div>
    <div class="quiz_topbar">
      <div class="title">제목</div>
      <input type="text" v-model="title" class="input-title" placeholder="제목을 입력하세요.">
      <select class="combobox" v-model="category">
        <option value="==분류선택==" selected>==분류선택==</option>
        <option value="c">C</option>
        <option value="cs">C#</option>
        <option value="java">Java</option>
        <option value="python">Python</option>
        <option value="js">Javacript</option>
      </select>
    </div>

   <div class="posting-wrap">
      <div class="writing-page">
        <button class="quizcontent-btn" @click="addInput('quizcontent')">객관식 추가</button>
        <div v-for="(input, index) in inputs" :key="index" class="input-section">
          <div class="quiz_lan">문제 {{ index + 1 }}</div>
          <QuizContentInput v-if="input.type === 'quizcontent'" v-model="input.title" class="summernote" id="summer_title"/>
          <div v-for="answerIndex in 5" :key="answerIndex" id="answers">
            <div class="form-check">
              <input class="form-check-input" type="radio" :name="'flexRadioDefault' + index" :id="'flexRadioDefault' + index + answerIndex" v-model="input.selectedChoice" :value="answerIndex" />
              <label class="form-check-label" :for="'flexRadioDefault' + index + answerIndex">
                {{ answerIndex }}번
              </label>
            </div>
            <QuizContentInput v-if="input.type === 'quizcontent'" v-model="input['answer' + answerIndex]" class="summernote" id="summer_answer"/>
          </div>
          <button class="remove-btn" @click="removeInput(index)">문제삭제</button>
          <div class="makemulti_wrap"></div>
        </div>
      </div>
    </div>
    <div class="footer">
      <button class="wrtbtn" @click="writeQuiz">퀴즈등록</button>
    </div>
  </div>
  
</template>

<script>
import QuizContentInput from '@/views/QuizContentInput_T.vue';
import apiClient from '@/axios/axios';
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
      selectedAnswers: [],
    };
  },
  methods: {
    writeQuiz() {
      const quizPost = {
        quizType: "multi",
        title: this.title,
        totalScore: this.inputs.length,
        category: this.category
      }
      apiClient
        .post(`${process.env.VUE_APP_API_URL}/api/quiz/post-quizPost.do`, quizPost, {
          headers: {
            'Content-Type': 'application/json',
          }
        })
        .then(response => {
          if (response.data.success) {
            const quizPostId = response.data.result;
            this.postQuizData(quizPostId);
            this.$router.push({name : "adminmain"});
          } else {
            console.error("Post failed");
          }
        })
        .catch(error => {
          console.error(error);
        });

      
    },
    postQuizData(quizPostId) {
      const quizMultiList = [];
      this.inputs.forEach(quiz => {
        const quizInfo = {
          answer: quiz.selectedChoice,
          choice1: quiz.answer1,
          choice2: quiz.answer2,
          choice3: quiz.answer3,
          choice4: quiz.answer4,
          choice5: quiz.answer5,
          title: quiz.title,
          quizPostId: quizPostId
        };
        quizMultiList.push(quizInfo);
      });
      apiClient
          .post(`${process.env.VUE_APP_API_URL}/api/quiz/post-multiQuiz-list.do`,quizMultiList)
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
      this.inputs.push({ type: type, title: '', answer1: '', answer2: '', answer3: '', answer4: '', answer5: '', selectedChoice: '' });
      this.selectedAnswers.push(null); // 초기값 설정
    },
    answerInput(index, value) {
      this.selectedAnswers[index] = value; // 직접 배열 값을 업데이트
    },
    removeInput(index) {
      this.inputs.splice(index, 1);
    },
  },
  components: {
    QuizContentInput
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
.quizcontent-btn {
  margin-right: 10px;
  margin-top: 30px;
  width: 160px;
  height: 40px;
  background-color: #FF6969;
  border: none;
  border-radius: 25px;
  color: white;
  font-size: 18px;
  font-weight: bold;
}
.quizcontent-btn:hover {
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
  margin-top: 10px;
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
}
.form-check-input{
  width: 15px;
  height: 15px;
  margin-top: 35px;
  border: 1px solid black;
}
.form-check-label{
  font-size: 30px;
  margin-top: 18px;
}
.makemulti_wrap{
  width: 1400px;
  border-bottom: 3px solid black;
  margin-top: 5px;
}
#answers{
  display: flex;
  align-content : space-between;
  margin-top: 20px;
}
#summer_answer{
  margin-left: 10px;
}
#summer_title{
  min-height: 100px;
}
</style>  