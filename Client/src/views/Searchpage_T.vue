<template>
  <router-view></router-view>
  <div class="searchpage_wrap">
    <div class="search_head_wrap">
      <div class="search_text">통합검색</div>
      <input class="search_all-bar" type="text" v-model="searchQuery" @input="updateSearchQuery">
      <div class="search_btn" @click="search">검색</div>
    </div>
    <div class="search_category_wrap">
      <div class="all_btn" :style="all_btn" ref="all_btn" @click="trans_all" @mouseover="over_all" @mouseleave="leave_all">전체</div>
      <div class="post_btn" :style="post_btn" ref="post_btn" @click="trans_post" @mouseover="over_post" @mouseleave="leave_post">게시글</div>
      <div class="quiz_btn" :style="quiz_btn" ref="quiz_btn" @click="trans_quiz" @mouseover="over_quiz" @mouseleave="leave_quiz">퀴즈</div>
      <div class="develop_btn" :style="develop_btn" ref="develop_btn" @click="trans_develop" @mouseover="over_develop" @mouseleave="leave_develop">개발지식</div>
    </div>
    <div class="searchpage_all" v-if="show_all">
      <div class="all_headwrap">
        <div class="all_text">전체글</div>
        <div class="all_num">17,322</div>
        <div class="acc">●정확도 순</div>
        <div class="recent">●최신 순</div>
      </div>
      <div class="search_result">
        <ul>
          <li v-for="(search_all, index) in search_all" :key="index" class="search_item">
            <div class="search_title" v-html="highlightedText(search_all.title)"></div>
            <div class="search_context" v-html="highlightedText(search_all.context)"></div>
            <div class="search_footwrap">
              <div class="search_category">{{ search_all.category }}</div>
              <div class="search_date">{{ search_all.date }}</div>
            </div>
            <div class="search_line"></div>
          </li>
        </ul>
      </div>
    </div>
    <div class="searchpage_post" v-if="show_post">
      <div class="post_headwrap">
        <div class="post_text">게시글</div>
        <div class="post_num">1,557</div>
        <div class="acc">●정확도 순</div>
        <div class="recent">●최신 순</div>
      </div>
      <div class="search_result">
        <ul>
          <li v-for="(search_post, index) in search_post" :key="index" class="search_item">
            <div class="search_title" v-html="highlightedText(search_post.title)"></div>
            <div class="search_context" v-html="highlightedText(search_post.context)"></div>
            <div class="search_footwrap">
              <div class="search_category">{{ search_post.category }}</div>
              <div class="search_date">{{ search_post.date }}</div>
            </div>
            <div class="search_line"></div>
          </li>
        </ul>
      </div>
    </div>
    <div class="searchpage_quiz" v-if="show_quiz">
      <div class="quiz_headwrap">
        <div class="quiz_text">퀴즈</div>
        <div class="quiz_num">7,332</div>
        <div class="acc">●정확도 순</div>
        <div class="recent">●최신 순</div>
      </div>
      <div class="search_result">
        <ul>
          <li v-for="(search_quiz, index) in search_quiz" :key="index" class="search_item">
            <div class="search_title" v-html="highlightedText(search_quiz.title)"></div>
            <div class="search_context" v-html="highlightedText(search_quiz.context)"></div>
            <div class="search_footwrap">
              <div class="search_category">{{ search_quiz.category }}</div>
              <div class="search_date">{{ search_quiz.date }}</div>
            </div>
            <div class="search_line"></div>
          </li>
        </ul>
      </div>
    </div>
    <div class="searchpage_develop" v-if="show_develop">
      <div class="develop_headwrap">
        <div class="develop_text">개발지식</div>
        <div class="develop_num">32</div>
        <div class="acc">●정확도 순</div>
        <div class="recent">●최신 순</div>
      </div>
      <div class="search_result">
        <ul>
          <li v-for="(search_develop, index) in search_develop" :key="index" class="search_item">
            <div class="search_title" v-html="highlightedText(search_develop.title)"></div>
            <div class="search_context" v-html="highlightedText(search_develop.context)"></div>
            <div class="search_footwrap">
              <div class="search_category">{{ search_develop.category }}</div>
              <div class="search_date">{{ search_develop.date }}</div>
            </div>
            <div class="search_line"></div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
