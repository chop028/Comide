<template>
  <div class="drive_wrap">
    <div class="drive_topbar">
      <span style="font-size: 33px; font-weight: bold; color: #515151; padding-left: 30px;">드라이브<img
          src="../../assets/search.png"
          style="width: 40px; height: 40px; margin-top: 4px; float: right; margin-right: 40px; cursor: pointer;"></span>
    </div>
    <div class="top_subbar">
      <select class="drive_combobox">
        <option value="A-Z" selected>A-Z</option>
        <option value="Z-A">Z-A</option>
        <option value="Folder">Folder</option>
        <option value="File">File</option>
      </select>
      <div class="option"></div>
    </div>

    <div class="drive_list">
      <!-- 폴더 부분 주석처리
      <ul>
        <li v-for="(folder, index) in folder" :key="index" class="folder_item">
          <div class="folder_content">
            <div class="folder_icon"><img src="../../assets/folder.svg" width="30px" height="30px"></div>
            <div class="title">{{ folder.Title }}</div>
            <div class="Item_Mb">{{ folder.ItemMb }}</div>
          </div>
        </li>
      </ul>
      -->
      <ul class="drive-file-wrap">
        <li v-for="(file, index) in folders" :key="index" class="file_item" @click="selectProject(file)">
          <div class="file_content">
            <div class="file_lan"><img src="../../assets/folder2.svg" style="width: 30px; height: 30px;" /></div>
            <div class="file_title">{{ file.name }}</div>
          </div>
        </li>
      </ul>
    </div>
    <div class="navigationbar">
      <div class="nbox" @click="movefriend"><img class="neviImg" src="../../assets/people-fill.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="movechat"><img class="neviImg" src="../../assets/chat.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="movegroup"><img class="neviImg" src="../../assets/wechat.svg" width="30px" height="30px"></div>
      <div class="nbox" @click="movedrive"><img class="neviImg" src="../../assets/storage.svg" width="30px" height="30px"  style="filter: invert(76%) sepia(23%) saturate(6021%) hue-rotate(312deg) brightness(75%) contrast(105%);"></div>
      <div class="nbox" @click="moveoption"><img class="neviImg" src="../../assets/option.svg" width="30px" height="30px"></div>
    </div>
  </div>
</template>
  
<script>
import apiClient from '@/axios/axios';
import { useAuthStore } from '@/store/auth'; // Pinia 스토어 가져오기
import emitter from '@/eventBus/eventBus.js';


