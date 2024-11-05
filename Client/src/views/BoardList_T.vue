<template>
  <RouterView :key="$route.fullPath"></RouterView>
  <div class="center-wrap">
    <div class="wrapper_left">
      <div class="leftlayout">
        <div class="menubar">
          <div class="topicmenu">게시글</div>
          <div class="hovermenu" @click="linkToBoard('main')">전체게시글</div>
          <div class="submenu" @click="linkToBoard('err')">- 오류코드</div>
          <div class="submenu" @click="linkToBoard('qna')">- 질의응답</div>
          <div class="submenu" @click="linkToBoard('opensrc')">- 오픈소스</div>
          <div class="submenu" @click="linkToBoard('help')">- 협업모집</div>
        </div>
      </div>
    </div>
    <div class="wrapper_right">
      <div class="titlebar">
        <div class="titletext">{{ boardTitle }}</div>
        <button class="wrtbtn" @click="navigateToWrite">글쓰기</button>
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
          <tr v-for="(post, index) in paginatedPosts" :key="index"  @click="getPostDetail(post.id)">
            <th class="num1">{{ post.num +1 }}</th>
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
        <select class="combobox">
          <option value="all">전체</option>
          <option value="err">오류코드</option>
          <option value="qna">질의응답</option>
          <option value="opensource">오픈소스</option>
          <option value="help">협업모집</option>
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
      boardTitle: '전체게시글',
      currentPage: 1,
      totalPages: 0,
      postsPerPage: 10,
    };
  },
  props: {
    msg: String
  },
  created() {
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
    getPostDetail(postid) {
      this.$router.push({ name: 'PostDetail', params: { id: postid } });
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
    loadBoardList() {
      const cate = this.$route.params.category;
      var cateKR = null;
      if (cate == 'main') {
        this.getBoardList();
      } else {
        switch(cate) {
          case 'err': cateKR = "오류코드"; break;
          case 'qna': cateKR = "질의응답"; break;
          case 'opensrc': cateKR = "오픈소스"; break;
          case 'help': cateKR = "협업모집"; break;
        }
        this.getBoardListByCategory(cateKR);
        this.currentPage = 1;
      }
    },
    getBoardList() {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/board/get-board-list.do`)
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
        .catch((error) => {
          console.error(error);
        });
    },
    getBoardListByCategory(category) {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/board/get-board-list-category.do?cate=${category}`)
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
        .catch((error) => {
          console.error(error);
        });
    },
    navigateToWrite() {
      this.$router.push({ path: '/post/write' });
    },
    goToPage(page) {
      this.currentPage = page;
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
};
</script>

<style scoped>
.center-wrap {
	width: 1810px;
	margin: 0 auto;
}
.leftlayout{
  float: left;
}
.rightlayout{
  float: left;
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

.wrtbtn {
  background-color: #FAE9E9;
  width: 140px;
  height: 50px;
  transition: background-color 0.2s;
  border: 1px solid #888686;
  font-size: 18px;
}

.wrtbtn:hover {
  background-color: #FFBBBB;
  cursor: pointer;
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

.pagebtn:hover {
    background-color: #f7afab;
}
</style>