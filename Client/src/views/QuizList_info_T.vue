<template>
    <div class="quizwrapper">
        <div class="quizmenubar">
            <div class="quiz_topicmenu">퀴즈</div>
            <div class="quiz_hovermenu">전체퀴즈</div>
            <div class="quiz_submenu_lang">- 언어</div>
            <div class="quiz_submenu2">• JAVA</div>
            <div class="quiz_submenu2">• C#</div>
            <div class="quiz_submenu2">• C++</div>
            <div class="quiz_submenu2">• Python</div>
            <div class="myquiz_hovermenu" @click="movemyquiz">내 퀴즈</div>
        </div>
        <div class="right_layouts">
            <div class="quizbar">
                <div class="quizall">정보처리</div>
            </div>
            <div class="select_bar">
                <input class="search_bar" method="get" type="text" placeholder="검색어를 입력하세요">
                <button class="srcbtn"><span style="color:white; font-size:20px; font-weight: bold; text-align: center;">검색</span></button>
            </div>
            <table class="quiztable">
                <thead>
                    <tr>
                        <th class="top-no">No</th>
                        <th class="top-wrt">글쓴이</th>
                        <th class="top-title">Title</th>
                        <th class="top-time">Time</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(post, index) in post" :key="index">
                        <th class="num1">{{ post.num +1 }}</th>
                        <td class="num2-1">{{ post.category }}</td>
                        <td class="num2-2">{{ post.title }}</td>
                        <td class="num2">{{ post.created_date }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    
</template>

<script>
    export default {
        methods: {
          movemyquiz(){
            this.$router.push({ name: 'quizresult' });
          },
        },
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
    margin-top: 10px;
    font-weight: bold;
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
.quiz_submenu_lang:hover,
.quiz_submenu_info:hover{
    color: black;
    font-weight: bold;
    cursor: pointer;
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
.quizbar {
  width: 1210px;
  height: 75px;
  border-bottom: 2px solid #888686;
  justify-content: space-between;
}
.quizall {
  font-size: 45px;
}
.select_bar{
    display: flex;
    justify-content: center;
    width: 1210px;
    height: 90px;
    font-size: 25px;
    padding-top: 23px;
}
.search_bar{
    text-align: center;
    font-size: 15px;
    border: 1px solid #888686;
    border-radius: 10px;
    width: 500px;
    height: 40px;
}
.srcbtn{
  width: 90px;
  height: 42px;
  background-color: #FFBEBA;
  border: 1px solid #888686;
  margin-left: 20px;
  border-radius: 10px;
  padding-bottom: 7px;
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
.top-wrt {
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
  border-right: 1px solid #888686;
  padding: 10px;
  vertical-align : middle;
}
.num2{
  vertical-align : middle;
}
.num2-1{
  border-right: 1px solid #888686;
  padding: 10px;
  vertical-align : middle;
}
.num2-2{
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