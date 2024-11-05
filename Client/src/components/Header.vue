<template>
<div class="wrap">
  <div class="header">
      <div class="blank1">
        <div class="logo">
        <img src="@/assets/Logo.svg" width="90px" height="90px" @click="handleClick"></div>
        <form class="searchbar" @submit.prevent="linkToSearch">
          <input class="search-txt" type="text" v-model="searchQuery" placeholder="나의 진짜 성장을 도와줄 컨텐츠를 찾아보세요">
          <img class="search-icon" src="../assets/search.png" @click="linkToSearch"/>
        </form>
        <div v-if="isAuthenticated" class="header-user-profile">
          <div class="user_head" @click="toggleProfileMenu">
            <img :src="userInfo.picture" class="header-profile-icon"/>
            <span class="head-username">{{ userInfo.name }}</span>
          </div>
          <div v-if="showProfileMenu" class="profile-menu" @click.stop>
            <div class="profile-header">
              <img :src="userInfo.picture" class="header-profile-picture" />
              <span class="header-username">{{ userInfo.name }}</span>
            </div>
            <div class="menu-options">
              <div class="signup" id="mypage"><router-link to="/mypage/userinfo" @click="closeProfileMenu">마이페이지</router-link></div>
              <div class="login"  id="logout" @click="logout()"><router-link to="/logout/redirect" @click="closeProfileMenu">로그아웃</router-link></div>
            </div>
          </div>
        </div>
        <div v-else>
          <div class="headlogin"><router-link to="/login">로그인</router-link></div>
        </div>
      </div>
    </div>
  <div class="categorybar">
<nav class="menu-1">
<ul>
  <li>
  <a @click="linkToDevelopInt">개발지식</a>
  <ul>
    <li>
      <a @click="linkToDevelopInt">언어지식</a>
      <ul>
        <li><a>웹/프론트엔드</a></li>
        <li><a>서버/백엔드</a></li>
      </ul>
    </li>
  </ul>
</li>
<li>
  <a @click="linkToQuizlistAll">퀴즈</a>
  <ul>
    <li>
      <a @click="linkToQuizlistAll">전체퀴즈</a>
    </li>
    <li>
      <a @click="linkToMyQuiz">내 퀴즈</a>
    </li>
  </ul>
</li>
<li>
  <a @click="linkToCodeEditer">코드에디터</a>
</li>
<li>
  <a @click="linkToMyGroup">내 그룹</a>
</li>
<li>
  <a @click="linkToBoardMain">게시글</a>
  <ul>
    <li><a @click="linkToBoard('err')">오류코드</a></li>
    <li><a @click="linkToBoard('qna')">질의응답</a></li>
    <li><a @click="linkToBoard('opensrc')">오픈소스</a></li>
    <li><a @click="linkToBoard('help')">협업모집</a></li>
  </ul>
</li>
<li>
  <a @click="linkToBoard('help')">협업 모집</a>
</li>
</ul>
</nav>

</div>
</div>
</template>

<script>
import { computed } from 'vue';
import { useAuthStore } from '../store/auth';
import eventBus from '@/eventBus/eventBus';

