<template>
  <div>
    <input type="text" v-model="name" placeholder="이름">
    <div class="post_topbar">
      <div class="title">제목</div>
      <input type="text" v-model="title" class="input-title" placeholder="제목을 입력하세요.">
      <select class="combobox" v-model="category">
        <option value="==분류선택==" disabled selected>==분류선택==</option>
        <option value="오류코드">오류코드</option>
        <option value="질의응답">질의응답</option>
        <option value="오픈소스">오픈소스</option>
        <option value="협업모집">협업모집</option>
      </select>
      <div v-if="showbtn" class="selectgroupbox">
        <button class="selectgroupbtn" @click="openSelectGroupModal">그룹선택</button>
      </div>
      <div v-if="changebtn" class="selectedgroupbox">
        <button class="selectedgroupbtn" @click="openSelectGroupModal">{{ selectedGroup }}</button>
      </div>
    </div>

    <div class="posting-wrap">
      <div><span class="taging-box">해시태그</span></div>
      <div v-for="(hashtag, index) in hashtags" :key="index">
        <input type="text" v-model="hashtags[index]" class="hashtaging">
      </div>
      <button class="add-hashtagbtn" @click="addHashtag">+</button>

      <div class="writing-page">
        <button class="content-postbtn" @click="addInput('content')">글추가</button>
        <button class="code-postbtn" @click="addInput('code')">코드추가</button>
        <div v-for="(input, index) in inputs" :key="index" class="input-section">
          <ContentInput v-if="input.type === 'content'" v-model="input.value" class="summernote"/>
          <CodeInput v-else-if="input.type === 'code'" v-model="input.value" class="codenote"  :codeId="input.codeId"/>
          <button class="remove-btn" @click="removeInput(index)">삭제</button>
        </div>
      </div>
    </div>
    <div class="input_group">
      <input type="file" class="form-control" id="inputGroupFile01">
      <input type="file" class="form-control" id="inputGroupFile02">
    </div>
    <div class="footer">
      <button class="wrtbtn" @click="writePost">글쓰기</button>
    </div>
  </div>
  <OpenFolder v-if="codeModalVisible" @close="codeModalVisible = false" @closeWithSelection="handleModalClose"></OpenFolder>
  <SelectGroupModal v-if="SelectGroupModalVisible" @close="SelectGroupModalVisible = false" @groupSelected="updateSelectedGroup" :userId="userId"></SelectGroupModal>
</template>

<script>
import apiClient from '@/axios/axios';
import moment from 'moment';
import SelectGroupModal from '@/views/SelectGroupModal.vue';
import OpenFolder from '../views/modal/OpenFolder.vue';
import CodeInput from '@/views/CodeInput_T.vue';
import ContentInput from '@/views/ContentInput_T.vue';
import { useAuthStore } from '@/store/auth';

export default {
  data() {
    const authStore = useAuthStore();
    return {
      name: '',
      title: '',
      category: "==분류선택==",
      SelectGroupModalVisible: false,
      showbtn: false,
      selectedGroup: '',
      changebtn: false,
      codeModalVisible: false,
      userId: authStore.userInfo?.id || '',
      selGroup: null,
      hashtags: [''],
      hashTagList:[],
      inputs: [],
      boardId: '',
      boardContentList: [],
      currentCodeId: '',
      currentCodeValue: '',
    };
  },
  methods: {
    handleModalClose(codeId) {
      apiClient
          .get(`${process.env.VUE_APP_API_URL}/api/code-editor/get-code-detail.do?codeId=${codeId}`)
          .then((response) => {
          if (response.data.success) {
              this.currentCodeId = codeId;
              this.currentCodeValue = response.data.result.content;
              this.inputs.push({ type: "code", codeId: this.currentCodeId, completed: false });
          } else {
              console.error("코드 받아오기 실패!");
          }
        })
        .catch(function(error) {
        console.error(error);
        });
    },
    writePost() {
      if (this.category === "==분류선택==") {
        alert("게시판을 선택해주세요");
        return;
      }
      const createdDateFormatted = moment(new Date()).format('YYYY.MM.DD');
      const modifiedDateFormatted = moment(new Date()).format('YYYY.MM.DD');
      const postData = {
        name: this.name,
        category: this.category,
        title: this.title,
        content: "",
        //content: this.inputs.filter(input => input.type === 'content').map(input => input.value).join("\n"),
        //code: this.inputs.filter(input => input.type === 'code').map(input => input.value).join("\n"),
        created_date: createdDateFormatted,
        modified_date: modifiedDateFormatted,
        userId: this.userId,
        groupId: this.selGroup,
        viewCnt: 0,
        likes: 0,
      };
      apiClient.post(`${process.env.VUE_APP_API_URL}/api/board/post-board.do`, postData, {
        headers: {
          'Content-Type': 'application/json',
        }
      })
      .then(response => {
        if (response.data.success) {
          this.boardId = response.data.result;
          this.saveBoardContnent();
          this.saveHashTag();
          this.$router.push('/board/main');
        } else {
          console.error("Post failed");
        }
      })
      .catch(error => {
        console.error(error);
      });
    },
    saveBoardContnent(){
      this.inputs.forEach(input => {
        let contentData = {
          category: input.type,
          boardId: this.boardId,
          codeId: null,
          content: null
        }
        if(input.type === "content") {
          contentData.content = input.value;
        } else {
          contentData.codeId = input.codeId;
        }
        this.boardContentList.push(contentData);
      });
      apiClient
        .post(`${process.env.VUE_APP_API_URL}/api/board/save-board-content.do`, this.boardContentList)
        .then((response) => {
        if (!response.data.success) {
          console.error("get-board-list failed");
        }
        })
        .catch(function(error) {
        console.error(error);
        });
    },
    saveHashTag(){
      this.hashtags.forEach(hashtag => {
        let hashTagData = {
          content: hashtag,
          boardId: this.boardId
        }
        this.hashTagList.push(hashTagData);
      });
      apiClient
        .post(`${process.env.VUE_APP_API_URL}/api/board/save-hashTag.do`, this.hashTagList)
        .then((response) => {
        if (!response.data.success) {
          console.error("get-board-list failed");
        }
        })
        .catch(function(error) {
        console.error(error);
        });
    },
    openSelectGroupModal() {
      this.SelectGroupModalVisible = true;
    },
    updateSelectedGroup(group) {
      this.selGroup = group.id;
      this.selectedGroup = group.name;
      this.changebtn = true;
      this.showbtn = false;
    },
    openCodeModal() {
      this.codeModalVisible = true;
    },
    addInput(type) { 
      if(type === "content") {
        this.inputs.push({ type: type, value: '', completed: false });
      } else {
        this.openCodeModal();
      }
      
    },
    completeInput(index) {
      this.inputs[index].completed = true;
    },
    removeInput(index) {
      this.inputs.splice(index, 1);
    },
    addHashtag() {
      this.hashtags.push('');
    },
  },
  watch: {
    category(newVal) {
      if (newVal === "협업모집") {
        this.showbtn = true;
        this.changebtn = false;
      } else {
        this.showbtn = false;
        this.changebtn = false;
      }
    }
  },
  components: {
    SelectGroupModal,
    OpenFolder,
    CodeInput,
    ContentInput
  }
}
</script>