export default {
  data() {
    return {
      folders: [],
      folder: [
        {
          folder_icon: "",
          Title: "C# 자료 모음",
          ItemMb: "1 Item · 160MB",
        },
        {
          folder_icon: "",
          Title: "졸업작품 참고 코드",
          ItemMb: "2 Item · 356MB",
        },
        {
          folder_icon: "",
          Title: "자료 폴더 1",
          ItemMb: "5 Item · 158MB",
        },
        {
          folder_icon: "",
          Title: "자료 폴더 2",
          ItemMb: "3 Item · 152MB",
        },
      ],
      file: [
        {
          lan: "C#",
          Title: "Test.cs",
          Mb: "12mb",
          upload_icon: "",
        },
        {
          lan: "JS",
          Title: "JSTest.js",
          Mb: "1mb",
          upload_icon: "",
        },
        {
          lan: "C#",
          Title: "Test.cs",
          Mb: "12mb",
          upload_icon: "",
        },
      ],
    };
  },
  methods: {
    selectProject(file) {
      emitter.emit('open-group-project');
      this.$router.push({
        name: 'codeedit',
        query: { 
          openGroupProject: true,
          isPersonalDrive: true,
          projectId: file.id,
          projectName: file.name,
         }
      });
    },
    loadCodeList() {
      const authStore = useAuthStore(); 
      const userId = authStore.userInfo?.id; 

      if (userId) {
        apiClient
          .get(`${process.env.VUE_APP_API_URL}/api/file-system/get-individual-project?userId=${userId}`)
          .then((response) => {
            if (response.data.success) {
              this.folders = response.data.result;
            } else {
              console.error("코드 정보 받아오기 실패!");
            }
          })
          .catch(function (error) {
            console.error(error);
          });
      } else {
        console.error("유저 ID가 없습니다.");
      }
    },
    movefriend() {
        this.$emit('changeComponent', 'FriendList');
    },
    movechat() {
        this.$emit('changeComponent', 'ChatList');
    },
    movegroup() {
        this.$emit('changeComponent', 'MyGroupPage');
    },
    movechatting() {
        this.$emit('changeComponent', 'ChattingPage');
    },
    moveoption() {
        this.$emit('changeComponent', 'OptionPage');
    },
    movedrive() {
        this.$emit('changeComponent', 'DrivePage');
    },
  },
  mounted(){
    this.loadCodeList();
  }
};
</script>
  
  <style>
  .drive-file-wrap {
    padding: 0;
    text-align: center;
  }
  .neviImg:hover{
    filter: invert(76%) sepia(23%) saturate(6021%) hue-rotate(312deg) brightness(75%) contrast(105%);
  }
  .drive_wrap {
    position: relative;
    width: 500px;
    height: 780px;
    overflow-y: auto; 
    background-color: white;
    border-radius: 25px;
    margin-bottom: 60px; 
    z-index: 2;
}
  .drive_topbar{
    width: 500px;
    height: 80px;
    padding-top: 25px;
    border-radius: 25px 25px 0 0;
  }
  .top_subbar{
    display: flex;
    align-content : space-between;
    width: 500px;
    height: 30px;
    margin-top: 12px;
  }
  .addFriends{
    float: right;
    padding-top: 12px;
    padding-left: 8px;
    width: 50px;
    height: 50px;
  }
  .navigationbar {
    width: 500px;
    height: 52.5px;
    border-radius: 0 0 25px 25px;
    background-color: #F0F0F0;
}
.nbox{
    float: left;
    padding-top: 10px;
    padding-left: 15px;
    margin-left: 34px;
    width: 60px;
    height: 40px;
    cursor: pointer;
}
.movechatting{
    padding-top: 10px;
    padding-left: 200px;
    margin-left: 12px;
    width: 60px;
    height: 40px;
    cursor: pointer;
}
.drive_combobox{
  width: 50px;
  height: 20px;
  font-weight: 600px;
  font-size: 10px;
  color: #888888;
  margin-left: 30px;
}
.option{
    margin-left: 350px;
    width: 30px;
    height: 30px;
    background-image: url("../../assets/blackMenuIcon.png");
    background-size: 90%;
}
.drive_list{
  padding: 20px;
  position: relative;
  width: 500px;
  height: 605px;
  overflow-y: auto;
  border-radius: 25px;
}
.folder_item{
  width: 428px;
  height: 67px;
  border-radius: 8px;
  border: 1px solid #D4D4D4;
  background-color: #FFFFFF;
  box-shadow: 0px 5px 5px -2px gray;
  margin-top: 20px;
  cursor: pointer;
}
.folder_icon_code{
  width: 30px;
  height: 30px;
  margin-top: 15px;
  margin-left: 30px;
}
.folder_icon_cloud{
  width: 30px;
  height: 30px;
  margin-top: 15px;
  margin-left: 30px;
}
.folder_content{
  display: flex;
  align-content : space-between;
}
.file_title{
  width: 200px;
  max-width: 300px;
  display: inline-block;
  height: 26px;
  font-weight: bold;
  font-size: 20px;
  color: #515151;
  margin-left: 20px;
  margin-top: 10px;
  padding-top: 8px;
}
.file_item{
  width: 428px;
  height: 67px;
  border-radius: 8px;
  border: 1px solid #D4D4D4;
  background-color: #FFFFFF;
  box-shadow: 0px 5px 5px -2px gray;
  margin-top: 20px;
  cursor: pointer;
  display: inline-block;
  text-align: left;
}
.upload_icon{
  width: 25px;
  height: 25px;
  margin-top: 20px;
  margin-left: 30px;
}
.file_content{
  display: flex;
  align-content : space-between;
}
.file_lan{
  width: 32px;
  height: 32px;
  font-weight: bold;
  font-size: 20px;
  filter: invert(31%) sepia(3%) saturate(4%) hue-rotate(318deg) brightness(90%) contrast(84%);
  margin-top: 18px;
  margin-left: 20px;
  text-align: center;
}
.Mb{
  width: 120px;
  max-width: 300px;
  display: inline-block;
  height: 13px;
  font-weight: bold;
  font-size: 10px;
  color: #888888;
  margin-left: -200px;
  margin-top: 45px;
}
.navigationbar {
  width: 500px;
  height: 53px;
  border-radius: 0 0 25px 25px;
  background-color: #f0f0f0;
}
</style>