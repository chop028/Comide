<template>
  <div class="modal-overlay">
      <div class="modal-content-file">
      <p class="fileTitle">새로운 파일</p>
      <input ref="fileInput" class="create-file-name" v-model="fileName" placeholder="파일 이름" />
      <div class="modal-actions">
          <button class="file-btn" @click="createFile">생성</button>
          <button class="file-btn" @click="close">취소</button>
      </div>
      </div>
  </div>
</template>

<script>
export default {
  props: ['isOpen'],
  data() {
    return {
      fileName: ""
    };
  },
  mounted() {
    this.setFocus();
  },
  methods: {
    setFocus() {
      this.$nextTick(() => {
        if (this.isOpen && this.$refs.fileInput) {
          this.$refs.fileInput.focus();
        }
      });
    },
    createFile() {
      if (this.fileName) {
        this.$emit("create-file", this.fileName, null);
        this.close();
      }
    },
    close() {
      this.$emit("close-modal");
    }
  },
  watch: {
    isOpen(newVal) {
      if (newVal) {
        this.setFocus();
      }
    }
  }
};
</script>

<style scoped>
.modal-overlay {
position: fixed;
top: 0;
left: 0;
width: 100%;
height: 100%;
background-color: rgba(0, 0, 0, 0.5);
display: flex;
justify-content: center;
align-items: center;
animation: fadeIn 0.4s ease;
}

.modal-content-file {
background-color: white;
padding: 20px;
border-radius: 5px;
width: 500px;
height: 200px;
text-align: center;
animation: slideUp 0.4s ease;
}

.fileTitle {
font-size: 30px;
font-weight: bolder;
}

.create-file-name {
height: 40px;
}

.modal-actions {
margin-top: 20px;
display: flex;
justify-content: space-around;
}

.file-btn {
border: none;
border-radius: 5px;
width: 70px;
height: 40px;
background-color: #FAE9E9;
transition: background-color 0.3s ease;
}

.file-btn:hover {
background-color: #ffdcdc;
}

@keyframes fadeIn {
from {
  opacity: 0;
}
to {
  opacity: 1;
}
}

@keyframes slideUp {
from {
  transform: translateY(50px);
  opacity: 0;
}
to {
  transform: translateY(0);
  opacity: 1;
}
}
</style>