<template>
  <div class="quizwrapper">
      <div class="quizmenubar">
          <div class="quiz_topicmenu">퀴즈</div>
          <div class="quiz_hovermenu">전체퀴즈</div>
          <div class="quiz_submenu_lang">- 언어</div>
          <div class="quiz_submenu2">• Java</div>
          <div class="quiz_submenu2">• Python</div>
          <div class="quiz_submenu2">• C#</div>
          <div class="quiz_submenu2">• Javascript</div>
          <div class="myquiz_hovermenu" @click="movemyquiz">내 퀴즈</div>
      </div>
      <div class="right_layouts">
          <div class="quizbar">
              <div class="quizall">전체퀴즈</div>
          </div>
          <div class="selectbar">
              <span class="select_lang" style="font-weight: bold;">언어선택</span>
              <select class="quiz_combobox">
                  <option value="전체" selected>전체</option>
                  <option value="JAVA">JAVA</option>
                  <option value="C#">C#</option>
                  <option value="C++">C++</option>
                  <option value="Python">Python</option>
              </select>
              <span class="select_cate" style="font-weight: bold;">문제유형</span>
              <select class="quiz_combobox">
                  <option value="all" selected>전체</option>
                  <option value="객관식">객관식</option>
                  <option value="주관식">주관식</option>
              </select>
              <button class="srcbtn"><span style="color:white; font-size:20px; font-weight: bold; text-align: center;">검색</span></button>
          </div>
          <table class="quiztable">
              <thead>
                  <tr>
                      <th class="top-no">No</th>
                      <th class="top-lang">Lang</th>
                      <th class="top-title">Title</th>
                      <th class="top-time">Type</th>
                  </tr>
              </thead>
              <tbody>
                  <tr v-for="(post, index) in post" :key="index" @click="getQuizDetail(post.id)">
                      <th class="num1">{{ post.num +1 }}</th>
                      <td class="num2-1">{{ post.category }}</td>
                      <td class="num2-2">{{ post.title }}</td>
                      <td class="num2">{{ post.quizType === 'multi' ? '객관식' : '주관식' }}</td>
                  </tr>
              </tbody>
          </table>
      </div>
  </div>
  
</template>

<script>
import apiClient from '@/axios/axios';

export default {
  data() {
    return {
      post: [],
    };
  },
  props: {
    msg: String
  },
  created(){
    this.getQuizList();
  },
  methods: {
    movemyquiz(){
      this.$router.push({ name: 'mypagequiz' });
    },
    getQuizList() {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/quiz/get-quiz-list.do`)
        .then((response) => {
          if (response.data.success) {
          const sortedPosts = response.data.result.map((post, index) => ({
            ...post,
            num: index
          }));

          this.post = sortedPosts.sort((a, b) => b.num - a.num);
        } else {
          console.error("get-board-list failed");
        }
      })
      .catch(function(error) {
        console.error(error);
      });
    },
    getQuizDetail(quizid) {
      this.$router.push({name: 'startquiz', params: {quizPostId: quizid}});
    },
  }
}
</script>

<style scoped>
.quizwrapper{
	width: 1810px;
    height: 3000px;
	margin: 0 auto;
}
.quizmenubar{
    float: left;
    margin-left: 100px;
    margin-top: 230px;
    width: 200px;
    height: 600px;
}
.right_layouts{
    float: left;
    margin-top: 230px;
    margin-left: 150px;
    width: 1210px;
    height: 1100px;
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
.quiz_submenu_info:hover,
.quiz_submenu_lang:hover{
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
.quiz_submenu_lang {
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
.quiz_submenu2 {
    width: 200px;
    font-size: 22px;
    padding-left: 50px;
    line-height: 50px;
    color: #888686;
    transition: color 0.2s, font-weight 0.2s;
}
.quizbar {
  width: 1210px;
  height: 75px;
  border-bottom: 2px solid #888686;
  justify-content: space-between;
}
.quizall {
  font-size: 45px;
}
.selectbar{
    width: 1210px;
    height: 90px;
    font-size: 25px;
    padding-top: 23px;
}
.select_lang{
    margin-left: 3px;
}
.select_cate{
    margin-left: 30px;
}
.quiz_combobox{
    font-size: 20px;
    text-align: center;
    margin-left: 20px;
    margin-right: 20px;
    border-radius: 10px;
    width: 100px;
    height: 40px;
}
.srcbtn{
  width: 90px;
  height: 45px;
  background-color: #FFBEBA;
  border: 1px solid #888686;
  margin-left: 20px;
  border-radius: 10px;
  padding-bottom: 5px;
}
.srcbtn:hover {
  background-color: #fda1a1;
  cursor: pointer;
}
.quiztable {
  width: 1210px;
  text-align: center;
  margin-top: 5px;
  border-top: 1.5px solid #888686;
  border-bottom: 1.5px solid #888686;
  font-size: 20px;
  vertical-align : middle;
} 
.top-no {
  background-color: #FAE9E9;
  border-top: 1.5px solid #888686;
  border-bottom: 1.5px solid #888686;
  border-right: 1px solid #888686;
  padding: 10px;
}
.top-lang {
  background-color: #FAE9E9;
  border-top: 1.5px solid #888686;
  border-bottom: 1.5px solid #888686;
  border-right: 1px solid #888686;
  padding: 10px;
}
.top-title {
  background-color: #FAE9E9;
  border-top: 1.5px solid #888686;
  border-bottom: 1.5px solid #888686;
  border-right: 1px solid #888686;
  padding: 10px;
}
.top-time {
  background-color: #FAE9E9;
  border-top: 1.5px solid #888686;
  border-bottom: 1.5px solid #888686;
  padding: 10px;
}
.num1{
  border-bottom: 1px solid #888686;
  border-right: 1px solid #888686;
  padding: 10px;
  vertical-align : middle;
}
.num2{
  border-bottom: 1px solid #888686;
  vertical-align : middle;
}
.num2-1{
  border-bottom: 1px solid #888686;
  border-right: 1px solid #888686;
  padding: 10px;
  vertical-align : middle;
}
.num2-2{
  border-bottom: 1px solid #888686;
  border-right: 1px solid #888686;
  padding: 10px;
  vertical-align : middle;
}

.num2:hover {
  cursor: pointer;
}

.num2-1:hover {
  cursor: pointer;
}

.num2-2:hover {
  text-decoration: underline;
  cursor: pointer;
}
</style>