methods: {
     highlightedText(text) {
      if (!this.searchQuery) {
        return text;
      }
      const escapedQuery = this.searchQuery.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
      const regex = new RegExp(`(${escapedQuery})`, 'gi');
      return text.replace(regex, '<span style="color: red;">$1</span>');
    },
    trans_all(){
      this.show_all = true;
      this.show_post = false;
      this.show_quiz = false;
      this.show_develop = false;
      this.allHoverActive = false;
      this.postHoverActive = true;
      this.quizHoverActive = true;
      this.developHoverActive = true;
      this.all_btn['background-color']='#FFBBBB';
      this.all_btn['color']='#E25454';
      this.all_btn['border']='none';
      this.post_btn['background-color']='#FFFFFF';
      this.post_btn['border']='1px solid #999292';
      this.post_btn['color']='#999292';
      this.quiz_btn['background-color']='#FFFFFF';
      this.quiz_btn['border']='1px solid #999292';
      this.quiz_btn['color']='#999292';
      this.develop_btn['background-color']='#FFFFFF';
      this.develop_btn['border']='1px solid #999292';
      this.develop_btn['color']='#999292';
    },
    trans_post(){
      this.show_all = false;
      this.show_post = true;
      this.show_quiz = false;
      this.show_develop = false;
      this.allHoverActive = true;
      this.postHoverActive = false;
      this.quizHoverActive = true;
      this.developHoverActive = true;
      this.post_btn['background-color']='#FFBBBB';
      this.post_btn['color']='#E25454';
      this.post_btn['border']='none';
      this.all_btn['background-color']='#FFFFFF';
      this.all_btn['border']='1px solid #999292';
      this.all_btn['color']='#999292';
      this.quiz_btn['background-color']='#FFFFFF';
      this.quiz_btn['border']='1px solid #999292';
      this.quiz_btn['color']='#999292';
      this.develop_btn['background-color']='#FFFFFF';
      this.develop_btn['border']='1px solid #999292';
      this.develop_btn['color']='#999292';
    },
    trans_quiz(){
      this.show_all = false;
      this.show_post = false;
      this.show_quiz = true;
      this.show_develop = false;
      this.allHoverActive = true;
      this.postHoverActive = true;
      this.quizHoverActive = false;
      this.developHoverActive = true;
      this.quiz_btn['background-color']='#FFBBBB';
      this.quiz_btn['color']='#E25454';
      this.quiz_btn['border']='none';
      this.all_btn['background-color']='#FFFFFF';
      this.all_btn['border']='1px solid #999292';
      this.all_btn['color']='#999292';
      this.post_btn['background-color']='#FFFFFF';
      this.post_btn['border']='1px solid #999292';
      this.post_btn['color']='#999292';
      this.develop_btn['background-color']='#FFFFFF';
      this.develop_btn['border']='1px solid #999292';
      this.develop_btn['color']='#999292';
    },
    trans_develop(){
      this.show_all = false;
      this.show_post = false;
      this.show_quiz = false;
      this.show_develop = true;
      this.allHoverActive = true;
      this.postHoverActive = true;
      this.quizHoverActive = true;
      this.developHoverActive = false;
      this.develop_btn['background-color']='#FFBBBB';
      this.develop_btn['color']='#E25454';
      this.develop_btn['border']='none';
      this.all_btn['background-color']='#FFFFFF';
      this.all_btn['border']='1px solid #999292';
      this.all_btn['color']='#999292';
      this.post_btn['background-color']='#FFFFFF';
      this.post_btn['border']='1px solid #999292';
      this.post_btn['color']='#999292';
      this.quiz_btn['background-color']='#FFFFFF';
      this.quiz_btn['border']='1px solid #999292';
      this.quiz_btn['color']='#999292';
    },
    over_all(){
    if(this.allHoverActive) {
      this.all_btn['background-color']='#FFBEBA';
      }
    },
    leave_all(){
    if(this.allHoverActive) {
      this.all_btn['background-color']='#FFFFFF';
      }
    },
    over_post(){
    if(this.postHoverActive) {
      this.post_btn['background-color']='#FFBEBA';
      }
    },
    leave_post(){
    if(this.postHoverActive) {
      this.post_btn['background-color']='#FFFFFF';
      }
    },
    over_quiz(){
    if(this.quizHoverActive) {
      this.quiz_btn['background-color']='#FFBEBA';
      }
    },
    leave_quiz(){
    if(this.quizHoverActive) {
      this.quiz_btn['background-color']='#FFFFFF';
      }
    },
    over_develop(){
    if(this.developHoverActive) {
      this.develop_btn['background-color']='#FFBEBA';
      }
    },
    leave_develop(){
    if(this.developHoverActive) {
      this.develop_btn['background-color']='#FFFFFF';
      }
    },
    search() {
      this.$router.push({ name: 'search', query: { q: this.searchQuery } }).then(() => {
        this.$router.go(); // 페이지를 새로고침하여 쿼리 파라미터를 반영
      });
      this.updateSearchResults();
    },
    updateSearchResults() {
      this.contents = this.contents.map(content => ({
        ...content,
        text: this.highlightedText(content.text)
      }));
      this.posts = this.posts.map(post => ({
        ...post,
        text: this.highlightedText(post.text)
      }));
      this.quizzes = this.quizzes.map(quiz => ({
        ...quiz,
        text: this.highlightedText(quiz.text)
      }));
      this.developments = this.developments.map(development => ({
        ...development,
        text: this.highlightedText(development.text)
      }));
    },
  },