export default {
  name: 'header_1',
  data() {
    return {
      clickCount: 0,
      clickTimer: null,
      onshow2_1: false,
      onshow2_2: false,
      onshow2_3: false,
      onshow2_4: false,
      onshow2_5: false,
      onshow3_1_1: false,
      onshow3_1_2: false,
      onshow3_1_3: false,
      onshow3_1_4: false,
      onshow3_2_1: false,
      onshow3_2_2: false,
      onshow3_2_3: false,
      onshow3_3_1: false,
      onshow3_4_1: false,
      onshow3_4_2: false,
      onshow3_4_3: false,
      onshow3_4_4: false,
      onshow3_5_1: false,
      showProfileMenu: false,
      searchQuery: '',
    };
  },
  setup() {
    const authStore = useAuthStore();

    const isAuthenticated = computed(() => authStore.token !== null);

    const userInfo = computed(() => authStore.userInfo || {});

    const logout = () => {
      authStore.clearAuth();  // Pinia 스토어의 로그아웃 액션 실행
      window.location.href = '/';  // 로그아웃 후 메인 페이지로 리다이렉트
    };

    return {
      isAuthenticated,
      userInfo,
      logout,
    };
  },
  watch: {
    clickCount(newVal) {
      if (newVal === 1) {
        this.linkToMainPage();
      }
    },
  },
  methods: {
    handleClick() {
      this.clickCount++;

      if (this.clickCount === 5) {
        this.$router.push({ name: 'adminmain' });
      } else {
        if (this.clickTimer) {
          clearTimeout(this.clickTimer);
        }
        this.clickTimer = setTimeout(() => {
          this.clickCount = 0;
        }, 2000);
      }
    },
     navigateToLink(route) {
    switch (route) {
      case 'developint':
        this.linkToDevelopInt();
        break;
      case 'quizlistall':
        this.linkToQuizlistAll();
        break;
      case 'codeedit':
        this.linkToCodeEditer();
        break;
      case 'boardmain':
        this.linkToBoardMain();
        break;
      default:
        break;
    }
  },
    toggleProfileMenu() {
      this.showProfileMenu = !this.showProfileMenu;
    },
    closeProfileMenu() {
      this.showProfileMenu = false;
    },
    handleClickOutside(event) {
      if (this.showProfileMenu && this.$refs.profileMenu && !this.$refs.profileMenu.contains(event.target)) {
        this.closeProfileMenu();
      }
    },
    linkToMainPage() {
      this.$router.push({ name: 'main', params: { category: "main" } });
    },
    linkToBoardMain() {
      this.$router.push({ name: 'board', params: { category: "main" } });
    },
    linkToQuizlistAll() {
      this.$router.push({ name: 'quizlistall', params: { category: "main" } });
    },
    linkToQuizlistInfo() {
      this.$router.push({ name: 'quizlistinfo', params: { category: "main" } });
    },
    linkToCodeEditer() {
      this.$router.push({ name: 'codeedit', params: { category: "main" } });
    },
    linkToDevelopInt(){
      this.$router.push({ name: 'developint', params: { category: "main" } });
    },
    linkToDevelopInfor(){
      this.$router.push({ name: 'developinfor', params: { category: "main" } });
    },
    linkToDevelopInforUpload(){
      this.$router.push({ name: 'developinforupload', params: { category: "main" } });
    },
    linkToSearch(){
      this.$router.push({ name: 'search', query: { q: this.searchQuery } });
    },
    linkToMyQuiz(){
      this.$router.push({ name: 'mypagequiz' });
    },
    linkToMyGroup() {
      eventBus.emit('openFloatingPage', 'MyGroupPage');
    },
    linkToBoard(postcate) {
      this.$router.push({ name: 'board', params: { category: postcate } });
      switch(postcate) {
        case 'main': 
          this.boardTitle = '전체게시글';
          break;
        case 'err':
          this.boardTitle = '오류코드';
          break;
        case 'qna':
          this.boardTitle = '질의응답';
          break;
        case 'opensrc':
          this.boardTitle = '오픈소스';
          break;
        case 'help':
          this.boardTitle = '협업모집';
          break;
      }
    },
    show1_1() {
      this.onshow2_1 = true;
      this.onshow2_2 = false;
      this.onshow2_3 = false;
      this.onshow2_4 = false;
      this.onshow2_5 = false;
      this.onshow3_1_1 = false;
      this.onshow3_1_2 = false;
      this.onshow3_1_3 = false;
      this.onshow3_1_4 = false;
      this.onshow3_2_1 = false;
      this.onshow3_2_2 = false;
      this.onshow3_2_3 = false;
      this.onshow3_3_1 = false;
      this.onshow3_4_1 = false;
      this.onshow3_4_2 = false;
      this.onshow3_4_3 = false;
      this.onshow3_4_4 = false;
      this.onshow3_5_1 = false;
    },
    show1_2() {
      this.onshow2_1 = false;
      this.onshow2_2 = true;
      this.onshow2_3 = false;
      this.onshow2_4 = false;
      this.onshow2_5 = false;
      this.onshow3_1_1 = false;
      this.onshow3_1_2 = false;
      this.onshow3_1_3 = false;
      this.onshow3_1_4 = false;
      this.onshow3_2_1 = false;
      this.onshow3_2_2 = false;
      this.onshow3_2_3 = false;
      this.onshow3_3_1 = false;
      this.onshow3_4_1 = false;
      this.onshow3_4_2 = false;
      this.onshow3_4_3 = false;
      this.onshow3_4_4 = false;
      this.onshow3_5_1 = false;
    },
    show1_3() {
      this.onshow2_1 = false;
      this.onshow2_2 = false;
      this.onshow2_3 = true;
      this.onshow2_4 = false;
      this.onshow2_5 = false;
      this.onshow3_1_1 = false;
      this.onshow3_1_2 = false;
      this.onshow3_1_3 = false;
      this.onshow3_1_4 = false;
      this.onshow3_2_1 = false;
      this.onshow3_2_2 = false;
      this.onshow3_2_3 = false;
      this.onshow3_3_1 = false;
      this.onshow3_4_1 = false;
      this.onshow3_4_2 = false;
      this.onshow3_4_3 = false;
      this.onshow3_4_4 = false;
      this.onshow3_5_1 = false;
    },
    show1_4() {
      this.onshow2_1 = false;
      this.onshow2_2 = false;
      this.onshow2_3 = false;
      this.onshow2_4 = true;
      this.onshow2_5 = false;
      this.onshow3_1_1 = false;
      this.onshow3_1_2 = false;
      this.onshow3_1_3 = false;
      this.onshow3_1_4 = false;
      this.onshow3_2_1 = false;
      this.onshow3_2_2 = false;
      this.onshow3_2_3 = false;
      this.onshow3_3_1 = false;
      this.onshow3_4_1 = false;
      this.onshow3_4_2 = false;
      this.onshow3_4_3 = false;
      this.onshow3_4_4 = false;
      this.onshow3_5_1 = false;
    },
    show1_5() {
      this.onshow2_1 = false;
      this.onshow2_2 = false;
      this.onshow2_3 = false;
      this.onshow2_4 = false;
      this.onshow2_5 = true;
      this.onshow3_1_1 = false;
      this.onshow3_1_2 = false;
      this.onshow3_1_3 = false;
      this.onshow3_1_4 = false;
      this.onshow3_2_1 = false;
      this.onshow3_2_2 = false;
      this.onshow3_2_3 = false;
      this.onshow3_3_1 = false;
      this.onshow3_4_1 = false;
      this.onshow3_4_2 = false;
      this.onshow3_4_3 = false;
      this.onshow3_4_4 = false;
      this.onshow3_5_1 = false;
    },
    show2_1_1() {
      this.onshow3_1_1 = true;
      this.onshow3_1_2 = true;
      this.onshow3_1_3 = false;
      this.onshow3_1_4 = false;
    },
    show2_1_2() {
      this.onshow3_1_1 = false;
      this.onshow3_1_2 = false;
      this.onshow3_1_3 = true;
      this.onshow3_1_4 = true;
    },
    show2_2_1() {
      this.onshow3_2_1 = true;
      this.onshow3_2_2 = true;
      this.onshow3_2_3 = false;
    },
    show2_2_2() {
      this.onshow3_2_1 = false;
      this.onshow3_2_2 = false;
      this.onshow3_2_3 = true;
    },
    show2_3_1() {
      this.onshow3_3_1 = true;
    },
    show2_4_1() {
      this.onshow3_4_1 = true;
      this.onshow3_4_2 = false;
      this.onshow3_4_3 = false;
      this.onshow3_4_4 = false;
    },
    show2_4_2() {
      this.onshow3_4_1 = false;
      this.onshow3_4_2 = true;
      this.onshow3_4_3 = false;
      this.onshow3_4_4 = false;
    },
    show2_4_3() {
      this.onshow3_4_1 = false;
      this.onshow3_4_2 = false;
      this.onshow3_4_3 = true;
      this.onshow3_4_4 = false;
    },
    show2_4_4() {
      this.onshow3_4_1 = false;
      this.onshow3_4_2 = false;
      this.onshow3_4_3 = false;
      this.onshow3_4_4 = true;
    },
    show2_5_1() {
      this.onshow3_5_1 = true;
    },
  },
  mounted() {
    document.addEventListener("click", this.handleClickOutside);
    const authStore = useAuthStore();
    authStore.loadAuthFromLocalStorage(); // localStorage에서 인증 정보 로드
  },
   beforeUnmount() {
    document.removeEventListener("click", this.handleClickOutside);
  },
};
</script>

