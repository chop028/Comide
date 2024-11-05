<template>
<RouterView></RouterView>
<div class="mypage_wrap">
    <div class="mypage_menubar">
        <div class="mypage_topic">마이페이지</div>
        <div class="mypage_sub" @click="linkToinfo">회원정보</div>
        <div class="mypage_sub" @click="linkToact" id="nowpage">활동내역</div>
        <div class="mypage_sub" @click="linkToquiz">내 퀴즈</div>
        <div class="mypage_sub" @click="linkToAlarm">알림</div>
    </div>
  <div class="mypage_right">
    <div class="Activity">활동내역</div>
    <div class="act_line1"></div>
    <div class="activity_menuwrap">
      <div class="post_activity_btn" v-bind:style="post_btn" ref="post_activity_btn" @click="trans_post" @mouseover="over_post" @mouseleave="leave_post">작성 글 보기</div>
      <div class="comment_activity_btn" v-bind:style="comment_btn" ref="comment_activity_btn" @click="trans_comment" @mouseover="over_comment" @mouseleave="leave_comment">작성 댓글 보기</div>
    </div>
    <div class="activity_postpage" v-if="show_post">
      <div class="postpage_headwrap">
        <input class="form-check-input-all" type="checkbox" @change="toggleSelectAllPost" :checked="isAllPostSelected">
        <div class="checkall_text">전체선택</div>
        <div class="delete_btn" @click="deletePost">삭제</div>
      </div>
      <div class="activity_headline"></div>
      <div class="activity_postlist">
        <ul>
          <li v-for="(post, index) in paginatedPosts" :key="index" class="activity_post_item">
            <input class="form-check-input" type="checkbox" :value="post" :id="'flexCheckDefault' + index" v-model="selectedPost">
            <div class="activity_post_content">
              <div class="activity_post_posttitle" @click="getPostDetail(post.id)">{{ post.title }}</div>
              <div class="activity_post_num">[{{ post.viewCnt }}]</div>
              <div class="activity_post_wrap2">
                <div class="activity_post_category">{{ post.category }}</div>
                <div class="activity_post_wall"></div>
                <div class="activity_post_time">{{ post.created_date }}</div>
              </div>
              <div class="activity_post_line"></div>
            </div>
          </li>
        </ul>
    </div>
    <div class="activity_total">total: {{ totalPages }}, page {{ currentPage }}/{{ totalPages }}</div>
        <div class="page_num">
          <button class="pagebtn back" @click="prevPageGroup(true)"><img src="@/assets/arrow-left-short.svg"></button>
          <button v-for="page in displayedPages" :key="page" class="pagebtn" @click="goToPage(page, true)">{{ page }}</button>
          <button class="pagebtn next" @click="nextPageGroup(true)"><img src="@/assets/arrow-right-short.svg"></button>
        </div>
          <div class="searchbox">  
            <select class="combobox" >
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
  <div class="activity_commentpage" v-if="show_comment">
      <div class="commentpage_headwrap">
        <input class="form-check-input-all" type="checkbox" @change="toggleSelectAllComment" :checked="isAllCommentSelected">
        <div class="checkall_text">전체선택</div>
        <div class="delete_btn">삭제</div>
      </div>
      <div class="activity_headline"></div>
      <div class="activity_commentlist">
        <ul style="margin: 0; padding: 0;">
          <li v-for="(comment, index) in paginatedComments" :key="index" class="activity_comment_item" style="border-bottom: 2px solid #FAE9E9;">
            <input class="form-check-input" type="checkbox" :value="comment" :id="'flexCheckDefault' + index" v-model="selectedComment">
            <div class="activity_comment_content">
              <div class="activity_comment_text" @click="getPostDetail(comment.userData.id)">{{ comment.postData.title }}</div>
              <div class="activity_comment_time">{{ comment.createdTime }}</div>
              <div class="activity_comment_posttitle">{{ comment.content }}</div>
              <div class="activity_comment_num">{{ comment.commentNum }}</div>
            </div>
          </li>
        </ul>
    </div>
    <div class="activity_total" id="total2">total: {{ totalPagesC }}, page {{ currentPageC }}/{{ totalPagesC }}</div>
    <div class="page_num">
      <button class="pagebtn back" @click="prevPageGroup(false)"><img src="@/assets/arrow-left-short.svg"></button>
      <button v-for="page in displayedPagesC" :key="page" class="pagebtn" @click="goToPage(page, false)">{{ page }}</button>
      <button class="pagebtn next" @click="nextPageGroup(false)"><img src="@/assets/arrow-right-short.svg"></button>
    </div>
      <div class="searchbox">  
        <select class="combobox" >
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
 </div>