<style>
.input-section{
  margin-top: 20px;
}
#test {
  position: absolute;
  top: 0;
}
.post_topbar {
  padding-left: 23px;
  padding-right: 25px;
  margin: auto;
  margin-top: 230px;
  width: 1450px;
  height: 90px;
  overflow: auto;
  display: flex;
  align-items: center;
}
.input-title {
  margin: auto;
  width: 1000px;
  height: 44px;
  margin-left: 30px;
  margin-right: 0px;
  font-size: 16px;
}
.title {
  width: 100px;
  font-size: 30px;
  font-weight: 700;
  float: left;
  color: #515151;
}
.combobox {
  width: 120px;
  height: 44px;
  text-align: center;
  margin-left: 20px;
}
.posting-box {
  padding-top: 10px;
  font-size: 25px;
  font-weight: bold;
  color: #515151;
}
.taging-box {
  padding-top: 10px;
  font-size: 25px;
  font-weight: bold;
  color: #515151;
}
.hashtaging {
  float: left;
  margin-top: 15px;
  margin-right: 20px;
  margin-bottom: 30px;
  width: 100px;
  height: 40px;
  border-radius: 8px;
  border: 1px solid #515151;
}
.add-hashtagbtn {
  margin-top: 15px;
  margin-right: 20px;
  margin-bottom: 30px;
  width: 100px;
  height: 40px;
  border-radius: 8px;
  border: 1px solid #515151;
  background-color: white;
}
.add-hashtagbtn:hover {
  background-color: rgb(239, 239, 239);
}
.posting-wrap {
  padding-top: 20px;
  margin: auto;
  width: 1400px;
}
.writing-page {
  width: 1400px;
}
.summernote {
  width: 1400px;
  height: 100%;
  margin: auto;
  margin-top: 20px;
  display: block;
}
.codenote {
  width: 1400px;
  height: 100px;
  margin: auto;
  margin-top: 20px;
  display: block;
}
.code-postbtn {
  margin-top: 30px;
  width: 90px;
  height: 40px;
  background-color: #FF6969;
  border: none;
  border-radius: 25px;
  color: white;
  font-size: 18px;
  font-weight: bold;
}
.code-postbtn:hover {
  background-color: #c04949;
}
.content-postbtn {
  margin-right: 10px;
  margin-top: 30px;
  width: 90px;
  height: 40px;
  background-color: #FF6969;
  border: none;
  border-radius: 25px;
  color: white;
  font-size: 18px;
  font-weight: bold;
}
.content-postbtn:hover {
  background-color: #c04949;
}
.input_group {
  margin: auto;
  width: 1400px;
  height: 100px;
  margin-top: 30px;
}
.footer {
  margin: auto;
  width: 1400px;
  height: 50px;
}
.wrtbtn {
  width: 120px;
  height: 45px;
  margin-left: 610px;
  margin-top: 2px;
}
.wrtbtn:hover {
  background-color: #d2d2d2;
  cursor: pointer;
}
.selectgroupbox {
  margin-top: 22px;
  float: left;
  width: 300px;
  height: 80px;
}
.selectgroupbtn {
  margin-left: 20px;
  width: 190px;
  height: 50px;
  letter-spacing: 2px;
  border: none;
  border-radius: 30px;
  font-size: 30px;
  font-weight: bold;
  background-color: #FF6969;
  color: white;
  box-shadow: 2px 2px 3px gray;
}
.selectedgroupbtn {
  margin-left: 20px;
  width: 190px;
  height: 50px;
  letter-spacing: 2px;
  border: none;
  border-radius: 30px;
  font-size: 20px;
  font-weight: bold;
  color: white;
  background-color: #949494;
  box-shadow: 2px 2px 3px rgb(114, 114, 114);
}
.selectgroupbtn:hover {
  background-color: #c04949;
}
.selectedgroupbtn:hover {
  background-color: #787878;
}
.remove-btn {
  margin-top: 10px;
  padding: 5px 10px;
  background-color: #ffa8a8;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}
.remove-btn:hover {
  background-color: #eb6060;
}
</style>