<style>
.wrap{
  top: 0;
  width: 100%;
  height: 174px;
  margin: auto;
  background-color: #000000;
  position: fixed;
  z-index: 100;
}
.header {
  display: flex;
  height: 94px;
  background-color: #f9e9e9;
  z-index: 100;
}
.blank1{
  margin: auto;
  display: flex;
  width: 142s0px;
  height: 94px;
}
.logo {
  margin-left: 20px;
  margin-top: 7px;
  font-weight: 400;
  color: #000000;
  font-size: 50px;
  line-height: normal;
  font-family: "Inter", Helvetica;
  letter-spacing: 0;
  cursor: pointer;

}
.welcomemsg{
  padding-left: 30px;
  margin-top: 30px;
  display: flex;
  font-size: 25px;
}
.searchbar .search-txt{
    margin-left: 50px;
    margin-top:18px;
    width: 588px;
    height: 54px;
    border: 1px solid gray;
    border-radius: 15px;
    background-color: #ffffff;
    outline: none;
}
.search-txt{
  padding-left: 10px;
}
.search-txt::placeholder{
  color: #ced4da;
}
.search-icon {
  width: 32px;
  height: 32px;
  object-fit: cover;
  margin-left: -40px;
  margin-top: -5px;
  cursor: pointer;
}
.login{
  margin-left: 150px;
  margin-top: 27px;
  float: left;
}
.login a {
  font-size: 25px;
  color: #000000;
  text-decoration-line: none;
}
.headlogin{
  width: 105px;
  height:52px;
  border: 1px solid gray;
  border-radius: 6px;
  background-color: #FF9898;
  margin-left: 220px;
  margin-top: 18px;
  text-align: center;
  line-height: 52px;
  cursor: pointer;
}
.headlogin a {
  font-weight: 500;
  font-size: 25px;
  color: white;
  text-decoration-line: none;
}
.headlogin:hover {
  background-color: #f98585;
}
.signup{
  margin-left: 40px;
  margin-top: 27px;
  float: left;
}
.signup a {
  font-size: 25px;
  color: #000000;
  text-decoration-line: none;
}
.categorybar{
  width: 100%;
  height: 80px;
  background-color: #ffbbbb;
}
.category-blank{
  margin: auto;
  display: flex;
  width: 1490px;
  height: 80px;
}
.category-square .category-groups {
  width: 130px;
  height: 78px;
  margin-left: 200px;
  background-color: #ffbbbb;
}
.category-square2 {
  width: 203px;
  height: 78px;
  background-color: #ffbbbb;
}
.category-square :hover .category-square2 {
  background-color: #ff5353;
}
.category-square :hover .category-text {
  color: white;
}
.category-square :hover .category-icon {
  content: url(../assets/whiteMenuIcon.png);
}
.category-text {
  width: 100px;
  margin-top: -54px;
  margin-left: 80px;
  font-weight: 700;
  color: #000000;
  font-size: 24px;
  line-height: normal;
  font-family: 'Inter', Helvetica;
  letter-spacing: 0;
}
.category-icon {
  position: relative;
  width: 36px;
  height: 36px;
  margin-top: -60px;
  margin-left: 30px;
}
.category-menu {
  display: none;
  /* opacity: 0;
  visibility: none;
  transition: all 1s ease-in-out; */
  position: relative;
  background-color: #ffffff;
  width: 2620px;
  height: 363px;
  margin-left: -750px;
  margin-top: -1px;
  border: 1px solid #D9D9D9;
  justify-content: space-between;
}
.category-square :hover .category-menu {
  display: block;
  /* opacity: 1;
  visibility: visible; */
}
.menu1{
  margin-left: 750px;
  margin-top: 20px;
}
.menu1 p {
  display: flex;
  flex-direction: column; /* 수정된 부분 */
  align-items: center;
  width: 200px; /* Adjust the width as needed */
  height: 70px;
  font-size: 24px;
  font-weight: 800;
}
.menu-line1 {
  border-left: medium solid #838383;
  height: 315px;
  margin-left: 230px;
  margin-top: -350px;
}

