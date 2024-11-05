<template>
<RouterView></RouterView>
<div class="mypage_wrap">
    <div class="mypage_menubar">
        <div class="mypage_topic">마이페이지</div>
        <div class="mypage_sub" @click="linkToinfo">회원정보</div>
        <div class="mypage_sub" @click="linkToact">활동내역</div>
        <div class="mypage_sub" @click="linkToquiz" id="nowpage">내 퀴즈</div>
        <div class="mypage_sub" @click="linkToAlarm">알림</div>
    </div>
  <div class="mypage_right">
    <div class="Quiz">내 퀴즈</div>
    <div class="quiz_line1"></div>
    <div class="quiz_menuwrap">
      <div class="quiz_btn" v-bind:style="quiz_btn" ref="quiz_btn" @click="trans_quiz" @mouseover="over_quiz" @mouseleave="leave_quiz">내가 푼 퀴즈</div>
    </div>
    <div class="activity_quizpage" v-if="show_quiz">
      <div class="quizpage_headwrap">
        <select class="lan_combobox">
          <option value="C" selected>C</option>
          <option value="JAVA">JAVA</option>
          <option value="C#">C#</option>
          <option value="C++">C++</option>
          <option value="Python">Python</option>
        </select>
        <select class="quiz_combobox">
          <option value="multiple" selected>객관식</option>
          <option value="subject">주관식</option>
        </select>
        <div class="datebox_wrap1">
          <input class="date-box1" type="date" v-model="date_box1">
        </div>
        <span class="and">~</span>
        <div class="datebox_wrap2">
          <input class="date-box2" type="date" v-model="date_box2">
        </div>
        <button class="checkbtn">조회</button>
      </div>
      <div class="quiz_headline"></div>
      <div class="quizlist">
        <ul>
          <li v-for="(result, index) in quizResultList" :key="index" class="quiz_item">
            <div class="quiz_content">
              <div class="quiz_title">{{ result.quizPost.title }}</div>
              <div class="quiz_wrap">
                <div class="quiz_category">{{ result.quizPost.category.name }}</div>
                <div class="quiz_wall"></div>
                <div class="quiz_time">{{ result.quizResult.excuteDate }}</div>
                <div class="quiz_re" @click="getQuizDetail(result.quizPost.id, result.quizPost.quizType)">다시풀기</div>
                <div class="quiz_result" @click="movequizresult(result.quizResult.id)">결과확인</div>
              </div>
              <div class="quiz_line"></div>
            </div>
          </li>
        </ul>
    </div>
      <div class="quiz_total" id="quizfoot">total: {{ totalPages }}, page {{ currentPage }}/{{ totalPages }}</div>
        <div class="page_num">
          <button class="quiz_pagebtn back" @click="prevPageGroup(true)"><img src="@/assets/arrow-left-short.svg"></button>
          <button v-for="page in displayedPages" :key="page" class="quiz_pagebtn" @click="goToPage(page, true)">{{ page }}</button>
          <button class="quiz_pagebtn next" @click="nextPageGroup(true)"><img src="@/assets/arrow-right-short.svg"></button>
        </div>
      <div class="searchbox">  
        <select class="combobox" >
          <option value="all">전체</option>
          <option value="err">언어</option>
          <option value="qna">정보처리</option>
       </select>
        <input class="searchbar" method="get" type="text" placeholder="검색어를 입력하세요">
        <button class="srcbtn">검색</button>
    </div>
  </div>
 </div>
</div>
</template>

<script>
import apiClient from '@/axios/axios';
import { useAuthStore } from '@/store/auth';

