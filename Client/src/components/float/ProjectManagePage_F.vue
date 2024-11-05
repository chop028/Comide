<template>
  <ProjectManageModal v-if="isModalVisible" :groupId="gId" @close="closeModal" @selectNewProject="addAndClose" @add-file="addFileToList" />
  <div class="projectmanage_page">
    <div class="header-topp">
      <button class="projectmanagebackbtn" @click="goBack">
        <img src="../../assets/arrow-left-short.svg" width="45px" height="45px" />
      </button>
      그룹 프로젝트
    </div>
    <div class="project-box">
      <ul style="padding: 0;">
        <li v-for="(file, index) in folders" :key="index" class="file_item" @click="selectProject(file)">
          <div class="file_content">
            <div class="file_name">{{ file.name }}</div>
            <div class="file_language">{{ file.type }}</div>
            <div class="file_date">{{ file.creationDate }}</div>
          </div>
          <button class="project_delete-btn" @click="removeFile(index)">X</button>
        </li>
      </ul>
      <button class="plus_project" @click="movemakegroup">+</button>
    </div>
  </div>
</template>

<script>
import ProjectManageModal from '@/components/float/ProjectManageModal_F.vue';
import emitter from '@/eventBus/eventBus.js';
import apiClient from "@/axios/axios";

export default {
  emits: ['changeComponent'],
  components: {
    ProjectManageModal
  },
  
  props: {
      gId: {
        type: String,
        required: true,
      },
      gName: {
        type: String,
        required: true,
      }
  },
  data() {
    return {
      isModalVisible: false,
      folders: [],
      groupId: this.gId,
    };
  },
  methods: {
    selectProject(file) {
      emitter.emit('open-group-project');
      this.$router.push({
        name: 'codeedit',
        query: { 
          openGroupProject: true,
          projectId: file.id,
          projectName: file.name,
          groupName: this.gName,
         }
      });
    },
    addAndClose() {
      this.isModalVisible = false;
      this.$emit("changeComponent", 'MyGroupPage');
    },
    movemakegroup() {
      this.isModalVisible = true;
    },
    removeFile(index) {
      this.files.splice(index, 1);
    },
    closeModal() {
      this.isModalVisible = false;
    },
    addFileToList(file) {
      this.files.push(file);
    },
    goBack() {
      this.$emit("changeComponent", 'MyGroupPage');
    },
    loadCodeListByGroup() {
        apiClient
            .get(`${process.env.VUE_APP_API_URL}/api/file-system/get-group-project?groupId=${this.gId}`)
            .then((response) => {
            if (response.data.success) {
                this.folders = response.data.result;
            } else {
                console.error("그룹 코드 정보 받아오기 실패!");
            }
        })
        .catch(function(error) {
        console.error(error);
        });
    },
  },
  mounted() {
    emitter.on('add-file', this.addFileToList);
    this.loadCodeListByGroup();
  },
  beforeUnmount() {
    emitter.off('add-file', this.addFileToList);
  },
};
</script>

<style scoped>

.projectmanagebackbtn {
  float: left;
  background-color: transparent;
  border: none;
  cursor: pointer;
}
.projectmanagebackbtn img {
  filter: grayscale(100%);
  transition: filter 0.3s ease;
}
.projectmanagebackbtn:hover img {
  filter: none;
}

.projectmanage_page {
  width: 500px;
  height: 780px;
  background-color: #fafafa;
  border-radius: 25px;
  margin: 0 auto;
  padding-bottom: 60px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.header-topp {
  width: 100%;
  height: 80px;
  display: flex;
  align-items: center;
  padding: 0 40px;
  font-size: 28px;
  font-weight: 600;
  color: #333;
  background-color: #fafafa;
  border-radius: 25px 25px 0 0;
}

.project-box {
  overflow-y: auto;
  width: 100%;
  height: 700px;
  padding: 20px;
  background-color: #fafafa;
  border-radius: 0 0 25px 25px;
}

.plus_project {
  display: block;
  margin: 40px auto;
  width: 400px;
  height: 90px;
  border: 2px dashed #ccc;
  border-radius: 20px;
  font-size: 28px;
  font-weight: bold;
  color: #555;
  background-color: #fff;
  transition: background-color 0.3s ease, color 0.3s ease;
  cursor: pointer;
}

.plus_project:hover {
  background-color: #f0f0f0;
  color: #333;
}

.file_item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 20px;
  margin: 20px auto;
  width: 400px;
  height: 90px;
  background-color: #fff;
  border-radius: 15px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
}

.file_item:hover {
  transform: translateY(-5px);
}

.file_content {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.file_name {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 5px;
}

.file_num,
.file_language,
.file_date {
  font-size: 14px;
  color: #777;
}

.project_delete-btn {
  background-color: transparent;
  color: black;
  border: none;
  border-radius: 50%;
  width: 35px;
  height: 35px;
  font-size: 18px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.project_delete-btn:hover {
  background-color: #ff4d4d;
}
</style>