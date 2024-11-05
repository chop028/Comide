<template>
  <RouterView :key="$route.fullPath"></RouterView>
  <div class="developint_wrapper">
    <div class="developint_menubar">
            <div class="develop_topicmenu">개발지식</div>
            <div class="develop_lan_hovermenu" @click="linkToDevelopInt('main')">언어지식</div>
            <div class="develop_submenu_front">- 웹/프론트엔드</div>
            <div class="develop_submenu" @click="linkToDevelopInt('html')">• HTML/CSS</div>
            <div class="develop_submenu" @click="linkToDevelopInt('js')">• JS</div>
            <div class="develop_submenu" @click="linkToDevelopInt('react')">• React</div>
            <div class="develop_submenu" @click="linkToDevelopInt('vue')">• Vue</div>
            <div class="develop_submenu_back">- 서버/백엔드</div>
            <div class="develop_submenu" @click="linkToDevelopInt('java')">• JAVA</div>
            <div class="develop_submenu" @click="linkToDevelopInt('python')">• Python</div>
            <div class="develop_submenu" @click="linkToDevelopInt('cs')">• C#</div>
            <div class="develop_submenu" @click="linkToDevelopInt('spring')">• Spring</div>    
    </div>
    <div class="wrapper_right">
        <div class="titlebar">
        <div class="titletext">{{ developintTitle }}</div>
    </div>
      <table class="table">
        <thead>
          <tr>
            <th class="top-no">No</th>
            <th class="top-sub">Subject</th>
            <th class="top-title">Title</th>
            <th class="top-time">Time</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(post, index) in paginatedPosts" :key="index" @click="getPostDetail(post.id)">
            <th class="num1">{{ post.num + 1 }}</th>
            <td class="num2-1">{{ post.category }}</td>
            <td class="num2-2">{{ post.title }}</td>
            <td class="num2">{{ post.created_date }}</td>
          </tr>
        </tbody>
      </table>
      <div class="page_num">
          <button class="pagebtn back" @click="prevPageGroup"><img src="@/assets/arrow-left-short.svg"></button>
          <button v-for="page in displayedPages" :key="page" class="pagebtn" @click="goToPage(page)">{{ page }}</button>
          <button class="pagebtn next" @click="nextPageGroup"><img src="@/assets/arrow-right-short.svg"></button>
      </div>
      <div class="searchbox">  
          <select class="combobox" >
            <option value="all">전체</option>
            <option value="front">웹/프론트엔드</option>
            <option value="back">서버/백엔드</option>
         </select>
          <input class="searchbar" method="get" type="text" placeholder="검색어를 입력하세요">
          <button class="srcbtn">검색</button>
      </div>
      </div>
    </div>
  </template>
  
  <script>
