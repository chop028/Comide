<template>
  <div class="dialog-wrap">
    <div class="dialog-container">
      <div class="dialog-header">
        <button class="backbtn" @click="closeFileModal">
          <img src="../../assets/arrow-left-short.svg" width="45px" height="45px" />
        </button>
        파일 목록</div>
      <div class="file-list">
        <div 
          v-for="file in files" 
          :key="file.id" 
          class="file-item" 
          :class="{ 'is-selected': file.id === selectedFileId }"
          @click="selectFile(file)"
        >
          <div class="info-wrapper">
            <div class="name-wrapper">
              <span class="file-name">{{ file.name }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import apiClient from "@/axios/axios";
export default {
  props: {
    folderId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      files: [],
      selectedFileId: null,
    };
  },
  methods: {
    selectFileAndClose(file) {
      this.$emit('file-selected', file.code.id);
      this.$emit('close');
    },
    closeFileModal() {
      this.$emit('close');
    },
    loadFileList() {
      const projectId = this.folderId;
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/file-system/get-project-code?projectId=${projectId}`)
        .then((response) => {
          if (response.data.success) {
            this.files = response.data.result;
          } else {
            console.error("get-board-list failed");
          }
        })
        .catch(function (error) {
          console.error(error);
        });
      },
    selectFile(file) {
      this.selectFileAndClose(file);
    }
  },
  mounted() {
    this.loadFileList();
  }
}
</script>

<style scoped>
.dialog-wrap {
  position: fixed;
  width: 500px;
  height: 780px;
  overflow-y: auto;
  background-color: #ffffff;
  border-radius: 25px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  margin-bottom: 60px;
  z-index: 10;
  transition: transform 0.3s ease, opacity 0.3s ease;
}

.dialog-container {
  width: 100%;
  height: 100%;
  flex-direction: column;
  background-color: #fafafa;
  border-radius: 25px;
  z-index: 12;
}

.dialog-header {
  width: 100%;
  height: 80px;
  padding-top: 25px;
  background-color: #fafafa;
  border-radius: 25px 25px 0 0;
  display: flex;
  align-items: center;
  font-size: 33px;
  font-weight: bold;
  color: #2e3a59;
}

.file-list {
  flex-grow: 1;
  padding: 20px;
  flex-wrap: wrap;
  display: flex;
  gap: 20px;
  overflow-y: auto;
  justify-content: flex-start;
}

.file-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 220px;
  padding: 15px;
  background-color: #fff;
  border-radius: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.file-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.12);
}

.file-item.is-selected {
  border: 2px solid #ffbbbb;
}

.info-wrapper {
  margin-bottom: 5px;
}

.name-wrapper {
  text-align: center;
}

.file-name {
  font-size: 17px;
  color: #2e3a59;
  font-weight: 600;
  margin-top: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.close-btn:hover {
  background-color: #ffbbbb;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.backbtn {
  float: left;
  margin-left: 20px;
  margin-top: 3px;
  margin-right: 15px;
  background-color: transparent;
  border: none;
}

.file-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.12);
}

.file-item.is-selected {
  border: 2px solid #ffbbbb;
  background-color: #fff5f5;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(255, 187, 187, 0.5);
}

.file-item.is-selected:hover {
  box-shadow: 0 8px 16px rgba(255, 187, 187, 0.7);
}

@media screen and (max-width: 600px) {
  .dialog-wrap {
    width: 90%;
    height: 90%;
    margin-bottom: 30px;
  }

  .file-item {
    width: 100px;
    padding: 12px;
  }

  .file-name {
    font-size: 14px;
  }
}
</style>