data() {
    return{
      show_all: true,
      show_post: false,
      show_quiz: false,
      show_develop: false,
      allHoverActive: false,
      postHoverActive: true,
      quizHoverActive: true,
      developHoverActive: true,
      searchQuery: '',
      search_all: [
      {
        title: "C# 협업 모집합니다!!",
        context: "팀 아리가또 고자이마스에서 c#을 이용한 프로젝트를 진행하려고 합니다! 관심 있으신분들은 저희 팀의 리더이신 나까무라상에게 dm으로🙏🙏🙏🙏",
        category: "게시글 · 협업모집",
        date: "2024.01.23",
      },
      {
        title: "그거 아시나요?",
        context: "그거 그거 아시나요????",
        category: "언어지식 · C#",
        date: "2024.01.22",
      },
    ],
    search_post: [
      {
        title: "C# 협업 모집합니다!!",
        context: "팀 아리가또 고자이마스에서 c#을 이용한 프로젝트를 진행하려고 합니다! 관심 있으신분들은 저희 팀의 리더이신 나까무라상에게 dm으로🙏🙏🙏🙏",
        category: "퀴즈 · 언어 · C#",
        date: "2024.01.23",
      },
    ],
    search_quiz: [
      {
        title: "2024년 1월 21일 C# 제어문 퀴즈문제 1",
        context: "2024년 1월 21일자 C# 퀴즈문제입니다.",
        category: "게시글 · 협업모집",
        date: "2024.01.21",
      },
    ],
    search_develop: [
      {
        title: "C#의 장점? 그런건 없다",
        context: "이거 언제까지 써야하는지 좀 어지럽습니다. 제가 무슨 부귀영화를 누리겠다고 이걸 이렇게 하는걸까요. 피그마 이런 삐 삐삐삐 삐 삐삐삐 여기까지 하겠ㅅ,ㅡㅂ니ㅣ. .ㄴㅇㅁ ㅇ",
        category: "언어지식 · C",
        date: "2024.01.23",
      },
    ],
      all_btn: {
      'width': '110px',
      'height': '42px',
      'border-radius': '30px',
      'background-color': '#FFBBBB',
      'color': '#E25454',
      'font-size': '20px',
      'font-weight': '600px',
      'text-align': 'center',
      'line-height': '42px',
      cursor: 'pointer',
    },
    post_btn: {
      'width': '110px',
      'height': '42px',
      'border': '2px solid #999292',
      'border-radius': '30px',
      'background-color': '#FFFFFF',
      'color': '#999292',
      'font-size': '20px',
      'font-weight': '600px',
      'text-align': 'center',
      'line-height': '42px',
      'cursor': 'pointer',
      'margin-left': '15px',
    },
    quiz_btn: {
      'width': '110px',
      'height': '42px',
      'border': '2px solid #999292',
      'border-radius': '30px',
      'background-color': '#FFFFFF',
      'color': '#999292',
      'font-size': '20px',
      'font-weight': '600px',
      'text-align': 'center',
      'line-height': '42px',
      'cursor': 'pointer',
      'margin-left': '15px',
    },
    develop_btn: {
      'width': '110px',
      'height': '42px',
      'border': '2px solid #999292',
      'border-radius': '30px',
      'background-color': '#FFFFFF',
      'color': '#999292',
      'font-size': '20px',
      'font-weight': '600px',
      'text-align': 'center',
      'line-height': '42px',
      'cursor': 'pointer',
      'margin-left': '15px',
    },
  }
},
 created() {
    this.searchQuery = this.$route.query.q || '';
  },
  watch: {
    '$route.query.q'(newQuery) {
      this.searchQuery = newQuery;
    }
  },
}
</script>

