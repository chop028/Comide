<template>
  <routerview></routerview>
  <div class="develop_wrap">
      <div class="titlebar">
          <div class="titletext">{{ developTitle }}</div>
          <button class="uploadbtn" @click="uploaddevelop">업로드</button>
      </div>
      <table class="Table">
          <thead>
            <tr>
              <th class="top-No">No</th>
              <th class="top-Name">Name</th>
              <th class="top-Title">Title</th>
              <th class="top-Time">Time</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(developinfor, index) in developinfor" :key="index">
              <th class="developinfor_num" @click="getDevelopDetail(developinfor.id)">{{ developinfor.num +1 }}</th>
              <td class="developinfor_name" @click="getDevelopDetail(developinfor.id)">{{ developinfor.name }}</td>
              <td class="developinfor_title" @click="getDevelopDetail(developinfor.id)">{{ developinfor.title }}</td>
              <td class="developinfor_time" @click="getDevelopDetail(developinfor.id)">{{ developinfor.time }}</td>
            </tr>
          </tbody>
        </table>
      <div class="page_num">
        <button class="pagebtn back" @click="prevPageGroup"><img src="@/assets/arrow-left-short.svg"></button>
        <button v-for="page in displayedPages" :key="page" class="pagebtn" @click="goToPage(page)">{{ page }}</button>
        <button class="pagebtn next" @click="nextPageGroup"><img src="@/assets/arrow-right-short.svg"></button>
      </div>
  </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        developinfor: [{name: "중", title:"ㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴ", time:"2024.06.02"},],
        selectedDevelopinfor: [],
        developTitle: '개발정보 신청확인',
      };
    },
    computed: {
      displayedPages() {
        const start = Math.floor((this.currentPage - 1) / 10) * 10 + 1;
        const end = Math.min(start + 9, this.totalPages);
        let pages = [];
        for (let i = start; i <= end; i++) {
          pages.push(i);
        }
        return pages;
      },
      },
    methods: {
      uploaddevelop(){
      this.$router.push({ name: 'adminwritedevelopinfor' });
    },
      goToPage(page) {
        this.currentPage = page;
        // Load the posts for the current page (if pagination is server-side)
        // this.loadBoardList();
      },
      prevPageGroup() {
        if (this.displayedPages[0] > 1) {
          this.currentPage = this.displayedPages[0] - 1;
        }
      },
      nextPageGroup() {
        if (this.displayedPages[this.displayedPages.length - 1] < this.totalPages) {
          this.currentPage = this.displayedPages[this.displayedPages.length - 1] + 1;
        }
      },
      getDevelopDetail(developinforid) {
        this.$router.push({ name: 'developinfordetail', params: { id: developinforid } });
      },
    },
  }
  </script>
  
  <style>
  .develop_wrap{
    margin: auto;
    margin-top: 180px;
    width: 1500px;
    height: 1600px;
  }
  .titlebar {
    width: 1500px;
    height: 75px;
    margin-top: 230px;
    border-bottom: 2px solid #888686;
    display: flex;
    justify-content: space-between;
  }
  .titletext {
    font-size: 45px;
  }
  .uploadbtn {
    background-color: #FAE9E9;
    width: 140px;
    height: 50px;
    transition: background-color 0.2s;
    border: 1px solid #888686;
    font-size: 18px;
  }
  
  .uploadbtn:hover {
    background-color: #FFBBBB;
    cursor: pointer;
  }
  .Table {
    width: 1500px;
    text-align: center;
    margin-top: 15px;
    border-top: 1.5px solid #888686;
    border-bottom: 1.5px solid #888686;
    font-size: 20px;
    vertical-align : middle;
  } 
  .top-checkbtn {
    background-color: #FAE9E9;
    border-top: 1.5px solid #888686;
    border-bottom: 1.5px solid #888686;
    border-right: 1px solid #888686;
    padding: 10px;
  }
  .top-No {
    background-color: #FAE9E9;
    border-top: 1.5px solid #888686;
    border-bottom: 1.5px solid #888686;
    border-right: 1px solid #888686;
    padding: 10px;
  }
  .top-Name {
    background-color: #FAE9E9;
    border-top: 1.5px solid #888686;
    border-bottom: 1.5px solid #888686;
    border-right: 1px solid #888686;
    padding: 10px;
  }
  .top-Title {
    background-color: #FAE9E9;
    border-top: 1.5px solid #888686;
    border-bottom: 1.5px solid #888686;
    border-right: 1px solid #888686;
    padding: 10px;
  }
  .top-Time {
    background-color: #FAE9E9;
    border-top: 1.5px solid #888686;
    border-bottom: 1.5px solid #888686;
    padding: 10px;
  }
  .developinfor_checkbox{
    border-right: 1px solid #888686;
    padding: 10px;
    vertical-align : middle;
  }
  .developinfor_num{
    border-right: 1px solid #888686;
    padding: 10px;
    vertical-align : middle;
  }
  .developinfor_name{
    border-right: 1px solid #888686;
    vertical-align : middle;
  }
  .developinfor_title{
    border-right: 1px solid #888686;
    padding: 10px;
    vertical-align : middle;
  }
  .developinfor_time{
    padding: 10px;
    vertical-align : middle;
  }
  
  .developinfor_name:hover {
    cursor: pointer;
  }
  
  .developinfor_title:hover {
    cursor: pointer;
  }
  
  .developinfor_time:hover {
    text-decoration: underline;
    cursor: pointer;
  }
  .page_num{
    margin-left: 160px;
    padding-top: 10px;
    text-align: center;
    width: 1210px;
    height: 75px;
  }
  .pagebtn{
    margin-right: 10px;
    width: 40px;
    height: 40px;
    border: 1px solid;
    background-color: #FFBEBA;
  }
  .pagebtn:hover{
    background-color: #f7afab;
  }
  .form-check-input{
    border: 2px solid black;
  }
  </style>