<template>
  <div class="modal-wrap">
    <div class="modal-container">
      <div class="modal-top">파일 목록</div>
      <div class="file-container">
        <div 
          v-for="file in files" 
          :key="file.id" 
          class="file" 
          :class="{ 'selected': file.id === selectedFileId }"
          @click="selectFile(file)"
        >
          <div class="infowrap">
            <div class="namewrap">
              <span class="file_name">{{ file.name }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="modal-btn">
        <button class="cancelbtn" @click="closeFileModal">닫기</button>
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
.modal-wrap {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-container {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  width: 500px;
  padding: 30px;
  box-sizing: border-box;
  animation: fadeIn 0.3s ease-in-out;
}

.modal-top {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.file-container {
  max-height: 400px;
  overflow-y: auto;
  margin-bottom: 20px;
  padding: 10px;
  display: grid;
  grid-template-columns: 1fr;
  gap: 10px;
}

.file {
  display: flex;
  align-items: center;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.file:hover {
  background-color: #f0f0f0;
}

.selected {
  border: 2px solid #FFBBBB;
}

.imgwrap {
  flex-shrink: 0;
  margin-right: 20px;
}

.file-image {
  width: 50px;
  height: 50px;
}

.infowrap {
  flex-grow: 1;
}

.file_name {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.file_size {
  font-size: 14px;
  color: #777;
}

.modal-btn {
  display: flex;
  justify-content: flex-end;
}

.cancelbtn {
  width: 100px;
  height: 40px;
  border-radius: 6px;
  background-color: #FAE9E9;
  color: #555;
  border: none;
}
.cancelbtn:hover {
  background-color: #f7d6d6;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>