<style scoped>
.searchpage_wrap{
  margin: auto;
  margin-top: 180px;
  width: 1300px;
  height: 1600px;
}
.search_head_wrap{
    display: flex;
    align-content : space-between;
    width:1034px;
    height: 51px;
    margin-left: 120px;
    margin-top: 210px;
}
.search_text{
    width: 130px;
    height: 39px;
    font-weight: bold;
    font-size: 32px;
    color: #515151;
}
.search_all-bar{
    width:750px;
    height: 51px;
    border-radius:10px;
    border: 2px solid #999292;
    background-color: #FFFFFF;
    margin-left: 20px;
}
.search_btn{
    width: 104px;
    height: 51px;
    background-color: #FFBBBB;
    font-size: 20px;
    font-weight: 800;
    color: #FFFFFF;
    text-align: center;
    line-height: 51px;
    cursor: pointer;
    border-radius: 10px;
    margin-left: 20px;
}
.search_category_wrap{
    display: flex;
    align-content : space-between;
    width:1034px;
    height: 51px;
    margin-left: 120px;
    margin-top: 40px;
}
.searchpage_all{
    width: 1300px;
    height: 1400px;
    margin-top: 20px;
}
.all_headwrap{
    display: flex;
    align-content : space-between;
    width:1034px;
    height: 51px;
    margin-left: 120px;
    margin-top: 20px;
}
.all_text{
    width:74px;
    height:29px;
    font-weight: 600px;
    font-size: 24px;
    color: #000000;
}
.all_num{
    width: 90px;
    height: 29px;
    font-weight: 600px;
    font-size: 24px;
    color: #737373;
    margin-left: 20px;
}
.acc{
    width: 155px;
    height: 29px;
    font-weight: bold;
    font-size: 24px;
    color: #E25454;
    margin-left: 550px;
    cursor: pointer;
}
.recent{
    width: 125px;
    height: 29px;
    font-weight: bold;
    font-size: 24px;
    color: #767676;
    margin-left: 10px;
    cursor: pointer;
}
.search_result{
    width: 1034px;
    height: 150px;
    margin-left: 120px;
    margin-top: 20px;
}
.search_title{
    width: 500px;
    height: 30px;
    font-weight: 600px;
    font-size: 24px;
    color: #000000;
    max-width: 900px;
    display: inline-block;
    overflow: hidden;
}
.search_context{
  width: 1034px;
  height: 60px;
  font-size: 20px;
  color: #6C6C6C;
  max-width: 1034px;
  max-height: 100px;
  overflow: hidden;
}
.search_footwrap{
  width: 1034px;
  height: 25px;
  margin-top: 10px;
  display: flex;
  align-content : space-between;
}
.search_category{
  width: 160px;
  height: 26px;
  max-width: 200px;
  overflow: hidden;
  font-size: 20px;
  color: #000000;
}
.search_date{
  width:108px;
  height: 24px;
  font-size: 20px;
  color: #565656;
  margin-left: 10px;
}
.search_line{
  width: 1034px;
  height: 1px;
  background-color: #999292;
  margin-top: 19px;
}
.searchpage_post{
    width: 1300px;
    height: 1400px;
    margin-top: 20px;
}
.post_headwrap{
    display: flex;
    align-content : space-between;
    width:1034px;
    height: 51px;
    margin-left: 120px;
    margin-top: 20px;
}
.post_text{
    width:74px;
    height:29px;
    font-weight: 600px;
    font-size: 24px;
    color: #000000;
}
.post_num{
    width: 90px;
    height: 29px;
    font-weight: 600px;
    font-size: 24px;
    color: #737373;
    margin-left: 20px;
}
.searchpage_quiz{
    width: 1300px;
    height: 1400px;
    margin-top: 20px;
}
.quiz_headwrap{
    display: flex;
    align-content : space-between;
    width:1034px;
    height: 51px;
    margin-left: 120px;
    margin-top: 20px;
}
.quiz_text{
    width:74px;
    height:29px;
    font-weight: 600px;
    font-size: 24px;
    color: #000000;
}
.quiz_num{
    width: 90px;
    height: 29px;
    font-weight: 600px;
    font-size: 24px;
    color: #737373;
    margin-left: 20px;
}
.searchpage_develop{
    width: 1300px;
    height: 1400px;
    margin-top: 20px;
}
.develop_headwrap{
    display: flex;
    align-content : space-between;
    width:1034px;
    height: 51px;
    margin-left: 120px;
    margin-top: 20px;
}
.develop_text{
    width:120px;
    height:29px;
    font-weight: 600px;
    font-size: 24px;
    color: #000000;
}
.develop_num{
    width: 90px;
    height: 29px;
    font-weight: 600px;
    font-size: 24px;
    color: #737373;
    margin-left: 20px;
}
.highlight {
  color: red;
}
</style>