export default {
  methods: {
    getQuizList() {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/quiz/get-quiz-result-list.do?userId=${this.userId}`)
        .then((response) => {
          if (response.data.success) {
            const sortedResults = response.data.result.map((result, index) => ({
              ...result,
              num: index
            }));
            this.quizResultList = sortedResults.sort((a, b) => b.num - a.num);
            this.totalPages = Math.ceil(this.quizResultList.length / this.postsPerPage);
        } else {
          console.error("get-board-list failed");
        }
      })
      .catch(function(error) {
        console.error(error);
      });
    },
    linkToinfo() {
      this.$router.push({ name: 'mypageuserinfo', params: { category: "mypage" } });
    },
    linkToact() {
        this.$router.push({ name: 'mypageactivity', params: { category: "mypage" } });
    },
    linkToquiz() {
      this.$router.push({ name: 'mypagequiz', params: { category: "mypage" } });
    },
    linkTooption() {
      this.$router.push({ name: 'mypageoption', params: { category: "mypage" } });
    },
    linkToAlarm() {
      this.$router.push({ name: 'mypagealarm', params: { category: "mypage" } });
    },
    trans_quiz(){
      this.show_quiz = true;
      this.quizActivityHoverActive = false;
      this.quiz_btn['background-color']='#FFBEBA';
      this.quiz_btn['border']='1px solid #FFBEBA';
      this.quiz_btn['color']='black';
    },
    over_quiz(){
    if(this.quizActivityHoverActive) {
      this.quiz_btn['background-color']='#FFBEBA';
      }
    },
    leave_quiz(){
    if(this.quizActivityHoverActive) {
      this.quiz_btn['background-color']='#FFFFFF';
      }
    },
    getQuizDetail(quizid, quizType) {
      if(quizType == "multi") {
        this.$router.push({name: 'quiz_multi', params: {quizPostId: quizid}});
      }
    },
    movequizresult(quizResultId){
      this.$router.push({name: 'quizresult', params: {quizResultId: quizResultId}});
    },
    goToPage(page, confirm) {
      if(confirm) {
        this.currentPage = page;
      } else {
        this.currentPageC = page;
      }
    },
    prevPageGroup(confirm) {
      if(confirm) {
        if (this.displayedPages[0] > 1) {
          this.currentPage = this.displayedPages[0] - 1;
        }
      } else {
        if (this.displayedPagesC[0] > 1) {
          this.currentPageC = this.displayedPagesC[0] - 1;
        }
      }
    },
    nextPageGroup(confirm) {
      if(confirm) {
        if (this.displayedPages[this.displayedPages.length - 1] < this.totalPages) {
          this.currentPage = this.displayedPages[this.displayedPages.length - 1] + 1;
        }
      } else {
        if (this.displayedPagesC[this.displayedPagesC.length - 1] < this.totalPagesC) {
          this.currentPageC = this.displayedPagesC[this.displayedPagesC.length - 1] + 1;
        }
      }
    },
  },
  computed: {
    displayedPages() {
      const start = Math.floor((this.currentPage - 1) / 10) * 10 + 1;
      const end = Math.min(start + 9, this.totalPages);
      let pages = [];
      for (let i = start; i <= end; i++) {
        pages.push(i);
      }
      return pages;
    },
    paginatedPosts() {
      const startIndex = (this.currentPage - 1) * this.postsPerPage;
      const endIndex = startIndex + this.postsPerPage;
      return this.post.slice(startIndex, endIndex);
    },
  },
  data() {
    const authStore = useAuthStore();
    return{
      userId: authStore.userInfo?.id || '',
      show_quiz: true,
      quizActivityHoverActive: false,
      quizResultList: [],
      currentPage: 1,
      totalPages: 0,
      postsPerPage: 4,
      quiz: [
        {
          quizTitle: "객관식이지롱",
          category: "C",
          time: "2024.01.03",
        },
        {
          quizTitle: "2014년도 정보처리산업기사 필기문제",
          category: "정보처리",
          time: "2024.01.02",
        },
        {
          quizTitle: "2014년도 정보처리산업기사 필기문제",
          category: "정보처리",
          time: "2024.01.02",
        },
        {
          quizTitle: "2014년도 정보처리산업기사 필기문제",
          category: "정보처리",
          time: "2024.01.02",
        },
        {
          quizTitle: "2014년도 정보처리산업기사 필기문제",
          category: "정보처리",
          time: "2024.01.02",
        },
      ],
      quiz_btn: {
      width: '142px',
      height: '46px',
      'border-radius': '4px',
      border: '1px solid #FFBEBA',
      'background-color': '#FFBEBA',
      color: 'black',
      'font-size': '20px',
      'text-align': 'center',
      'line-height': '46px',
      cursor: 'pointer',
      'z-index': '1',
      position: 'relative',
      'margin-left': '5px'
    },
  };
  },
  mounted() {
    this.getQuizList();
  }
};
</script>

<style scoped>
.mypage_wrap{
    width: 1810px;
    height: 3000px;
    margin: 0 auto;
}
.mypage_menubar{
    float: left;
    margin-left: 100px;
    margin-top: 230px;
    width: 200px;
    height: 600px;
}
.mypage_topic {
  width: 200px;
  height: 50px;
  font-size: 32px;
  font-weight: 700;
  color: #000000;
  border-bottom: 2px solid #888686;
}
.mypage_sub:hover {
  color: black;
  font-weight: bold;
  cursor: pointer;
}
.mypage_sub {
  width: 200px;
  font-size: 22px;
  padding-top: 10px;
  line-height: 50px;
  color: #888686;
  transition: color 0.2s, font-weight 0.2s;
}
#nowpage{
    color:#000000;
}
.mypage_right{
    float: left;
    width: 1210px;
    height: 1100px;
}
.Quiz{
  font-size: 32px;
  color: black;
  margin-top: 220px;
  margin-left: 140px;
}
.quiz_line1{
  width: 1199px;
  border: 3px solid #FFBEBA;
  margin-left: 125px;
  margin-top: 20px;
}
.quiz_menuwrap{
  display:flex;
  align-content : space-between;
  width:321px;
  height:100px;
  margin-left: 125px;
  margin-top: 20px; 
}
.activity_quizpage{
  width:1199px;
  height: 679px;
  border-radius: 8px;
  background-color: #FFFFFF;
  border: 1px solid #FFBEBA;
  box-shadow: inset 0px -1px 1px 1px rgba(0, 0, 0, 0.5);
  margin-top: -60px;
  margin-left: 125px;
  z-index: 2;
  position: relative;
}
.quizpage_headwrap{
  display: flex;
  align-content: space-between;
  width: 1034px;
  height: 50px;
  margin-left: 80px;
  margin-top: 35px;
}
.lan_combobox{
    margin-left: 40px;
    font-size: 20px;
    text-align: center;
    border-radius: 10px;
    border: 0.5px solid #858585;
    width: 100px;
    height: 40px;
}
.quiz_combobox{
    margin-left: 20px;
    font-size: 20px;
    text-align: center;
    border-radius: 10px;
    border: 0.5px solid #858585;
    width: 100px;
    height: 40px;
}
.datebox_wrap1{
    margin-left: 20px;
    width: 239px;
    height: 38px;
}
.date-box1{
    width: 239px;
    height: 38px;
    border: 0.5px solid #858585;
    border-radius: 10px;
    text-align: center;
    background-color: white;
    z-index: 3;
    position: relative;
}
.and{
  color: #646464;
  font-size: 20px;
  margin-left: 20px;
}
.datebox_wrap2{
    margin-left: 20px;
    width: 239px;
    height: 38px;
}
.date-box2{
    width: 239px;
    height: 38px;
    border: 0.5px solid #858585;
    border-radius: 10px;
    text-align: center;
    background-color: white;
    z-index: 3;
    position: relative;
}
.checkbtn{
  width: 80px;
  height: 37px;
  background-color: #FFBBBB;
  border: 1px solid #888888;
  border-radius: 10px;
  margin-left: 20px;
  color: white;
  font-size: 20px;
  font-weight: bold;
}
.checkbtn:hover {
  background-color: #fda1a1;
  cursor: pointer;
}
.quiz_headline{
  width: 1030px;
  border: 1px solid #FAE9E9;
  margin-left: 80px;
  margin-top: 20px; 
}
.quizlist{
  width: 1034px;
  height: 320px;
  margin-left: 80px;
  overflow: hidden;
}
.quiz_item{
  display: flex;
  align-content: space-between;
  width: 1034px;
  height: 100px;
  margin-left: -33px;
}
.quiz_title{
  width: auto;
  max-width: 940px;
  overflow: hidden;
  height: 37px;
  max-width: 700px;
  display: inline-block;
  font-size: 20px;
  color: black;
  margin-left: 20px;
}
.quiz_wrap{
  display: flex;
  align-content: space-between;
  width: 1034px;
  height: 30px;
  margin-left: -33px;
}
.quiz_category{
  width: auto;
  max-width: 150px;
  display: inline-block;
  height: 15px;
  font-size: 15px;
  color: black;
  margin-left: 53px;
  margin-top: -14px;
}
.quiz_wall{
  width: 5px;
  height: 25px;
  background-color: #FFBEBA;
  border: 3px solid #FFBEBA;
  margin-left: 5px;
}
.quiz_time{
  width: 100px;
  height: 20px;
  font-size: 16px;
  color: #0000008c;
  margin-left: 3px;
}
.quiz_line{
  width: 1030px;
  height: 1px;
  border: 1px solid #FAE9E9;
  margin-left: -30px;
}
.quiz_title{
  width: auto;
  max-width: 940px;
  overflow: hidden;
  height: 37px;
  max-width: 700px;
  display: inline-block;
  font-size: 20px;
  color: black;
  margin-left: 20px;
  margin-top: 27px;
}
.quiz_wall{
  width: 5px;
  height: 25px;
  background-color: #FFBEBA;
  border: 3px solid #FFBEBA;
  margin-left: 3px;
  margin-top: -15px;
}
.quiz_time{
  width: 94px;
  height: 15px;
  font-size: 15px;
  color: black;
  margin-left: 6px;
  margin-top: -15px;
}
.quiz_re{
  width: 90px;
  height: 37px;
  background-color: #FFBBBB;
  border: 1px solid #888888;
  border-radius: 10px;
  color: white;
  font-size: 20px;
  text-align: center;
  font-weight: bold;
  line-height: 37px;
  margin-left: auto;
  margin-top: -20px;
}
.quiz_re:hover {
  background-color: #fda1a1;
  cursor: pointer;
}
.quiz_result{
  width: 90px;
  height: 37px;
  background-color: #FFBBBB;
  border: 1px solid #888888;
  border-radius: 10px;
  color: white;
  font-size: 20px;
  text-align: center;
  font-weight: bold;
  line-height: 37px;
  margin-left: 10px;
  margin-top: -20px;
}
.quiz_result:hover {
  background-color: #fda1a1;
  cursor: pointer;
}
.quiz_total{
  width: 141px;
  height: 25px;
  color: #000000;
  font-size: 16px;
  margin-left: 548px;
  margin-top: 50px;
}
.page_num{
  margin-top: 10px;
  margin-left: 540px;
  width: 300px;
  height: 75px;
}
.quiz_pagebtn{
  margin-right: 10px;
  width: 40px;
  height: 40px;
  border: 1px solid;
  background-color: #FFBEBA;
}
.quiz_pagebtn:hover{
  background-color: #f7afab;
}
.searchbox {
  width: 1210px;
  height: 80px;
  margin-top: -55px;
  font-size: 18px;
}
.searchbar{
  width: 540px;
  height: 40px;
  background-color: white;
  border: 1px solid #888686;
  text-align: center;
}
.combobox{
  width: 140px;
  height: 40px;
  text-align: center;
  margin-left: 180px;
  margin-right: 20px;
  margin-top: 27px;
}
.srcbtn{
  width: 130px;
  height: 40px;
  background-color: #FFBBBB;
  border: 1px solid #888686;
  margin-left: 20px;
}
.srcbtn:hover {
  background-color: #fda1a1;
  cursor: pointer;
}
.ul {
  padding: 20px;
}
#quizfoot{
  margin-top: 100px;
}
.quiz_content {
  padding: 0 0 0 20px;
}
</style>