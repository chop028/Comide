<template>
    <div class="modal-wrap">
      <div class="modal-container">
        <div class="modal-top">그룹 선택</div>
        <div class="group-box">
          <select class="group-combobox" v-model="selectedGroup">
            <option v-for="(group, index) in groups" :key="index" :value="group">{{ group.name }}</option>
          </select>
        </div>
        <div class="modal-btn">
          <button class="selectbtn" @click="selectGroup">선택</button>
          <button class="cancelbtn" @click="closePop">취소</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
import apiClient from '@/axios/axios';
import { ref, onMounted } from 'vue';

export default {
  props: {
    userId: {
      type: String,
      required: true
    }
  },
  setup(props, { emit }) {
    const selectedGroup = ref('내 그룹1');
    const groups = ref([]);

    const closePop = () => {
      emit('close');
    };

    const selectGroup = () => {
        emit('groupSelected', selectedGroup.value);
        emit('close');
    };

    onMounted(() => {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/group/get-leader-group-list.do?userId=${props.userId}`)
        .then((response) => {
        if (response.data.success) {
            groups.value = [];
            response.data.result.forEach(group => {
              groups.value.push(group);
            });
        }
        })
        .catch(function(error) {
        console.error(error);
        });
    });

    return {
      selectedGroup,
      groups,
      closePop,
      selectGroup
    };
  }
};
</script>
  
  <style scoped>
  .modal-wrap {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.4);
  }
  
  .modal-container {
    position: relative;
    margin-top: 30px;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 500px;
    height: 350px;
    background: #fff;
    box-sizing: border-box;
  }
  
  .modal-top {
    padding-left: 50px;
    padding-top: 40px;
    width: 900px;
    height: 100px;
    font-weight: bold;
    font-size: 40px;
    letter-spacing: 2px;
    color: #515151;
  }
  
  .group-box {
    width: 500px;
    height: 150px;
  }
  
  .group-combobox {
    margin-top: 40px;
    margin-left: 45px;
    width: 400px;
    height: 70px;
    border: 2px solid #9C9C9C;
    color: #515151;
    font-weight: bold;
    font-size: 25px;
    letter-spacing: 1px;
  }
  
  .modal-btn {
    margin-left: 50px;
    font-size: 25px;
  }
  
  .selectbtn {
    width: 180px;
    height: 55px;
    border: none;
    border-radius: 5px;
    background-color: #ff9898;
    color: white;
    font-weight: bold;
  }
  
  .cancelbtn {
    margin-left: 30px;
    border-radius: 5px;
    border: 3px solid #9c9c9c;
    width: 180px;
    height: 55px;
    color: #9c9c9c;
    background-color: white;
    font-weight: bold;
  }
  
  .selectbtn:hover {
    background-color: #f07a74;
  }
  
  .cancelbtn:hover {
    background-color: rgb(238, 238, 238);
  }
  
  .folder:hover {
    cursor: pointer;
    background-color: rgb(238, 238, 238);
  }
  
  .folder-image {
    width: 70px;
    height: 70px;
  }
  </style>  