import apiClient from '@/axios/axios';

  export default {
    data() {
      return {
          post: [],
          developintTitle: '언어지식',
          currentPage: 1,
          totalPages: 0,
          postsPerPage: 10,
      };
    },
    created(){
      this.loadBoardList();
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
    }
  },
  methods: {
      linkToDevelopInt(intcate){
        this.$router.push({ name: 'developint', params: { category: intcate } });
        switch(intcate) {
        case 'main': 
          this.developintTitle = '언어지식';
          break;
        case 'front':
          this.developintTitle = '웹/프론트엔드';
          break;
        case 'html':
          this.developintTitle = 'HTML/CSS';
          break;
        case 'js':
          this.developintTitle = 'JS';
          break;
        case 'react':
          this.developintTitle = 'React';
          break;
        case 'vue':
          this.developintTitle = 'Vue';
          break;
        case 'back':
          this.developintTitle = '서버/백엔드';
          break;
        case 'java':
          this.developintTitle = 'JAVA';
          break;
        case 'python':
          this.developintTitle = 'Python';
          break;
        case 'cs':
          this.developintTitle = 'C#';
          break;
        case 'spring':
          this.developintTitle = 'Spring';
          break;
        }
      },
      linkToDevelopInfor(){
        this.$router.push({ name: 'developinfor', params: { category: "develop" } });
      },
      linkToDevelopInforUpload(){
        this.$router.push({ name: 'developinforupload', params: { category: "develop" } });
      },
      getPostDetail(postid) {
          this.$router.push({ name: 'LangKnwlDetail', params: { id: postid } });
      },
      writeLangKnwl(){
          this.$router.push({ name: 'writeLangKnwl' });
      },
      loadBoardList() {
        const cate = this.$route.params.category;
        if(cate=='main') {
          this.getBoardList();
        } else{
          this.getBoardListByCategory(cate);
          this.currentPage = 1;
        }
      },
      getBoardList() {
        apiClient
              .get(`${process.env.VUE_APP_API_URL}/api/LangKnwl/get-langKnwl-list.do`)
              .then((response) => {
              if (response.data.success) {
                  const sortedPosts = response.data.result.map((post, index) => ({
                  ...post,
                  num: index
                  }));
                  this.post = sortedPosts.sort((a, b) => b.num - a.num);
                  this.totalPages = Math.ceil(this.post.length / this.postsPerPage);
              } else {
                  console.error("get-board-list failed");
              }
              })
              .catch(function(error) {
                  console.error(error);
              });
      },
      getBoardListByCategory(category) {
        apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/LangKnwl/get-langKnwl-list-category.do?category=${category}`)
        .then((response) => {
          if (response.data.success) {
            const sortedPosts = response.data.result.map((post, index) => ({
              ...post,
              num: index
            }));
            this.post = sortedPosts.sort((a, b) => b.num - a.num);
            this.totalPages = Math.ceil(this.post.length / this.postsPerPage);
          } else {
            console.error("get-board-list failed");
          }
        })
        .catch(function(error) {
          console.error(error);
        });
      },
      goToPage(page) {
      this.currentPage = page;
      // Load the posts for the current page (if pagination is server-side)
      // this.loadBoardList();
      },
      prevPageGroup() {
      if (this.displayedPages[0] > 1) {
          this.currentPage = this.displayedPages[0] - 1;
      }
      },
      nextPageGroup() {
      if (this.displayedPages[this.displayedPages.length - 1] < this.totalPages) {
          this.currentPage = this.displayedPages[this.displayedPages.length - 1] + 1;
      }
      }
    },
    watch: {
      '$route.params.category'(newCategory, oldCategory) {
        if (newCategory !== oldCategory) {
          this.loadBoardList();
        }
      }
    }
  }
  </script>
  
  <style scoped>
  
  .wrtbtn {
  background-color: #FAE9E9;
  width: 140px;
  height: 50px;
  transition: background-color 0.2s;
  border: 1px solid #888686;
  font-size: 18px;
  }
  .developint_wrapper{
      width: 1810px;
    height: 3000px;
      margin: 0 auto;
  }
  .wrapper_right{
    width: 1710px;
    height: 1100px;
  }
  .developint_menubar{
      float: left;
      margin-left: 100px;
      width: 200px;
      height: 600px;
  }
  .develop_topicmenu {
      width: 200px;
      height: 80px;
      font-size: 45px;
      border-bottom: 2px solid #888686;
  }
  
  .develop_lan_hovermenu {
      width: 200px;
      text-align: left;
      font-size: 25px;
      font-weight: bold;
      margin-top: 10px;
      transition: color 0.2s, font-weight 0.2s;
  }
  .develop_infor_hovermenu {
      width: 200px;
      text-align: left;
      font-size: 25px;
      margin-top: 15px;
      color: #888686;
      transition: color 0.2s, font-weight 0.2s;
  }
  
  .develop_lan_hovermenu:hover,
  .develop_submenu_front:hover,
  .develop_submenu_back:hover,
  .develop_submenu:hover,
  .develop_infor_hovermenu:hover,
  .develop_submenu_infor:hover,
  .develop_submenu_upload:hover{
      color: black;
      font-weight: bold;
      cursor: pointer;
  }
  
  .develop_submenu {
      width: 250px;
      font-size: 22px;
      padding-left: 50px;
      line-height: 50px;
      color: #888686;
      transition: color 0.2s, font-weight 0.2s;
  }
  .develop_submenu_front {
      width: 250px;
      font-size: 25px;
      padding-top: 10px;
      padding-left: 30px;
      line-height: 50px;
      color: #888686;
      transition: color 0.2s, font-weight 0.2s;
  }
  .develop_submenu_back {
      width: 200px;
      height: 50px;
      font-size: 25px;
      padding-top: 10px;
      padding-left: 30px;
      line-height: 50px;
      color: #888686;
      transition: color 0.2s, font-weight 0.2s;
  }
  .develop_submenu_infor {
      width: 200px;
      height: 50px;
      font-size: 25px;
      padding-top: 10px;
      padding-left: 30px;
      line-height: 50px;
      color: #888686;
      transition: color 0.2s, font-weight 0.2s;
  }
  .develop_submenu_upload {
      width: 200px;
      height: 50px;
      font-size: 25px;
      padding-top: 10px;
      padding-left: 30px;
      line-height: 50px;
      color: #888686;
      transition: color 0.2s, font-weight 0.2s;
  }
  .titlebar {
    width: 1210px;
    height: 75px;
    margin-left: 500px;
    margin-top: 230px;
    border-bottom: 2px solid #888686;
    display: flex;
    justify-content: space-between;
  }
  .titletext {
    font-size: 45px;
  }
  .menubar {
    width: 200px;
    height: 400px;
    margin-left: 150px;
  }
  .topicmenu {
    width: 200px;
    height: 78px;
    font-size: 45px;
    border-bottom: 2px solid #888686;
  }
  
  .hovermenu {
    width: 200px;
    text-align: left;
    font-size: 24px;
    margin-top: 8px;
    color: #888686;
    transition: color 0.2s, font-weight 0.2s;
  }
  
  .hovermenu:hover,
  .submenu:hover {
    color: black;
    font-weight: bold;
    cursor: pointer;
  }
  
  .submenu {
    width: 200px;
    font-size: 22px;
    padding-top: 10px;
    padding-left: 30px;
    line-height: 50px;
    color: #888686;
    transition: color 0.2s, font-weight 0.2s;
  }
  
  .table {
    width: 1210px;
    text-align: center;
    margin-top: 15px;
    margin-left: 500px;
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
  .top-sub {
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
  .page_num{
  margin-left: 500px;
  padding-top: 10px;
  text-align: center;
  width: 1210px;
  height: 75px;
  }
  .pagebtn{
    margin-right: 10px;
    width: 40px;
    height: 40px;
    border: 1px solid;
    background-color: #FFBEBA;
  }
  .pagebtn:hover{
    background-color: #f7afab;
  }
  .searchbox {
    width: 1210px;
    height: 100px;
    margin-left: 500px;
    background-color: #FAE9E9;
    margin-bottom: 1000px;
    border: 1.5px solid #888686;
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
  </style>