.menu1 p {
  color: gray;
  cursor: pointer;
  transition: color 0.3s, font-weight 0.3s;
  margin: 0;
}
.menu1 p:hover {
  color: #000000;
}
.menu2-1{
  margin-left: 1010px;
  margin-top: -325px;
}
.menu2-1 p{
  display: flex;
  flex-direction: column; /* 수정된 부분 */
  align-items: center;
  width: 200px; /* Adjust the width as needed */
  height: 70px;
  font-size: 24px;
  font-weight: 800;
}
.menu2-1 p {
  /* display: none; */
  color: gray;
  cursor: pointer;
  transition: color 0.3s, font-weight 0.3s;
  margin: 0;
}
.menu2-1 p:hover {
  color: #000000;
  font-weight: bold;
}
.menu-line2-1 {
  border-left: medium solid #838383;
  height: 315px;
  margin-left: 230px;
  margin-top: -69px;
}
.menu2-1 p:hover {
  color: #000000;
  font-weight: bold;
}

.menu3-1-1 {
  margin-top: -320px;
  margin-left: 1330px;
  font-family: 'Inter', Helvetica;
  font-weight: 500;
  text-decoration-line: underline;
  cursor: pointer;
  font-size: 24px;
  letter-spacing: 0;
  line-height: normal;
  white-space: nowrap;
}
.menu3-1-2 {
  margin-left: 1530px;
  margin-top: -32px;
  font-family: 'Inter', Helvetica;
  font-weight: 500;
  text-decoration-line: underline;
  cursor: pointer;
  font-size: 24px;
  letter-spacing: 0;
  line-height: normal;
  white-space: nowrap;
}
.menu3-1-3 {
  margin-top: -320px;
  margin-left: 1330px;
  font-family: 'Inter', Helvetica;
  font-weight: 500;
  text-decoration-line: underline;
  cursor: pointer;
  font-size: 24px;
  letter-spacing: 0;
  line-height: normal;
  white-space: nowrap;
}
.menu3-1-4 {
  margin-left: 1530px;
  margin-top: -32px;
  font-family: 'Inter', Helvetica;
  font-weight: 500;
  text-decoration-line: underline;
  cursor: pointer;
  font-size: 24px;
  letter-spacing: 0;
  line-height: normal;
  white-space: nowrap;
}
.menu2-2{
  margin-left: 1010px;
  margin-top: -325px;
}
.menu2-2 p{
  display: flex;
  flex-direction: column; /* 수정된 부분 */
  align-items: center;
  width: 200px; /* Adjust the width as needed */
  height: 70px;
  font-size: 24px;
  font-weight: 800;
}
.menu2-2 p {
  /* display: none; */
  color: gray;
  cursor: pointer;
  transition: color 0.3s, font-weight 0.3s;
  margin: 0;
}
.menu-line2-2 {
  border-left: medium solid #838383;
  height: 315px;
  margin-left: 230px;
  margin-top: -139px;
}
.menu2-2 p:hover {
  color: #000000;
  font-weight: bold;
}
.menu3-2-1 {
  margin-top: -320px;
  margin-left: 1330px;
  font-family: 'Inter', Helvetica;
  font-weight: 500;
  text-decoration-line: underline;
  cursor: pointer;
  font-size: 24px;
  letter-spacing: 0;
  line-height: normal;
  white-space: nowrap;
}
.menu3-2-2 {
  margin-left: 1530px;
  margin-top: -32px;
  font-family: 'Inter', Helvetica;
  font-weight: 500;
  text-decoration-line: underline;
  cursor: pointer;
  font-size: 24px;
  letter-spacing: 0;
  line-height: normal;
  white-space: nowrap;
}
.menu3-2-3 {
  margin-top: -320px;
  margin-left: 1330px;
  font-family: 'Inter', Helvetica;
  font-weight: 500;
  text-decoration-line: underline;
  cursor: pointer;
  font-size: 24px;
  letter-spacing: 0;
  line-height: normal;
  white-space: nowrap;
}
.menu2-3{
  margin-left: 1010px;
  margin-top: -325px;
}
.menu2-3 p{
  display: flex;
  flex-direction: column; /* 수정된 부분 */
  align-items: center;
  width: 200px; /* Adjust the width as needed */
  height: 70px;
  font-size: 24px;
  font-weight: 800;
}
.menu2-3 p {
  /* display: none; */
  color: gray;
  cursor: pointer;
  transition: color 0.3s, font-weight 0.3s;
  margin: 0;
}
.menu-line2-3 {
  border-left: medium solid #838383;
  height: 315px;
  margin-left: 230px;
  margin-top: -69px;
}
.menu2-3 p:hover {
  color: #000000;
  font-weight: bold;
}
.menu3-3-1 {
  margin-top: -320px;
  margin-left: 1330px;
  font-family: 'Inter', Helvetica;
  font-weight: 500;
  text-decoration-line: underline;
  cursor: pointer;
  font-size: 24px;
  letter-spacing: 0;
  line-height: normal;
  white-space: nowrap;
}
.menu2-4{
  margin-left: 1010px;
  margin-top: -325px;
}
.menu2-4 p{
  display: flex;
  flex-direction: column; /* 수정된 부분 */
  align-items: center;
  width: 200px; /* Adjust the width as needed */
  height: 70px;
  font-size: 24px;
  font-weight: 800;
}
.menu2-4 p {
  /* display: none; */
  color: gray;
  cursor: pointer;
  transition: color 0.3s, font-weight 0.3s;
  margin: 0;
}
.menu2-4 p:hover {
  color: #000000;
  font-weight: bold;
}
.menu-line2-4 {
  border-left: medium solid #838383;
  height: 315px;
  margin-left: 230px;
  margin-top: -279px;
}
.menu3-4-1 {
  margin-top: -320px;
  margin-left: 1330px;
  font-family: 'Inter', Helvetica;
  font-weight: 500;
  text-decoration-line: underline;
  cursor: pointer;
  font-size: 24px;
  letter-spacing: 0;
  line-height: normal;
  white-space: nowrap;
}
.menu3-4-2 {
  margin-top: -320px;
  margin-left: 1330px;
  font-family: 'Inter', Helvetica;
  font-weight: 500;
  text-decoration-line: underline;
  cursor: pointer;
  font-size: 24px;
  letter-spacing: 0;
  line-height: normal;
  white-space: nowrap;
}
.menu3-4-3 {
  margin-top: -320px;
  margin-left: 1330px;
  font-family: 'Inter', Helvetica;
  font-weight: 500;
  text-decoration-line: underline;
  cursor: pointer;
  font-size: 24px;
  letter-spacing: 0;
  line-height: normal;
  white-space: nowrap;
}
.menu3-4-4 {
  margin-top: -320px;
  margin-left: 1330px;
  font-family: 'Inter', Helvetica;
  font-weight: 500;
  text-decoration-line: underline;
  cursor: pointer;
  font-size: 24px;
  letter-spacing: 0;
  line-height: normal;
  white-space: nowrap;
}
.menu2-5{
  margin-left: 1010px;
  margin-top: -325px;
}
.menu2-5 p{
  display: flex;
  flex-direction: column; /* 수정된 부분 */
  align-items: center;
  width: 200px; /* Adjust the width as needed */
  height: 70px;
  font-size: 24px;
  font-weight: 800;
}
.menu-line2-5 {
  border-left: medium solid #838383;
  height: 315px;
  margin-left: 230px;
  margin-top: -69px;
}
.menu2-5 p {
  /* display: none; */
  color: gray;
  cursor: pointer;
  transition: color 0.3s, font-weight 0.3s;
  margin: 0;
}
.menu2-5 p:hover {
  color: #000000;
  font-weight: bold;
}

