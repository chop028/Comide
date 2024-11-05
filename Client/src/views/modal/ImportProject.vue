<template>
  <div class="projectmodal-overlay">
    <div class="importprojectmodal-content">
      <p class="importprojectmodaltitle">프로젝트 불러오기</p>
      <input class="project-num" v-model="projectNum" placeholder="프로젝트 번호" />
      <div class="importprojectmodal-actions">
        <button class="project-openbtn" @click="confirm">불러오기</button>
        <button class="project-openbtn" @click="close">취소</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      projectNum: "",
    };
  },
  methods: {
    confirm() {
      this.$emit('load-project', this.projectNum);
      this.close();
    },
    close() {
      this.$emit("close-modal");
    },
  },
};
</script>

<style scoped>
.projectmodal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.importprojectmodal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.importprojectmodaltitle {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
  text-align: center;
}

.project-num {
  width: 50%;
  height: 40px;
  margin: auto;
  margin-bottom: 20px;
  text-align: center;
}

.importprojectmodal-actions {
  display: flex;
  justify-content: center;
  gap: 30px;
}

.project-openbtn {
  border: none;
  border-radius: 5px;
  width: 85px;
  height: 40px;
  background-color: #FAE9E9;
}

.project-openbtn:hover {
  background-color: #ffdcdc;
}
</style>
