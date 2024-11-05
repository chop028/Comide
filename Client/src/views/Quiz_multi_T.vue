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
            <div class="myquiz_hovermenu">내 퀴즈</div>
        </div>
        <div class="right_layout">
            <div class="quiz_titlebar">
                <div class="quiz_titletext">{{ quiz.title }}</div>
                <div class="question_num">{{ question.number }} {{ question.title }}</div>
                <div class="quiz_detail">
                    <div class="form-check" v-for="(option, index) in options" :key="index">
                        <input
                            class="form-check-input"
                            type="radio"
                            :name="'flexRadioDefault'"
                            :id="'flexRadioDefault' + (index + 1)"
                            :value="option"
                            v-model="selectedOption"
                            :checked="selectedOption === option"
                        />
                        <label :for="'flexRadioDefault' + (index + 1)" class="form-check-label">{{ option }}</label>
                    </div>
                </div>
            </div>
            <div class="btn_layout">
                <button class="submitbtn" @click="nextQuiz"><span style="color:white">{{ nextBtnText }}</span></button>
                <button class="prevbtn" @click="prevQuiz"><span style="color:white">이전</span></button>
            </div>
            
            </div>
        </div>
    
</template>

<script>
import apiClient from '@/axios/axios';
import moment from 'moment';
import { useAuthStore } from '@/store/auth';

export default {
    data() {
        const authStore = useAuthStore();
        return {
            userId: authStore.userInfo?.id || '',
            startTime: null,
            endTime: null,
            durationTime: null,
            nextBtnText: '다음',
            quizList: [],
            currentQuizIndex: 0,
            question: {
                number: '1.',
                title: '다음 중 옳지 않은 것을 고르시오.',
            },
            quiz: {},
            options: [],
            selectedOption: null,
        };
    },

    props: {
        msg: String
    },
    methods: {
        fetchQuizData(id) {
            apiClient
                .get(`${process.env.VUE_APP_API_URL}/api/quiz/get-quiz-detail-list.do?postId=${id}`)
                .then(response => {
                    if (response.data.success) {
                        const sortedPosts = response.data.result.map((quiz, index) => ({
                            ...quiz,
                            num: index,
                            answer: quiz.answer,
                            title: quiz.title,
                            options: [quiz.choice1, quiz.choice2, quiz.choice3, quiz.choice4, quiz.choice5],
                            selectedAnswer: null
                        }));

                        this.quizList = sortedPosts;
                        this.updateQuizData(this.quizList[0]);
                    } else {
                        console.error("Failed to fetch quiz data");
                    }
            });
        },
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
        updateQuizData(quizData) {
            this.question.number = quizData.num + 1 + '. ';
            this.question.title = quizData.title;
            this.options = quizData.options;
            this.selectedOption = quizData.selectedAnswer;
            
            if(this.currentQuizIndex == this.quizList.length -1) {
                this.nextBtnText = '제출';
            } else {
                this.nextBtnText = '다음';
            }
            if (this.quizList[this.currentQuizIndex].selectedOption !== null) {
                    this.selectedOption = this.quizList[this.currentQuizIndex].selectedOption;
            } else {
                this.selectedOption = null;
            }
        },
        nextQuiz() {
            if (this.selectedOption == null) {
                alert("정답을 선택해 주세요");
                return;
            }
            
            // 선택된 옵션의 인덱스를 저장
            const selectedOptionIndex = this.options.indexOf(this.selectedOption) + 1;

            // 현재 퀴즈의 선택된 옵션을 저장
            this.quizList[this.currentQuizIndex].selectedOption = selectedOptionIndex;

            if (this.currentQuizIndex < this.quizList.length - 1) {
                this.currentQuizIndex++;
                this.updateQuizData(this.quizList[this.currentQuizIndex]);
            } else {
                if (confirm("마지막 퀴즈입니다. 퀴즈를 마치고 결과를 확인하시겠습니까?")) {
                    this.endTimer();
                    this.sendResultData();
                }
            }
        },
        prevQuiz() {
            if (this.currentQuizIndex > 0) {
                this.currentQuizIndex--;
                this.updateQuizData(this.quizList[this.currentQuizIndex]);
            } else {
                alert("첫 번째 퀴즈입니다.");
            }
        },
        sendResultData() {
            const quizPostId = this.$route.params.quizPostId;
            const excuteData = moment(new Date()).format('YYYY.MM.DD');
            const durationTime = this.durationTime;
            const comment = '다형성에 대한 추가적인 학습이 필요함';
            let correctAnswer = 0;
            this.quizList.forEach(quiz => {
                if (quiz.answer == quiz.selectedOption) {
                    
                    correctAnswer++;
                }
            });
            const resultData = {
                userId: this.userId,
                quizPostId: quizPostId,
                excuteDate: excuteData,
                durationTime: durationTime,
                comment: comment,
                correctAnswer: correctAnswer
            };

            apiClient
                .post(`${process.env.VUE_APP_API_URL}/api/quiz/post-quiz-result.do`, resultData)
                .then((response) => {
                if (response.data.success) {
                    const quizResultId = response.data.result;
                    this.$router.push({name: 'quizresult', params: {quizResultId: quizResultId}});
                 }else{
                    console.error("get-board-list failed");
                }
                })
                .catch(function(error) {
                console.error(error);
                });
            
        },
        startTimer() {
            this.startTime = new Date();
        },
        endTimer() {
            this.endTime = new Date();
            const duration = this.endTime - this.startTime;
            const hours = Math.floor(duration / 3600000);
            const minutes = Math.floor((duration % 3600000) / 60000);
            const seconds = Math.floor(((duration % 3600000) % 60000) / 1000);
            this.durationTime = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
        },
    },
    created() {
        const quizId = parseInt(this.$route.params.quizPostId);
        this.fetchQuizData(quizId);
        this.startTimer();
    },
    mounted() {
        this.quizPostId = this.$route.params.quizPostId;  // 라우트에서 quizPostId 받아오기
        this.getQuizPostData();  // 컴포넌트가 마운트되면 퀴즈 데이터를 가져옴
},
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
.quiz_titlebar {
  width: 1210px;
  height: 75px;
  border-bottom: 2px solid #888686;
  justify-content: space-between;
}
.quiz_titletext {
  font-size: 45px;
}
.question_num{
    float: left;
    font-size: 25px;
    font-weight: bold;
    margin-top: 50px;
    margin-left: 30px;
}
.question_text{
    float: left;
    font-size: 25px;
    font-weight: bold;
    margin-top: 50px;
    margin-left: 10px;
}
.quiz_detail{
    width: 1210px;
    margin-top: 130px;
    border: 1px solid #888686;
    border-radius: 10px;
    padding-bottom: 50px;
}
.form-check{
    width: 100%;
    font-size: 25px;
    margin-top: 50px;
    margin-left: 30px;
}
.submitbtn{
    float: right;
    margin-top: 130px;
    background-color: #FFA0A0;
    width: 110px;
    height: 47px;
    border: none;
    border-radius: 10px;
    transition: background-color 0.2s;
    font-size: 20px;
    box-shadow: 0 5px 5px #888686;
}
.prevbtn{
    float: left;
    margin-top: 130px;
    margin-right: 75px;
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