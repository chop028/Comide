<template>
    <RouterView></RouterView>
    <div class="wrapper">
        <div class="quiz_menubar">
            <div class="quiz_topicmenu">퀴즈</div>
            <div class="quiz_hovermenu">전체퀴즈</div>
            <div class="quiz_submenu_lang">- 언어</div>
            <div class="quiz_submenu2">• JAVA</div>
            <div class="quiz_submenu2">• C#</div>
            <div class="quiz_submenu2">• C++</div>
            <div class="quiz_submenu2">• Python</div>
            <div class="myquiz_hovermenu">내 퀴즈</div>
        </div>
        <div class="right_layout">
            <div class="quiz_titlebar">
            <div class="quiz_titletext">C# 코드 문제</div></div>
            <div class="code_question"></div><!-- 코드 문제 div-->
            <div class="question_num">{{ question.number }}</div><div class="question_text">{{ question.text }}</div>
            <input class="answerbox" method="get" type="text">
            <button class="submitbtn"><span style="color:white">제출</span></button>
        </div>
        </div>
    
</template>

<script>
    export default {
        data() {
    return {
      question: {
        number: '1.',
        text: '위 빈칸에 들어갈 코드를 적으시오.',
                },
            };
        },
    }
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
.quiz_titlebar {
  width: 1210px;
  height: 75px;
  border-bottom: 2px solid #888686;
  justify-content: space-between;
}
.quiz_titletext {
  font-size: 45px;
}
.code_question{
    margin-top: 30px;
    width: 1210px;
    height: 230px;
    border: 1px solid #888686;
    border-radius: 10px;
}
.question_num{
    float: left;
    font-size: 35px;
    font-weight: bold;
    margin-top: 150px;
    margin-left: 10px;
}
.question_text{
    float: left;
    font-size: 35px;
    font-weight: bold;
    margin-top: 150px;
}
.answerbox{
    margin-top: 20px;
    width: 1210px;
    height: 50px;
    font-size: 20px;
    box-shadow: inset 0 2px 2px rgb(179, 179, 179);
    border: 1px solid #888686;
    border-radius: 10px;
}
.submitbtn{
    float: right;
    margin-top: 90px;
    margin-right: 60px;
    background-color: #FFA0A0;
    width: 110px;
    height: 47px;
    border: none;
    border-radius: 10px;
    transition: background-color 0.2s;
    font-size: 20px;
    box-shadow: 0 5px 5px #888686;
}
.submitbtn:hover {
  background-color: #fa7a7a;
  cursor: pointer;
}
</style>