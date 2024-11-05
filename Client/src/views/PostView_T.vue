<template>
  <RouterView></RouterView>
  <div class="view_wrapper">
    <div class="menubar">
      <div class="topicmenu">게시글</div>
      <div class="hovermenu" @click="linkToBoard('main')">전체게시글</div>
      <div class="submenu" @click="linkToBoard('err')">- 오류코드</div>
      <div class="submenu" @click="linkToBoard('qna')">- 질의응답</div>
      <div class="submenu" @click="linkToBoard('opensrc')">- 오픈소스</div>
      <div class="submenu" @click="linkToBoard('help')">- 협업모집</div>
    </div>
    <div class="view_right_layout">
      <div class="post_titlebox">
        <div class="title_text">{{ title }}</div>
      </div>
      <div v-if="isHelp && !appliedbtn" class="applybox">
        <button class="applybtn" @click="openApplyModal"><span style="color: white;">지원하기</span></button>
      </div>
      <div v-if="isHelp && appliedbtn" class="appliedbox">
        <button class="appliedbtn"><span style="color: white;">지원완료</span></button>
      </div>
  
      <div class="tagbox">
        <div class="hashtag" v-for="hashtag in hashTags" :key="hashtag">{{ "#" + hashtag.content }}</div>
      </div>
      <div class="post_data_box">
        <div v-if="isHelp" class="application-num">{{ currentApplicants }} / {{ maxApplicants }}</div>
        <div v-if="!isHelp" class="application-num"></div>
        <div class="writer">{{ userName }}</div>
        <div class="time-line">{{ createdDate }}</div>
        <button class="friend-plusbtn" @click="sendFriendRequest">친구 신청</button>
      </div>
      <div class="post_content">
        <!-- 게시글 내용 및 코드 번갈아 표시 -->
        <div v-for="(item, index) in postDataList" :key="index" style="margin-bottom: 15px;">
          <div v-if="item.category === 'content'" class="content-container">
            <div class="post-text" v-html="formatContent(item.content)"></div>
          </div>
          <div v-else-if="item.category === 'code'" style="display: flex; align-items: flex-end;">
            <DraggableComponent :codeId="item.codeId"/>
            <img class="codeblock-btn" src="../assets/code_blocks.svg" @click="linkToCodeEditor(item.codeId)">
          </div>
        </div>
                
        <div class="heartbox">
          <button class="heartbtn" @click="toggleLikes">
            <img v-if="likes === 0" src="../assets/heart.svg" style="width: 20px; height: 20px; margin-right: 15px;">
            <img v-else src="../assets/heart-fill.svg" style="width: 20px; height: 20px; margin-right: 15px;">
            <span>{{ likes }}</span>
          </button>
        </div>
      </div>
      <div class="commentbox">
        <div class="comment_text">{{ commentCount }}개의 댓글</div>
        <textarea v-model="cmtInput" class="comment_textbox" method="get" type="text" placeholder="댓글을 작성하세요"></textarea>
        <div class="submitbox">
          <button class="submitbtn" @click="writeComment">댓글 작성</button>
        </div>
      </div>
      <div class="comment_content">
        <div v-for="(comment, index) in commentList" :key="index" style="margin-top: 30px;">
          <div class="user-box">
            <div class="profile-box"><img :src="comment.userData.picture" style="width: 70px; height: 70px; border-radius: 70%;"></div>
            <div class="userinform">
              <div class="user-name">{{ comment.userData.name }}</div>
              <div class="timeline">{{ comment.cmtData.createdTime }}</div>
            </div>
          </div>
          <div class="content-box">
            {{ comment.cmtData.content }}
          </div>
          <button class="plus-comment">+ 답글 달기</button>
        </div>
      </div>
    </div>
  </div>
  <DriveModal v-if="driveModalVisible" @close="driveModalVisible = false" :postData="postData"></DriveModal>
  <ApplyModal v-if="ApplyModalVisible" @close="ApplyModalVisible = false" @applicationSubmitted="changingbtn" :userId="userId" :groupId="groupId"></ApplyModal>
