<template>
  <div v-if="isModalVisible" class="opmodal-overlay">
    <div class="opmodalcontent">
      <div class="opmodal_headwrap">
        <div class="opnewfile" @click="openCreateProjectModal">새 프로젝트</div>
        <div class="oploadfile" @click="openLoadProject">불러오기</div>
      </div>
      <button class="experiment" @click="closeModal">
        실험실 <img src="../../assets/experiment.svg" class="experiment-icon">
      </button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isModalVisible: true
    };
  },
  methods: {
    openCreateProjectModal() {
      this.$emit('new-file');
    },
     openLoadProject() {
      this.$emit('load-file');
    },
    closeModal() {
      this.$emit('select-lab');
      this.isModalVisible = false;
    }
  }
};
</script>

<style>
.opmodal-overlay {
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
.opmodalcontent {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  width: 600px;
  max-width: 90%;
  height: 380px;
  padding: 30px;
  box-sizing: border-box;
  animation: fadeIn 0.3s ease-in-out;
}
.opmodal_headwrap{
  width:500px;
  height:200px;
  display: flex;
  margin:auto;
  padding: 30px;

}
.opnewfile{
  width: 150px;
  height: 150px;
  border: none;
  border-radius: 10px;
  box-shadow: 0 5px 5px #888686;
  margin: auto;
  text-align: center;
  background-color: #FF9898;
  color: white;
  font-size: 24px;
  font-weight: 800;
  line-height: 150px;
  cursor: pointer;
}
.opnewfile:hover {
  background-color: #f98585;
}
.oploadfile{
  width: 150px;
  height: 150px;
  border: none;
  border-radius: 10px;
  box-shadow: 0 5px 5px #888686;
  margin: auto;
  text-align: center;
  background-color: #FF9898;
  color: white;
  font-size: 24px;
  font-weight: 800;
  line-height: 150px;
  cursor: pointer;
}
.oploadfile:hover {
  background-color: #f98585;
}

.experiment {
  border: none;
  background-color: transparent;
  display: flex;
  margin: auto;
  margin-top: 60px;
  text-align: center;
  color: #7F7F7F;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
}
.experiment-icon {
  margin: auto;
  text-align: center;
  filter: invert(51%) sepia(10%) saturate(10%) hue-rotate(359deg) brightness(96%) contrast(96%);
  font-size: 15px;
  font-weight: bold;
  cursor: pointer;
}
.experiment:hover {
  transform: scale(1.05);
}
</style>

