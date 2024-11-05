<template>
    <RouterView></RouterView>
    <div class="wrapper">
        <div class="quiz_menubar">
            <div class="quiz_topicmenu">퀴즈</div>
            <div class="quiz_hovermenu">전체퀴즈</div>
            <div class="quiz_submenu_lang">- 언어</div>
            <div class="quiz_submenu2">• Java</div>
            <div class="quiz_submenu2">• Python</div>
            <div class="quiz_submenu2">• C#</div>
            <div class="quiz_submenu2">• Javascript</div>
            <div class="myquiz_hovermenu" @click="movemyquiz">내 퀴즈</div>
        </div>
        <div class="right_layout">
            <div class="resultbox">
                <div class="listbox">
                    <span class="list_id">언어</span><br>
                    <span class="list_id">문제유형</span><br>
                    <span class="list_id">날짜</span><br>
                    <span class="list_id">문항수</span><br>
                    <span class="list_id">소요시간</span><br>
                    <span class="list_id">점수</span><br>
                    <span class="list_id">평가진단</span>
                </div>
                <div class="result_databox">
                    <div class="lang_data" style="color: #6C6C6C;">{{ language }}</div>
                    <div class="category_data" style="color: #6C6C6C;">{{ category }}</div>
                    <div class="date_data" style="color: #6C6C6C;">{{ date }}</div>
                    <div class="quiznum_data" style="color: #6C6C6C;">{{ correctAnswers }} / {{ totalQuestions }}</div>
                    <div class="time_data" style="color: #6C6C6C;">{{ time }}</div>
                    <div class="score_data" style="color: #6C6C6C;">{{ score }}점</div>
                    <div class="diagnosis_data" style="color: #6C6C6C;">{{ diagnosis }}</div>
                </div>
                <div class="graphbox">
                    <AnswerChart :wrongAnswers="wrongAnswers"></AnswerChart>
                </div>
            </div>
            <div class="btnbox">
                <button class="quiz_myquiz_btn" @click="movemyquiz"><span style="color:white">내 퀴즈 통계로 이동</span></button>
                <!--<button class="quiz_answer_btn"><span style="color:white">정답 확인하기</span></button>-->
            </div>
        </div>
    </div>
</template>

<script>
import apiClient from '@/axios/axios';
import AnswerChart from '@/views/AnswerChart.vue';

export default {
    components: {
        AnswerChart
    },
    data() {
        return {
            language: 'C',
            category: '객관식',
            date: '2002년 2월 19일',
            correctAnswers: 4,
            totalQuestions: 5,
            time: '02 : 02 : 28',
            score: 80,
            diagnosis: '전체적인 문법에 대한 이해도는 좋으나, for 반복문에 대한 이해도가 낮아보임',
            wrongAnswers: []
        }
    },
    methods: {
        fetchResultData(resultId) {
            apiClient
                .get(`${process.env.VUE_APP_API_URL}/api/quiz/get-quiz-result.do?postId=${resultId}`)
                .then(response => {
                    if (response.data.success) {
                        const postData = response.data.result.quizPost;
                        const resultData = response.data.result.quizResult;

                        this.language = postData.category;
                        this.category = postData.quizType === 'multi' ? '객관식' : '주관식';
                        this.date = resultData.excuteDate;
                        this.time = resultData.durationTime;
                        this.correctAnswers = resultData.correctAnswer;
                        this.totalQuestions = postData.totalScore;
                        this.diagnosis = resultData.comment;
                        this.score = Math.round(100 * this.correctAnswers / this.totalQuestions);

                        // 오답률 계산 (undefined 체크 추가)
                        if (postData.questions) {
                            this.wrongAnswers = postData.questions.map(question => ({
                                rate: 100 * (question.totalAnswers - question.correctAnswers) / question.totalAnswers
                            }));
                        } else {
                            this.wrongAnswers = [];
                        }
                    }
                });
        },
        movemyquiz() {
            this.$router.push({ name: 'mypagequiz' });
        }
    },
    created() {
        const resultId = parseInt(this.$route.params.quizResultId);
        this.fetchResultData(resultId);
    }
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
.resultbox{
    width: 1300px;
    height: 550px;
    background-color: #FFF0F0;
    box-shadow: 0 3px 3px rgb(167, 167, 167);
}
.listbox{
    float: left;
    width: 200px;
    height: 550px;
    padding-top: 35px;
    padding-left: 60px;
    text-align: left;
    line-height: 65px;
    border-right: 1px solid #888686;
}
.list_id{
    font-size: 25px;
    font-weight: bold;
    letter-spacing: 1px;
}
.result_databox{
    float: left;
    width: 500px;
    height: 550px;
}
.lang_data {
    margin-top: 40px;
    padding-left: 55px;
    padding-top: 10px;
    width: 500px;
    height: 50px;
    font-size: 20px;
    
}
.category_data {
    margin-top: 20px;
    width: 500px;
    height: 50px;
    font-size: 20px;
    padding-left: 55px;
    padding-top: 11px;
}
.date_data {
    margin-top: 20px;
    width: 500px;
    height: 50px;
    font-size: 20px;
    padding-left: 55px;
    padding-top: 9px;
}
.quiznum_data {
    margin-top: 20px;
    width: 500px;
    height: 50px;
    font-size: 20px;
    padding-left: 55px;
    padding-top: 6px;
}
.time_data {
    margin-top: 20px;
    width: 500px;
    height: 50px;
    font-size: 20px;
    padding-left: 55px;
    padding-top: 5px;
}
.score_data {
    margin-top: 20px;
    width: 500px;
    height: 50px;
    font-size: 20px;
    font-weight: bold;
    padding-left: 55px;
    padding-top: 4px;
}
.diagnosis_data {
    margin-top: 10px;
    width: 500px;
    height: 100px;
    font-size: 20px;
    padding-left: 55px;
    padding-top: 8px;
}
.graphbox{
    float: left;
    width: 600px;
    height: 550px;
}
.answer-chart{
    margin-top: 50px;
    margin-left: 130px;
    margin-bottom: 30px;
    width: 350px;
    height: 350px;
    border: 2px solid white;
    background: conic-gradient(#FFECEB 0% 15%, #FFD9D7 15% 40%, #FFBBB7 40% 70%, #FF9791 70% 90%, #FF7B74 90% 10%);
    border-radius: 50%;
}
.btnbox{
    margin-top: 50px;
    width: 1300px;
    height: 100px;
}
.quiz_myquiz_btn{
    background-color: #FF8989;
    margin-left: 500px;
    margin-top: 25px;
    width: 330px;
    height: 47px;
    border: none;
    border-radius: 10px;
    transition: background-color 0.2s;
    font-size: 25px;
    letter-spacing: 2px;
    font-weight: bold;
    box-shadow: 0 5px 5px #9f9f9f;
}
.quiz_answer_btn{
    margin-left: 70px;
    background-color: #FF8989;
    width: 200px;
    height: 47px;
    border: none;
    border-radius: 10px;
    transition: background-color 0.2s;
    font-size: 25px;
    letter-spacing: 2px;
    font-weight: bold;
    box-shadow: 0 3px 5px #9f9f9f;
}
.quiz_myquiz_btn:hover,
.quiz_answer_btn:hover {
  background-color: #fb6767;
  cursor: pointer;
}
</style>