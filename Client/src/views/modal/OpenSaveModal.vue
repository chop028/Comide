<template>
  <div class="modal-overlay">
    <div class="save-modal-content">
      <p class="SaveTitle">저장하기</p>
      <p class="Saving">Title</p>
      <input 
        ref="titleInput"
        class="create-title" 
        v-model="SaveTitle" 
        placeholder="제목" 
        @keydown.enter.prevent="focusComment"
      />
      <p class="Saving">Comment</p>
      <input 
        ref="commentInput"
        class="create-comment" 
        v-model="SaveComment" 
        placeholder="코멘트" 
      />
      <div class="modal-actions">
        <button class="save-code-btn" @click="save">저장</button>
        <button class="save-code-btn" @click="close">취소</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      SaveTitle: '',
      SaveComment: '',
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.$refs.titleInput.focus();
    });
  },
  methods: {
    focusComment() {
      this.$nextTick(() => {
        if (this.$refs.commentInput) {
          this.$refs.commentInput.focus();
        }
      });
    },
    save() {
      if (this.SaveTitle === '') {
        alert('제목을 입력해주세요.');
        return;
      }
      const saveData = {
        title: this.SaveTitle,
        comment: this.SaveComment,
      };
      this.$emit('closeWithSelection', saveData);
      this.close();
    },
    close() {
      this.$emit('close-modal');
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

.save-modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  width: 500px;
  height: 400px;
  text-align: center;
  animation: slideUp 0.4s ease;
}

.SaveTitle {
    margin-bottom: 50px;
  font-size: 30px;
  font-weight: bolder;
}

.Saving {
    margin-top: 20px;
    font-size: 20px;
}

.create-title {
    width: 400px;
  height: 40px;
}

.create-comment {
    width: 400px;
  height: 40px;
}

.modal-actions {
  margin-top: 20px;
  display: flex;
  justify-content: space-around;
}

.save-code-btn {
  border: none;
  border-radius: 5px;
  width: 70px;
  height: 40px;
  background-color: #FAE9E9;
  transition: background-color 0.3s ease;
}

.save-code-btn:hover {
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