</template>

<script>
import apiClient from '@/axios/axios';
import moment from 'moment';
import DraggableComponent from '@/views/DropBox.vue';
import ApplyModal from '@/views/ApplyModal.vue';
import DriveModal from '@/views/DriveModal_T.vue';
import { useAuthStore } from '@/store/auth';
import emitter from '@/eventBus/eventBus.js';

export default {
  components: {
    DraggableComponent,
    ApplyModal,
    DriveModal
  },
  data() {
    const authStore = useAuthStore();
    return {
      isHelp: false,
      likes: 0,
      commentList: [],
      maxApplicants: 4,
      currentApplicants: 1,
      author: 'User1',
      commenter: 'User11',
      driveModalVisible: false,
      showDraggableComponent: true,
      ApplyModalVisible: false,
      appliedbtn: false,
      userId: authStore.userInfo?.id || '',
      groupId: "",
      postContent: '',
      writer_id: null,
      postDataList: [],
      hashTags: [],
      userName: null,
      createdDate: null,
      postData: null,
      cmtInput: '', // cmtInput 속성 추가
      title: '' // title 속성 추가
    };
  },
  methods: {
    sendFriendRequest() {
      const senderId = this.userId; // 현재 로그인한 사용자 ID
      const receiverId = this.writer_id; // 게시물 작성자 ID
      
      apiClient.post(`${process.env.VUE_APP_API_URL}/api/friendship/request?senderId=${senderId}&receiverId=${receiverId}`)
        .then(response => {
          if (response.data) {
            alert('친구 요청이 성공적으로 전송되었습니다.');
          } else {
            alert('친구 요청을 보낼 수 없습니다.');
          }
        })
        .catch(error => {
          console.error('친구 요청 전송 중 오류 발생:', error);
        });
    },
    formatContent(content) {
      if (!content) return '';
      // Replace tabs with 4 non-breaking spaces
      let formatted = content.replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;');
      // Replace newlines with <br> tags
      formatted = formatted.replace(/\n/g, '<br>');
      return formatted;
    },
    toggleLikes() {
      this.likes = 1 - this.likes;
    },
    fetchPostData(id) {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/board/get-post-detail.do?id=${id}`)
        .then(response => {
          this.getBoardContent(id);
          this.getHashTag(id);
          const postData = response.data.result;
          if (postData.category == "협업모집" && postData.userId != this.userId) {
            this.isHelp = true;
          }
          this.title = postData.title;
          this.content = postData.content;
          this.postContent = postData.content;
          this.createdDate = postData.created_date;
          this.groupId = postData.groupId;
          this.writer_id = postData.userId;
          this.loadUserName();
          this.loadComment();
          this.loadPostData();
        })
        .catch(error => {
          console.error('게시물 데이터를 가져오는 중 오류 발생:', error);
        });
    },
    getHashTag(boardId) {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/board/get-hashTag.do?boardId=${boardId}`)
        .then(response => {
          if (response.data.success) {
            this.hashTags = response.data.result;
          } else {
            console.error("댓글 작성 실패");
          }
        })
        .catch(error => {
          console.error('게시물 데이터를 가져오는 중 오류 발생:', error);
        });
    },
    getBoardContent(boardId) {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/board/get-board-content.do?boardId=${boardId}`)
        .then(response => {
          if (response.data.success) {
            this.postDataList = response.data.result;
          } else {
            console.error("댓글 작성 실패");
          }
        })
        .catch(error => {
          console.error('게시물 데이터를 가져오는 중 오류 발생:', error);
        });
    },
    loadUserName() {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/user/get-user-data`, {
          params: {
            userId: this.writer_id
          }
        })
        .then((response) => {
          if (response.data.success) {
            this.userName = response.data.result.name;
          } else {
            console.error("오류: 유저를 찾을 수 없습니다");
          }
        })
        .catch((error) => {
          console.error("유저 불러오기 실패! :", error);
        });
      },
    loadPostData() {
      const titleElement = document.querySelector('.title_text');
      if (titleElement) {
        titleElement.textContent = this.title;
      }
    },
    linkToBoard(postcate) {
      this.$router.push({ name: 'board', params: { category: postcate } });
    },
    writeComment() {
      const updatedDateFormatted = moment(new Date()).format('YYYY.MM.DD hh:mm:ss');
      const postData = {
        content: this.cmtInput,
        createdTime: updatedDateFormatted,
        likes: 0,
        postId: parseInt(this.$route.params.id),
        userId: this.userId,
      };
      
      apiClient
        .post(`${process.env.VUE_APP_API_URL}/api/comment/post-cmt.do`, postData)
        .then((response) => {
          if (response.data.success) {
            this.$router.go(0);
          } else {
            console.error("댓글 작성 실패");
          }
        })
        .catch(function (error) {
          console.error(error);
        });

    },
    loadComment() {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/comment/get-cmt-list-post.do?postId=${this.$route.params.id}`)
        .then((response) => {
          if (response.data.success) {
            this.commentList = response.data.result;
          } else {
            console.error("김민중 머리 부수기");
          }
        })
        .catch(function (error) {
          console.error(error);
        });
    },
    openApplyModal() {
      this.ApplyModalVisible = true;
    },
    changingbtn() {
      this.appliedbtn = true;
    },
    linkToCodeEditor(codeId) {
      emitter.emit('open-code');
      const routeData = this.$router.resolve({
        name: 'codeedit',
        query: { 
          codeId: codeId,
          openCode: true,
         } // query로 전달
      });
      window.open(routeData.href, '_blank');
    }

  },
  computed: {
    commentCount() {
      return this.commentList.length;
    },
  },
  created() {
    const postId = parseInt(this.$route.params.id);
    this.fetchPostData(postId);
  },
};
</script>

<style scoped>
.codeblock-btn{
    display: block;
    width: 30px;
    height: 30px;
    margin-left: 5px;
}
.codeblock-btn:hover{
    filter: invert(76%) sepia(23%) saturate(6021%) hue-rotate(312deg) brightness(75%) contrast(105%);
    cursor: pointer;
}
.code-container{
    display: flex;
    position: relative;
    margin-bottom: 20px;
    height: auto;
    float: left;
}
.view_wrapper{
    width: 1810px;
    height: 3000px;
    margin: 0 auto;
}
.menubar {
    float: left;
    margin-left: 150px;
    margin-top: 230px;
    width: 200px;
    height: 600px;
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
.view_right_layout{
    float: left;
    margin-top: 230px;
    margin-left: 100px;
    width: 1300px;
}
.post_titlebox{
    float: left;
    width: 1000px;
    height: 80px;
}
.title_text{
    margin-top: 10px;
    font-size: 40px;
    font-weight: bold;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
.applybox{
    float: left;
    width: 300px;
    height: 80px;
}
.applybtn{
    margin-top: 12px;
    margin-left: 40px;
    width: 200px;
    height: 55px;
    letter-spacing: 2px;
    border: none;
    border-radius: 30px;
    font-size: 30px;
    font-weight: bold;
    background-color: #FF6969;
    box-shadow: 2px 2px 3px gray;
}
.applybtn:hover{
    background-color: #c04949;
}
.appliedbox{
    float: left;
    width: 300px;
    height: 80px;
}
.appliedbtn{
    margin-top: 12px;
    margin-left: 40px;
    width: 200px;
    height: 55px;
    letter-spacing: 2px;
    border: none;
    border-radius: 30px;
    font-size: 30px;
    font-weight: bold;
    background-color: #949494;
    box-shadow: 2px 2px 3px gray;
}
.tagbox{
    position: absolute;
    margin-top: 90px;
    float: left;
    height: 80px;
}
.hashtag{
    margin-top: 25px;
    margin-right: 15px;
    float: left;
    font-size: 20px;
}
.post_data_box{
    float: right;
    height: 80px;
    padding-top: 10px;
}
.application-num{
    float: left;
    margin-left: 20px;
    padding-top: 15px;
    padding-left: 25px;
    width: 90px;
    height: 50px;
    color: #FF5151;
    font-size: 16px;
    font-weight: bold;
}
.writer{
    float: left;
    height: 50px;
    padding-top: 12px;
    padding-left: 13px;
    font-weight: bold;
    font-size: 20px;
    margin-right: 20px;
}
.time-line{
    float: left;
    width: 90px;
    height: 50px;
    padding-top: 15px;
    font-size: 17px;
}
.friend-plusbtn{
    float: left;
    width: 100px;
    height: 38px;
    margin-top: 7px;
    border: 1px solid;
    font-size: 18px;
    border-radius: 20px;
    background-color: #F5F5F5;
    margin-left: 10px;
}
.friend-plusbtn:hover{
    background-color: #cfcfcf;
}
.post_content{
    position: relative;
    float: left;
    width: 1300px;
    border-bottom: 0.5px solid #888686;
    border-top: 0.5px solid #888686;
    padding-top: 50px;
}
.code-box{
    width: 900px;
    height: 400px;
    background-color: rgb(236, 236, 236);
}
.heartbox{
    position: absolute;
    bottom: 0px;
    right: 0px;
    width: 150px;
    height: 80px;
}
.heartbtn{
    width: 100px;
    height: 40px;
    margin-left: 25px;
    margin-top: 20px;
    border: 1px solid #888686;
    border-radius: 20px;
    background-color: #F5F5F5;
}
.commentbox{
    float: left;
    margin-top: 5px;
    width: 1300px;
    height: 300px;
}
.comment_text{
    width: 300px;
    height: 50px;
    padding-top: 10px;
    font-size: 20px;
}
.comment_textbox{
    padding-bottom: 170px;
    width: 1300px;
    height: 200px;
    border: 1px solid;
    font-size: 20px;
    vertical-align: top;
    line-height: normal;
    word-wrap: break-word;
    resize: none;
    background-color: #ffffff;
    border-radius: 5px;
    outline: none;
}
.comment_textbox::placeholder{
    position: absolute;
    top: 0;
    left: 0;
}
.submitbox{
    float: right;
    width: 100px;
    height: 50px;
}
.submitbtn{
    margin-top: 7px;
    width: 90px;
    height: 35px;
    border: 1px solid;
    border-radius: 10px;
}
.submitbtn:hover{
    background-color: #cfcfcf;
}
.comment_content{
    float: left;
    width: 1300px;
    height: 200px;
}
.user-box{
    width: 1300px;
    height: 80px;
}
.profile-box{
    float: left;
    width: 80px;
    height: 80px;
    padding-left: 5px;
    padding-top: 5px;
}
.userinform{
    float: left;
    height: 80px;
}
.user-name{
    height: 30px;
    margin-top: 13px;
    margin-left: 15px;
    font-size: 20px;
    letter-spacing: 1px;
    font-weight: bold;
}
.timeline{
    width: 150px;
    height: 20px;
    margin-left: 15px;
    font-size: 14px;
}
.content-box{
    width: 1300px;
    padding-top: 20px;
    font-size: 20px;
    font-weight: bold;
}
.plus-comment{
    width: 100px;
    height: 30px;
    margin-top: 10px;
    font-size: 14px;
    color: #616161;
    text-align: left;
    border: none;
    background-color: transparent;
}
.plus-comment:hover{
    font-weight: bold;
}
.post-text {
    font-size: 20px;
}
</style>