.menu3-5-1 {
  margin-top: -320px;
  margin-left: 1330px;
  font-family: 'Inter', Helvetica;
  font-weight: 500;
  text-decoration-line: underline;
  cursor: pointer;
  font-size: 24px;
  letter-spacing: 0;
  line-height: normal;
  white-space: nowrap;
}
ul,
li {
  margin: 0;
  padding: 0;
  list-style: none;
  cursor: pointer;
}

a {
  color: #484848;
  font-size: 24px;
  text-decoration: none;
  cursor: pointer;
}

.menu-1 {
  text-align: center;
  margin-left:600px;
  cursor: pointer;
}

.menu-1 > ul {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  width: 1200px;
  height:78px;
}
.menu-1 > ul > li {
  position: relative;
  flex: 1;
  text-align: center;
  line-height: 80px;
  margin-right: 0;
}

.menu-1 ul > li > a {
  display: block;
  width: 100%;
  height: 100%;
  padding: 0px;
  color: #484848;
  text-decoration: none;
}

.menu-1 ul > li:hover > a {
  color: white;
  cursor: pointer;
}

.menu-1 ul > li:hover {
  background-color: #FF3D3D;
  cursor: pointer;
}

.menu-1 ul > li {
  position: relative;
  cursor: pointer;
  width: 200px;
  border-radius: 3px;
}

