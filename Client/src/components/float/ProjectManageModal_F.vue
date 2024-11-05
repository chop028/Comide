<template>
<div class="prjmodal-box">
    <div class="prjmodalcontent">
      <div class="prjheadbox">
        <button class="prjclose-modal-btn" @click="$emit('close')">X</button>
      </div>
      <div class="prjfilebox">
        <div class="newfile" @click="openCreateProjectModal">새 프로젝트</div>
      </div>
    </div>
</div>
</template>

<script>
import emitter from '@/eventBus/eventBus.js';

export default {
  props: {
      groupId: {
          type: String,
          required: true,
      },
  },
  methods: {
    openCreateProjectModal() {
      emitter.emit('open-create-project-from-manage');
      this.$router.push({
        name: 'codeedit',
        query: { 
          openCreateProject: true,
          groupId: this.groupId,
         }
      });
      this.$emit('selectNewProject');
      this.$emit('close');
    },
  }
};
</script>

<style>
.prjmodal-box {
  padding-top: 180px;
  position: fixed; 
  width: 500px;
  height: 780px;
  border-radius: 25px;
  background: rgba(102, 102, 102, 0.5);
  z-index: 9998;
}
.prjmodalcontent {
  width: 450px;
  height: 380px;
  border-radius: 20px;
  border: 1px solid #D4D4D4;
  background-color: white;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.5);
  margin-left: 25px;
}

.prjheadbox {
  width: 450px;
  height: 70px;
  padding-top: 25px;
  border-radius: 25px 25px 0 0;
}
.prjclose-modal-btn {
  margin-right: 10px;
  float: right;
  border: none;
  background-color: transparent;
}
.prjfilebox{
  width: 450px;
  height: 300px;
  display: flex;
  align-content : space-between;  
}
.newfile{
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
.newfile:hover {
  background-color: #fd9696;
}
</style>

