<template>
  <div class="choicemodal-box">
    <div class="choicemodal-content">
      <div class="choiceheadbox">
        <button class="choiceclose-modal-btn" @click="close">X</button>
      </div>
      <div class="choice-box">
        <div class="personal-file" @click="selectAndClose(true)">개인 파일</div>
        <div class="choicegroup-file" @click="selectAndClose(false)">그룹 파일</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    close() {
      this.$emit('close');
    },
    selectAndClose(isPrivateProj) {
      this.$emit('selectAndClose', isPrivateProj);
      this.$emit('close');
    }
  }
};
</script>

<style>
.choicemodal-box {
  padding-top: 180px;
  position: fixed; 
  width: 500px;
  height: 780px;
  border-radius: 25px;
  background: rgba(102, 102, 102, 0.5);
  z-index: 9998;
}
.choicemodal-content {
  width: 450px;
  height: 380px;
  border-radius: 20px;
  border: 1px solid #D4D4D4;
  background-color: white;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.5);
  margin-left: 25px;
}

.choiceheadbox {
  width: 450px;
  height: 70px;
  padding-top: 25px;
  border-radius: 25px 25px 0 0;
}
.choiceclose-modal-btn {
  margin-right: 10px;
  float: right;
  border: none;
  background-color: transparent;
}
.choice-box{
  width: 450px;
  height: 300px;
  display: flex;
  align-content: space-between;  
}
.personal-file{
  width: 130px;
  height: 80px;
  border: none;
  border-radius: 10px;
  box-shadow: 0 5px 5px #888686;
  margin: auto;
  margin-top: 80px;
  text-align: center;
  background-color: #FFA0A0;
  color: white;
  font-size: 20px;
  font-weight: 800;
  line-height: 80px;
  cursor: pointer;
  transition: background-color 0.2s;
}
.personal-file:hover {
  background-color: #fd9696;
}
.choicegroup-file{
  width: 130px;
  height: 80px;
  border: none;
  border-radius: 10px;
  box-shadow: 0 5px 5px #888686;
  margin: auto;
  margin-top: 80px;
  text-align: center;
  background-color: #FFA0A0;
  color: white;
  font-size: 20px;
  font-weight: 800;
  line-height: 80px;
  cursor: pointer;
  transition: background-color 0.2s;
}
.choicegroup-file:hover {
  background-color: #fd9696;
}
</style>
