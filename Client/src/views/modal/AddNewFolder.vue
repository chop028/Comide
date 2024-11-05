<template>
  <div class="modal-overlay">
    <div class="modal-content">
      <p class="folderTitle">새로운 폴더</p>
      <input ref="folderInput" class="create-folder-name" v-model="folderName" placeholder="폴더 이름" />
      <div class="modal-actions">
        <button class="folder-btn" @click="createFolder">생성</button>
        <button class="folder-btn" @click="close">취소</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
  },
  data() {
    return {
      folderName: "",
    };
  },
  mounted() {
    this.setFocus();
  },
  methods: {
    setFocus() {
      this.$nextTick(() => {
        if (this.isOpen && this.$refs.folderInput) {
          this.$refs.folderInput.focus();
        }
      });
    },
    createFolder() {
      this.$emit("create-folder", this.folderName); 
      this.folderName = "";
    },
    close() {
      this.$emit("close-modal");
    },
  },
  watch: {
    isOpen(newVal) {
      if (newVal) {
        this.setFocus();
      }
    },
  },
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

.modal-content {
background-color: white;
padding: 20px;
border-radius: 5px;
width: 500px;
height: 200px;
text-align: center;
animation: slideUp 0.4s ease;
}

.folderTitle {
font-size: 30px;
font-weight: bolder;
}

.create-folder-name {
height: 40px;
}

.modal-actions {
margin-top: 20px;
display: flex;
justify-content: space-around;
}

.folder-btn {
border: none;
border-radius: 5px;
width: 70px;
height: 40px;
background-color: #FAE9E9;
transition: background-color 0.3s ease;
}

.folder-btn:hover {
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