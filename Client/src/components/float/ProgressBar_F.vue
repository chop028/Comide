<template>
  <div class="progress-box">
    <div class="progress-bar">
      <div class="progress" :style="{ width: progressPercentage, transition: 'width 0.3s ease' }"></div>
    </div>
    <div class="progress-label-wrap">
      <div class="progress-label" :style="{ left: progressPercentage, transition: 'left 0.3s ease' }">{{ progressPercentage }}</div>
    </div>
  </div>
</template>

<script>
export default {
props: {
  progress: {
    type: Number,
    required: true,
  },
  max: {
    type: Number,
    default: 100,
  },
},
computed: {
  progressPercentage() {
    return `${Math.round((this.progress / this.max) * 100)}%`;
  },
},
};
</script>

<style scoped>
.progress-label-wrap{
  width: 310px;
}
.progress-box{
margin: auto;
width: 350px;
height: 25px;
}
.progress-bar {
position: relative;
width: 350px;
height: 25px;
background-color: white;
border-radius: 50px;
border: 1px solid #D6D6D6;
z-index: 3;
}
.progress-label-end{
position: absolute;
margin-right: 65px;
right: 0;
margin-top: 5px;
font-size: 16px;
color: #5E5E5E;
}
.progress {
height: 100px;
background-color: #FF9898;
}
.progress-label {
position: relative;
margin-top: 5px;
font-size: 16px;
color: #5E5E5E;
}
</style>