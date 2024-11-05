<template>
    <RouterView></RouterView>
    <div class="view_wrapper">
      <div class="developint_menubar">
        <div class="develop_topicmenu">개발지식</div>
        <div class="develop_lan_hovermenu" @click="linkToDevelopInt('main')">언어지식</div>
        <div class="develop_submenu_front" @click="linkToDevelopInt('front')">- 웹/프론트엔드</div>
        <div class="develop_submenu" @click="linkToDevelopInt('html')">• HTML/CSS</div>
        <div class="develop_submenu" @click="linkToDevelopInt('js')">• JS</div>
        <div class="develop_submenu" @click="linkToDevelopInt('react')">• React</div>
        <div class="develop_submenu" @click="linkToDevelopInt('vue')">• Vue</div>
        <div class="develop_submenu_back" @click="linkToDevelopInt('back')">- 서버/백엔드</div>
        <div class="develop_submenu" @click="linkToDevelopInt('java')">• JAVA</div>
        <div class="develop_submenu" @click="linkToDevelopInt('python')">• Python</div>
        <div class="develop_submenu" @click="linkToDevelopInt('c#')">• C#</div>
        <div class="develop_submenu" @click="linkToDevelopInt('spring')">• Spring</div>
      </div>
      <div class="view_right_layout">
        <div class="post_titlebox">
          <div class="title_text">{{ title }}</div>
        </div>
        <div class="post_content">
          <!-- 게시글 내용 및 코드 번갈아 표시 -->
          <div v-for="(item, index) in postDataList" :key="index" style="margin-bottom: 15px;">
            <div v-if="item.category === 'content'" class="content-container">
              <div class="post-text" v-html="formatContent(item.content)"></div>
            </div>
            <div v-else-if="item.category === 'code'" style="display: flex; align-items: flex-end;">
              <DraggableComponent :codeId="item.codeId" />
              <img class="codeblock-btn" src="../assets/code_blocks.svg" @click="linkToCodeEditor(item.codeId)" />
            </div>
          </div>
          <div class="heartbox">
            <button class="heartbtn" @click="toggleLikes">
              <img v-if="likes === 0" src="../assets/heart.svg" style="width: 20px; height: 20px; margin-right: 15px;" />
              <img v-else src="../assets/heart-fill.svg" style="width: 20px; height: 20px; margin-right: 15px;" />
              <span>{{ likes }}</span>
            </button>
          </div>
        </div>
      </div>
      <DriveModal v-if="driveModalVisible" @close="driveModalVisible = false" :postData="postData" />
    </div>
  </template>
  
  <script>
  import apiClient from '@/axios/axios';
  import DraggableComponent from '@/views/DropBox.vue';
  import DriveModal from '@/views/DriveModal_T.vue';
  import { useAuthStore } from '@/store/auth';
  import emitter from '@/eventBus/eventBus.js';
  
  export default {
    components: {
      DraggableComponent,
      DriveModal,
    },
    data() {
      const authStore = useAuthStore();
      return {
        isHelp: false,
        likes: 0,
        commentList: [],
        maxApplicants: 10,
        currentApplicants: 3,
        driveModalVisible: false,
        showDraggableComponent: true,
        userId: authStore.userInfo?.id || '',
        postDataList: [],
        userName: null,
        createdDate: null,
        postData: null,
      };
    },
    methods: {
      linkToDevelopInt(intcate) {
        this.$router.push({ name: 'developint', params: { category: intcate } });
        switch (intcate) {
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
          case 'c#':
            this.developintTitle = 'C#';
            break;
          case 'spring':
            this.developintTitle = 'Spring';
            break;
        }
      },
      openDriveModal(codeId) {
        apiClient
          .get(`${process.env.VUE_APP_API_URL}/api/code-editor/get-code-detail.do?codeId=${codeId}`)
          .then((response) => {
            if (response.data.success) {
              this.postData = response.data.result;
              this.driveModalVisible = true;
            } else {
              console.error('코드 받아오기 실패!');
            }
          })
          .catch(function (error) {
            console.error(error);
          });
      },
      formatContent(content) {
        if (!content) return '';
        let formatted = content.replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;');
        formatted = formatted.replace(/\n/g, '<br>');
        return formatted;
      },
      toggleLikes() {
        this.likes = 1 - this.likes;
      },
      fetchPostData(id) {
        apiClient
          .get(`${process.env.VUE_APP_API_URL}/api/LangKnwl/get-langKnwl-detail.do?id=${id}`)
          .then((response) => {
            this.getBoardContent(id);
            const postData = response.data.result;
            this.title = postData.title;
            this.createdDate = postData.created_date;
          })
          .catch((error) => {
            console.error('게시물 데이터를 가져오는 중 오류 발생:', error);
          });
      },
      getBoardContent(langKnwlId) {
        apiClient
          .get(`${process.env.VUE_APP_API_URL}/api/LangKnwl/get-langKnwl-content.do?langKnwlId=${langKnwlId}`)
          .then((response) => {
            if (response.data.success) {
              this.postDataList = response.data.result;
            } else {
              console.error('댓글 작성 실패');
            }
          })
          .catch((error) => {
            console.error('게시물 데이터를 가져오는 중 오류 발생:', error);
          });
      },
      openApplyModal() {
        this.ApplyModalVisible = true;
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
      },
    },
    created() {
      const postId = parseInt(this.$route.params.id);
      this.fetchPostData(postId);
    },
  };
</script>
  

<style scoped>
.developint_menubar{
        float: left;
        margin-left: 100px;
        width: 200px;
        height: 600px;
        margin-top: 230px;
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
    font-size: 45px;
    font-weight: bold;
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
    height: 100%;
    border-bottom: 0.5px solid #888686;
    border-top: 0.5px solid #888686;
    padding-top: 50px;
    padding-bottom: 100px;
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
    background-color: #E2E2E2;
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