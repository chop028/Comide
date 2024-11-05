<template>
  <file-dialog v-if="isDialogOpen" :folderId="selectedFolderId" @close="closeDialog" @file-selected="selectedAndClose"/>
  <div class="drive_wrap1">
    <div class="drive_topbar1">
      <button class="backbtn" @click="Closed">
        <img src="../../assets/arrow-left-short.svg" width="45px" height="45px" />
      </button>
      <span style="font-size: 33px; font-weight: bold; color: #2e3a59;">프로젝트 불러오기</span>
    </div>
    <div class="drive-container">
      <div v-for="folder in folders" :key="folder.id" class="mydrive" @click="selectItem(folder.id)">
        <div class="img-wrap"><img src="@/assets/folder.svg" class="folder-image"></div>
        <div class="info-wrap">
          <div class="name-wrap"><span class="drive_name">{{ folder.name }}</span></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from '@/store/auth';
import apiClient from "@/axios/axios";
import FileDialog from "../float/OpenFileComponent.vue";

export default {
  emits: ['changeComponent'],
  data() {
    return {
      folders: [],
      selectedFolderId: null,
      isDialogOpen: false,
    }
  },
  props: {
    selectedProjType: {
      type: Boolean,
      required: true
    },
  },
  components : {
    FileDialog
  },
  methods: {
    selectedAndClose(codeId) {
      this.$emit('closeWithSelection', codeId);
      this.$emit('close');
    },
    selectItem(folderId) {
      this.openDialog(folderId);
    },
    loadCodeList() {
      const userId = useAuthStore().userInfo.id;
      if(!this.selectedProjType) {
        apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/group/get-group-code-list?userId=${userId}`)
        .then((response) => {
          if (response.data.success) {
            this.folders = response.data.result;
          } else {
            console.error("Error loading folders");
          }
        })
        .catch(function(error) {
          console.error(error);
        });
      } else {
        apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/file-system/get-individual-project?userId=${userId}`)
        .then((response) => {
          if (response.data.success) {
            this.folders = response.data.result;
          } else {
            console.error("Error loading folders");
          }
        })
        .catch(function(error) {
          console.error(error);
        });
      }
    },
    closeFileModal() {
      this.selectedFolderId = null;
    },
    openDialog(folderId) {
      this.selectedFolderId = folderId;
      this.isDialogOpen = true;
    },
    closeDialog() {
      this.isDialogOpen = false;
      this.selectedFolderId = null;
    },
    Closed() {
      this.$emit('close');
    }
  },
  mounted() {
    this.loadCodeList();
  }
}
</script>

<style>
.drive_wrap1 {
  position: fixed;
  width: 500px;
  height: 780px;
  overflow-y: auto;
  background-color: #f9f9f9;
  border-radius: 25px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  margin-bottom: 60px;
  z-index: 2;
}

.drive_topbar1 {
  width: 100%;
  height: 80px;
  padding-top: 25px;
  background-color: #f9f9f9;
  border-radius: 25px 25px 0 0;
  display: flex;
  align-items: center;
}

.drive-container {
  display: flex;
  flex-wrap: wrap;
  padding: 20px;
  gap: 15px;
  justify-content: space-between;
}

.mydrive {
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

.mydrive:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.img-wrap {
  margin-bottom: 10px;
}

.folder-image {
  width: 60px;
  height: 60px;
}

.info-wrap {
  text-align: center;
}

.drive_name {
  font-size: 16px;
  color: #333;
  font-weight: 600;
  margin-top: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.backbtn {
  float: left;
  margin-left: 20px;
  margin-top: 3px;
  margin-right: 15px;
  background-color: transparent;
  border: none;
}

@media screen and (max-width: 600px) {
  .drive_wrap1 {
    width: 90%;
    height: 90%;
    margin-bottom: 30px;
  }
  .mydrive {
    width: 100%;
  }
}
</style>