.menu-1 ul {
  background-color: #FFBBBB;
  cursor: pointer;
  border-radius: 3px;
}

.menu-1 ul ul {
  list-style: none;
  padding: 0;
  margin: 0;
  position: absolute;
  top: 100%;
  left: 0;
  display: none;
  background-color: #FFF2F2;
  border-radius: 3px;
}

.menu-1 ul li:hover > ul {
  display: block;
}

.menu-1 ul ul ul {
  margin-top: -83px;
  margin-left: 200px;
  cursor: pointer;
}
.menu-1 ul ul li {
  margin: 0;
}

.menu-1 ul ul li a {
  text-decoration: none;
  color: #000;
}
.header-user-profile{
  width: 55px;
  margin-left: 190px;
}
.profile-menu {
  position: absolute;
  top: 50px;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 10px;
  width: 200px;
  padding: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-top: 28px;
  margin-left: -10px;
  z-index: 1000;
}
.profile-header {
  display: flex;
  align-items: center;
}
.header-profile-picture {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}

.header-username {
  font-weight: bold;
  font-size: 20px;
}

.menu-options {
  margin-top: 10px;
}

.menu-options a {
  display: block;
  text-decoration: none;
  color: black;
 
  border-bottom: 1px solid #eee;
  font-size: 20px;
}
#logout{
  margin-left: 40px;
}
#mypage:hover{
  font-weight:bold;
}
#logout:hover{
  font-weight: bold;
}
.user_head{
  width: 150px;
  height: 94px;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
}
.header-profile-icon{
  width: 55px;
  height: 55px;
  border-radius: 50%;
  margin-top: 20px;
}
.head-username{
  display:inline-block;
  width: 300px;
  max-width: 400px;
  overflow: hidden;
  font-size:25px;
  font-weight: bold;
  line-height: 94px;
  text-align: center;
}
nav.menu-1 {
  display: flex;
  justify-content: center; 
  align-items: center; 
  width: 100%;
  margin-left: 12px;
}

nav.menu-1 ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
  display: flex;
  justify-content: center;
}
nav.menu-1 ul li ul {
  position: absolute;
  display: none;
  list-style-type: none;
  padding: 0;
}

nav.menu-1 ul li ul li {
  display: block;
  margin: 0;
}
</style>