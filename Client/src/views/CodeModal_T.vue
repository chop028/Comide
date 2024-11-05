<template>
  <div class="modal-wrap">
    <div class="modal-container">
      <div class="modal-top">불러오기</div>
      <div class="folder-container">
        <div v-for="folder in folders" :key="folder.id" class="folder" @click="selectItem(folder.id, folder.name)">
          <div class="imgwrap"><img src="@/assets/folder.svg" class="folder-image"></div>
          <div class="infowrap">
            <div class="namewrap"><span class="folder_name">{{ folder.name }}</span></div>
          </div>
        </div>
      </div>

      <div class="modal-btn">
        <button class="selectbtn">선택</button>
        <button class="cancelbtn" @click="closePop">취소</button>
      </div>
    </div>
  </div>
</template>
  
<script>
import apiClient from '@/axios/axios';
import { useAuthStore } from '@/store/auth';

  export default {
      data() {
          return {
            folders: [],
          }
      },
      methods : {
      closePop() {
          this.$emit('close');
      },
      selectItem(codeId, codeName) {
          this.$emit('closeWithSelection', codeId, codeName);
          this.closePop();
      },
      loadCodeList() {
          const userId = useAuthStore().userInfo.id;
          apiClient
              .get(`${process.env.VUE_APP_API_URL}/api/file-system/get-individual-project?userId=${userId}`)
              .then((response) => {
              if (response.data.success) {
                  this.folders = response.data.result;
              } else {
                  console.error("코드 받아오기 실패");
              }
          })
          .catch(function(error) {
          console.error(error);
          });
      },
      },
      mounted() {
          this.loadCodeList();
      },
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
  width: 700px;
  max-width: 90%;
  height: auto;
  padding: 30px;
  box-sizing: border-box;
  animation: fadeIn 0.3s ease-in-out;
}

.modal-top {
  font-size: 32px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
  letter-spacing: 1.5px;
}

.folder-container {
  max-height: 500px;
  overflow-y: auto;
  margin-bottom: 20px;
  padding: 10px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.folder {
  display: flex;
  align-items: center;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 8px;
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.folder:hover {
  background-color: #e0f7fa;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  cursor: pointer;
}

.imgwrap {
  flex-shrink: 0;
  margin-right: 20px;
}

.folder-image {
  width: 60px;
  height: 60px;
}

.infowrap {
  flex-grow: 1;
}

.folder_name {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.numwrap {
  font-size: 14px;
  color: #777;
  margin-top: 5px;
}

.modal-btn {
  display: flex;
  justify-content: flex-end;
  gap: 20px;
}

.selectbtn, .cancelbtn {
  width: 120px;
  height: 45px;
  border-radius: 6px;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s ease, border-color 0.3s ease;
}

.selectbtn {
  background-color: #FAE9E9;
  color: #555;
  border: none;
}

.selectbtn:hover {
  background-color: #f7d6d6;
}

.cancelbtn {
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