</template>

<script>
import apiClient from '@/axios/axios';
import { useAuthStore } from '@/store/auth';

export default {
  methods: {
    deletePost() {
      if (this.selectedPost.length === 0) {
        alert("삭제할 게시글을 선택하세요.");
        return;
      }

      this.selectedPost.forEach(post => {
        apiClient
          .delete(`${process.env.VUE_APP_API_URL}/api/board/delete-board-content?boardId=${post.id}`)
          .then(response => {
            if (response.data.success) {
              this.getBoardList(); // 게시글 목록을 새로 불러옴
            } else {
              alert("게시글 삭제에 실패했습니다.");
            }
          })
          .catch(error => {
            console.error('게시글 삭제 중 오류 발생:', error);
          });
      });

      alert("선택된 게시글이 삭제되었습니다.");
    },
    getPostDetail(postid) {
      this.$router.push({ name: 'PostDetail', params: { id: postid } });
    },
    fetchPostData(item) {
      apiClient
            .get(`${process.env.VUE_APP_API_URL}/api/board/get-post-detail.do?id=${item.postId}`)
            .then(response => {
                item.postData = response.data.result;
            })
            .catch(error => {
                console.error('게시물 데이터를 가져오는 중 오류 발생:', error);
            });
    },
    loadComment() {
      apiClient
            .get(`${process.env.VUE_APP_API_URL}/api/comment/get-cmt-list-user.do?userId=${this.userId}`)
            .then((response) => {
            if (response.data.success) {
                const sortedComments = response.data.result.map((comment, index) => ({
                ...comment,
                num: index,
                postData: null,
              }));
              this.comment = sortedComments.sort((a, b) => b.num - a.num);
              this.totalPagesC = Math.ceil(this.comment.length / this.postsPerPageC);
              this.comment.forEach(item => this.fetchPostData(item));
            } else {
                console.error("댓글 불러오기 실패!");
            }
            })
            .catch(function(error) {
            console.error(error);
            });
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
    getBoardList() {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/board/get-board-list-user.do?userId=${this.userId}`)
        .then((response) => {
          if (response.data.success) {
            const sortedPosts = response.data.result.map((post, index) => ({
              ...post,
              num: index
            }));
            this.post = sortedPosts.sort((a, b) => b.num - a.num);
            this.totalPages = Math.ceil(this.post.length / this.postsPerPage);
            this.loadComment();
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
     trans_post(){
      this.show_post = true;
      this.show_comment = false;
      this.postActivityHoverActive = false;
      this.commentActivityHoverActive = true;
      this.post_btn['background-color']='#FFBEBA';
      this.post_btn['border']='1px solid #FFBEBA';
      this.post_btn['color']='black';
      this.comment_btn['background-color']='#FFFFFF';
      this.comment_btn['border']='1px solid #B7B7B7';
      this.comment_btn['color']='#898989';
    },
    trans_comment(){
      this.show_post = false;
      this.show_comment = true;
      this.postActivityHoverActive = true;
      this.commentActivityHoverActive = false;
      this.post_btn['background-color']='#FFFFFF';
      this.post_btn['border']='1px solid #B7B7B7';
      this.post_btn['color']='#898989';
      this.comment_btn['background-color']='#FFBEBA';
      this.comment_btn['border']='1px solid #FFBEBA';
      this.comment_btn['color']='black';
    },
    over_post(){
    if(this.postActivityHoverActive) {
      this.post_btn['background-color']='#FFBEBA';
      }
    },
    leave_post(){
    if(this.postActivityHoverActive) {
      this.post_btn['background-color']='#FFFFFF';
      }
    },
    over_comment(){
    if(this.commentActivityHoverActive) {
      this.comment_btn['background-color']='#FFBEBA';
      }
    },
    leave_comment(){
    if(this.commentActivityHoverActive) {
      this.comment_btn['background-color']='#FFFFFF';
      }
    },
    toggleSelectAllPost() {
      if (this.isAllPostSelected) {
        this.selectedPost = [];
      } else {
        this.selectedPost = [...this.post];
      }
    },
    toggleSelectAllComment() {
      if (this.isAllCommentSelected) {
        this.selectedComment = [];
      } else {
        this.selectedComment = [...this.comment];
      }
    },
  },
  data() {
    const authStore = useAuthStore();
    return {
      show_post: true,
      show_comment: false,
      post_btn: {
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
    comment_btn: {
      width: '142px',
      height: '46px',
      'border-radius': '4px',
      border: '1px solid #B7B7B7',
      'background-color': '#FFFFFF',
      color: '#898989',
      'font-size': '20px',
      'text-align': 'center',
      'line-height': '46px',
      cursor: 'pointer',
      'z-index': '1',
      position: 'relative',
      'margin-left': '18px'
    },
      post: [],
      comment: [],
      selectedPost: [],
      selectedComment: [],
      currentPage: 1,
      totalPages: 0,
      postsPerPage: 4,
      currentPageC: 1,
      totalPagesC: 0,
      postsPerPageC: 4,
      userId: authStore.userInfo?.id || '',
    };
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
    displayedPagesC() {
      const start = Math.floor((this.currentPageC - 1) / 10) * 10 + 1;
      const end = Math.min(start + 9, this.totalPagesC);
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
    paginatedComments() {
      const startIndex = (this.currentPageC - 1) * this.postsPerPageC;
      const endIndex = startIndex + this.postsPerPageC;
      return this.comment.slice(startIndex, endIndex);
    },
    isAllPostSelected() {
      return this.selectedPost.length === this.post.length;
    },
    isAllCommentSelected() {
      return this.selectedComment.length === this.comment.length;
    },
  },
  mounted(){
    this.getBoardList();
  }
};
</script>

<style scoped>
.activity_comment_text:hover{
  font-weight: bold;
  cursor: pointer;
}
.activity_post_posttitle:hover{
  font-weight: bold;
  cursor: pointer;
}
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
.Activity{
  font-size: 32px;
  color: black;
  margin-top: 220px;
  margin-left: 140px;
}
.act_line1{
  width: 1199px;
  border: 3px solid #FFBEBA;
  margin-left: 125px;
  margin-top: 20px;
}
.activity_menuwrap{
  display:flex;
  align-content : space-between;
  width:321px;
  height:100px;
  margin-left: 125px;
  margin-top: 20px; 
}
.activity_postpage{
  width:1199px;
  border-radius: 8px;
  background-color: #FFFFFF;
  border: 1px solid #FFBEBA;
  box-shadow: inset 0px -1px 1px 1px rgba(0, 0, 0, 0.5);
  margin-top: -60px;
  margin-left: 125px;
  z-index: 2;
  position: relative;
}
.postpage_headwrap{
  display: flex;
  align-content: space-between;
  width: 1034px;
  height: 50px;
  margin-left: 80px;
  margin-top: 35px;
}
.form-check-input-all{
  width: 30px;
  height: 30px;
  border-radius: 6px;
  border: solid 1px #9F9F9F;
  cursor: pointer;
}
.checkall_text{
  color:black;
  width: 83px;
  height: 37px;
  font-size:20px;
  margin-left: 15px;
}
.delete_btn{
  background-color: #FFA0A0;
  width: 83px;
  height: 35px;
  border: none;
  border-radius: 10px;
  transition: background-color 0.2s;
  font-size: 18px;
  font-weight: 800;
  color: white;
  text-align: center;
  box-shadow: 0 5px 5px #888686;
  margin-left: 820px;
  margin-top: -5px;
  cursor: pointer;
  line-height: 35px;
}
.delete_btn:hover{
    background-color: #f98585;
}
.checkall.clicked{
  background-color: #000000;
}
.activity_headline{
  width: 1030px;
  border: 1px solid #FAE9E9;
  margin-left: 80px;
  margin-top: 20px; 
}
.activity_commentpage{
  width:1199px;
  border-radius: 8px;
  background-color: #FFFFFF;
  border: 1px solid #FFBEBA;
  box-shadow: inset 0px -1px 1px 1px rgba(0, 0, 0, 0.5);
  margin-top: -60px;
  margin-left: 125px;
  z-index: 2;
  position: relative;
}
.activity_postlist{
  margin-left: 80px;
  overflow: hidden;
}
.form-check-input{
  width: 30px;
  height: 30px;
  border-radius: 6px;
  border: solid 1px #9F9F9F;
  cursor: pointer;
  margin-top: 30px;
}
.activity_post_item{
  padding: 0 0 0 20px;
  display: flex;
  align-content: space-between;
  width: 1034px;
  height: 90px;
}
.activity_post_posttitle{
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
.activity_post_num{
  width: auto;
  max-width: 20px;
  overflow: hidden;
  height: 37px;
  max-width: 200px;
  display: inline-block;
  font-size: 20px;
  color: #FF0000;
  margin-top: 15px;
  margin-left: 5px;
}
.activity_post_wrap2{
  display: flex;
  align-content: space-between;
  width: 1034px;
  height: 30px;
  margin-left: -33px;
}
.activity_post_category{
  width: 62px;
  height: 15px;
  font-size: 15px;
  color: black;
  margin-left: 53px;
}
.activity_post_wall{
  width: 5px;
  height: 25px;
  background-color: #FFBEBA;
  border: 3px solid #FFBEBA;
  margin-left: 3px;
}
.activity_post_time{
  width: 100px;
  height: 20px;
  font-size: 16px;
  color: #0000008c;
  margin-left: 3px;
}
.activity_post_line{
  width: 1030px;
  height: 1px;
  border: 1px solid #FAE9E9;
  margin-left: -30px;
}
.clicked {
  background-color: black;
}
.activity_total{
  color: #000000;
  font-size: 16px;
  margin: auto;
  text-align: center;
}
.page_num{
  margin: auto;
  text-align: center;
  margin-top: 10px;
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
  height: 80px;
  font-size: 18px;
  margin-bottom: 20px;
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
.commentpage_headwrap{
  display: flex;
  align-content: space-between;
  width: 1034px;
  height: 50px;
  margin-left: 80px;
  margin-top: 35px;
}
.activity_commentlist{
  margin-left: 80px;
  overflow: hidden;
}
.activity_comment_item{
  display: flex;
  align-content: space-between;
  width: 1034px;
  height: 100px;
}
.activity_comment_btn{
  width: 30px;
  height: 30px;
  border-radius: 6px;
  background-color: white;
  border: solid 1px #9F9F9F;
  cursor: pointer;
  margin-top: 30px;
}
.activity_comment_text{
  width: auto;
  max-width: 940px;
  overflow: hidden;
  height: 25px;
  max-width: 700px;
  display: inline-block;
  font-size: 20px;
  color: black;
  margin-top: 15px;
  margin-left: 30px;
}
.activity_comment_time{ 
  height: 20px;
  font-size: 16px;
  color: #0000008c;
  margin-left: 30px;
}
.activity_comment_posttitle{
  width: auto;
  max-width: 940px;
  overflow: hidden;
  height: 37px;
  max-width: 700px;
  display: inline-block;
  font-size: 16px;
  color: #0000008c;
  margin-left: 30px;
}
.activity_comment_num{
  width: auto;
  max-width: 20px;
  overflow: hidden;
  height: 37px;
  max-width: 200px;
  display: inline-block;
  font-size: 20px;
  color: #FF0000;
}
.ul {
  padding: 20px;
}
#total2{
  margin-top: 10px;
}
</style>