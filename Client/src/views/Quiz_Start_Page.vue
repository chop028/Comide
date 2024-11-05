<template>
    <RouterView></RouterView>
    <div class="wrapper">
      <div class="quiz_menubar">
        <div class="quiz_topicmenu">퀴즈</div>
        <div class="quiz_hovermenu">전체퀴즈</div>
        <div class="quiz_submenu">- 언어</div>
        <div class="quiz_submenu2">• Java</div>
        <div class="quiz_submenu2">• Python</div>
        <div class="quiz_submenu2">• C#</div>
        <div class="quiz_submenu2">• Javascript</div>
        <div class="quiz_hovermenu">내 퀴즈</div>
      </div>
      <div class="right_layout">
        <div class="start-box">
          <div class="quizTitle">{{ quiz.title }}</div>
          <div class="quizCategory">퀴즈 유형 : {{ quiz.quizType === 'multi' ? '객관식' : '주관식' }}</div>
          <div class="quizNum">문항수 : {{ quiz.totalScore }}</div>
          <button class="quizStartbtn" @click="startQuiz">퀴즈 시작</button>
        </div>
      </div>
    </div>
  </template>  

<script>
import apiClient from '@/axios/axios';

export default {
  data() {
    return {
      quizPostId: '',
      quiz: []
    };
  },
  methods: {
    getQuizPostData() {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/quiz/get-quiz-post.do?postId=${this.quizPostId}`)
        .then((response) => {
        if (response.data.success) {
            this.quiz = response.data.result;
        }
        })
        .catch(function(error) {
          console.error(error);
        });
    },
    startQuiz() {
      this.$router.push({name: 'quiz_multi', params: {quizPostId: this.quizPostId}});
    }
  },
  mounted() {
    const quizPostId = this.$route.params.quizPostId;
    this.quizPostId = quizPostId;
    this.getQuizPostData();
  }
};
</script>

<style scoped>
.wrapper{
	width: 1810px;
    height: 3000px;
	margin: 0 auto;
}
.quiz_menubar{
    float: left;
    margin-left: 100px;
    margin-top: 230px;
    width: 200px;
    height: 600px;
}
.right_layout{
    float: left;
    margin-top: 230px;
    margin-left: 150px;
    width: 1210px;
    height: 1000px;
}
.quiz_topicmenu {
    width: 200px;
    height: 80px;
    font-size: 45px;
    border-bottom: 2px solid #888686;
}

.quiz_hovermenu {
    width: 200px;
    text-align: left;
    font-size: 25px;
    font-weight: bold;
    margin-top: 10px;
    transition: color 0.2s, font-weight 0.2s;
}
.myquiz_hovermenu {
    width: 200px;
    text-align: left;
    font-size: 25px;
    margin-top: 15px;
    color: #888686;
    transition: color 0.2s, font-weight 0.2s;
}

.quiz_hovermenu:hover,
.quiz_submenu:hover,
.quiz_submenu2:hover,
.myquiz_hovermenu:hover,
.quiz_submenu_info:hover{
    color: black;
    font-weight: bold;
    cursor: pointer;
}

.quiz_submenu {
    width: 200px;
    font-size: 25px;
    padding-top: 10px;
    padding-left: 30px;
    line-height: 50px;
    color: #888686;
    transition: color 0.2s, font-weight 0.2s;
}
.quiz_submenu_info {
    width: 200px;
    font-size: 25px;
    padding-top: 10px;
    padding-left: 30px;
    line-height: 50px;
    color: #888686;
    transition: color 0.2s, font-weight 0.2s;
}
.quiz_submenu_lang {
    width: 200px;
    font-size: 25px;
    font-weight: bold;
    padding-top: 10px;
    padding-left: 30px;
    line-height: 50px;
    transition: color 0.2s, font-weight 0.2s;
}
.quiz_submenu2 {
    width: 200px;
    font-size: 22px;
    padding-left: 50px;
    line-height: 50px;
    color: #888686;
    transition: color 0.2s, font-weight 0.2s;
}
.start-box {
    padding-top: 100px;
    width: 1210px;
    height: 650px;
    background-color: #fef8f8;
    box-shadow: 0 3px 5px rgba(0, 0, 0, 0.3);
}
.quizTitle {
    text-align: center;
    margin: auto;
    font-size: 50px;
    font-weight: bold;
    color: #515151;
    letter-spacing: 1px;
}
.quizCategory {
    text-align: center;
    margin-top: 100px;
    font-size: 25px;
    color: #6b6b6b;
}
.quizNum {
    text-align: center;
    margin-top: 20px;
    font-size: 25px;
    color: #6b6b6b;
}
.quizStartbtn {
    margin-top: 100px;
    margin-left: 530px;
    width: 150px;
    height: 60px;
    border: none;
    border-radius: 8px;
    color: white;
    font-weight: bold;
    font-size: 25px;
    background-color: #FF6969;
    box-shadow: 0 3px 5px rgba(0, 0, 0, 0.3);
}
.quizStartbtn:hover {
    background-color: #e94b4b;
}
</style>