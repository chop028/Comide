<template>
  <div>
    <input type="text" v-model="name" placeholder="이름">
    <div class="dev_topbar">
      <div class="title">제목</div>
      <input type="text" v-model="title" class="input-title" placeholder="제목을 입력하세요.">
    </div>
    <textarea class="summernote" v-model="content" type="text" placeholder="내용입력 및 하단 첨부파일 첨부"></textarea>
    <div class="input_group">
      <input type="file" class="form-control" id="inputGroupFile01">
    </div>
    <div class="footer">
      <button class="wrtbtn" @click="upload">등록</button>
    </div>
  </div>
</template>
  
<script>
import apiClient from '@/axios/axios';
import moment from 'moment';
import { useAuthStore } from '@/store/auth';

export default {
  data() {
    return {
      
    }
  },
  methods: {
    // 글쓰기 버튼 클릭 시 실행될 함수
    upload() {
      const authStore = useAuthStore();

      const createdDateFormatted = moment(new Date()).format('YYYY.MM.DD');
      const modifiedDateFormatted = moment(new Date()).format('YYYY.MM.DD');
      const postData = {
        name: 'test',
        title: this.title,
        content: this.content,
        created_date: createdDateFormatted,
        modified_date: modifiedDateFormatted,
        userId: authStore.userInfo?.id,
      };

      apiClient
        .post(`${process.env.VUE_APP_API_URL}/api/board/post-board.do`, postData)
        .then((response) => {
          if (response.data.success) {
            this.$router.push('/board/main');
          }else{
            console.error("get-board-list failed");
          }
        })
        .catch(function(error) {
          console.error(error);
        });
    }
  }
}
</script>

<style>
#test{
  position: absolute;
  top: 0;
}
.dev_topbar{
  margin: auto;
  margin-top: 230px;
  width: 1400px;
  height: 70px;
  overflow: auto;
  display: flex;
  align-items: center;
}
.input-title{
  margin: auto;
  width: 1000px;
  height: 44px;
  margin-left: 95px;
  margin-right: 0px;
  font-size: 16px;
}
.title{
  font-size: 28px;
  font-weight: 700;
  float: left;
}
.summernote{
  width: 1400px;
  height: 700px;
  margin: auto;
  margin-top: 20px;
  display: block;
}
.summernote::placeholder{
  margin-left: 20px;
  margin-top: 10px;
}
.input_group{
  margin: auto;
  width: 1400px;
  height: 100px;
  margin-top: 20px;
}
.footer{
  margin: auto;
  width: 1400px;
  height: 50px;
}
.wrtbtn{
  width: 120px;
  height: 45px;
  margin-left: 610px;
  margin-top: 2px;
}
.wrtbtn:hover {
  background-color: #a7a7a7;
  cursor: pointer;
}
</style>