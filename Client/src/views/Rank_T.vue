<template>
  <routerview></routerview>
  <div class="rank_wrap">
    <div class="titlebar">
      <div class="titletext">{{ rankTitle }}</div>
    </div>
    <table class="rank_table">
      <thead>
        <tr>
          <th class="ranktop_No">No</th>
          <th class="ranktop_Profile">Profile</th>
          <th class="ranktop_Name">Name</th>
          <th class="ranktop_Comment">Comment</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(rank, index) in rank" :key="index">
          <th class="rank_num">{{ rank.num + 1 }}</th>
          <td class="rank_profile">
            <img
              class="user-icon"
              :src="userInfo.picture"
              width="55px"
              height="55px"
              style="border-radius: 50px;"
            />
          </td>
          <td class="rank_name">{{ userInfo.name }}</td>
          <td class="rank_comment">{{ rank.comment }}</td>
        </tr>
      </tbody>
    </table>
    <div class="page_num">
      <button class="pagebtn back" @click="prevPageGroup"><img src="@/assets/arrow-left-short.svg" /></button>
      <button v-for="page in displayedPages" :key="page" class="pagebtn" @click="goToPage(page)">{{ page }}</button>
      <button class="pagebtn next" @click="nextPageGroup"><img src="@/assets/arrow-right-short.svg" /></button>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from '@/store/auth';

export default {
  data() {
    return {
      rank: [{ comment: "은글슬쩍스리슬쩍랭킹심어주고" }],
      rankTitle: '랭킹',
    };
  },
  computed: {
    userInfo() {
      const authStore = useAuthStore();
      return authStore.userInfo;
    },
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
    goToPage(page) {
      this.currentPage = page;
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
  },
};
</script>

<style>
.rank_wrap{
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
.rank_table {
  width: 1500px;
  text-align: center;
  margin-top: 15px;
  border-top: 1.5px solid #888686;
  border-bottom: 1.5px solid #888686;
  font-size: 20px;
  vertical-align : middle;
} 
.ranktop_No {
  background-color: #FAE9E9;
  border-top: 1.5px solid #888686;
  border-bottom: 1.5px solid #888686;
  border-right: 1px solid #888686;
  padding: 10px;
}
.ranktop_Profile {
  background-color: #FAE9E9;
  border-top: 1.5px solid #888686;
  border-bottom: 1.5px solid #888686;
  border-right: 1px solid #888686;
  padding: 10px;
}
.ranktop_Name {
  background-color: #FAE9E9;
  border-top: 1.5px solid #888686;
  border-bottom: 1.5px solid #888686;
  border-right: 1px solid #888686;
  padding: 10px;
}
.ranktop_Comment {
  background-color: #FAE9E9;
  border-top: 1.5px solid #888686;
  border-bottom: 1.5px solid #888686;
  padding: 10px;
}
.rank_num{
  border-right: 1px solid #888686;
  padding: 10px;
  vertical-align : middle;
}
.rank_profile{
  border-right: 1px solid #888686;
  padding: 10px;
  vertical-align : middle;
}
.rank_name{
  border-right: 1px solid #888686;
  vertical-align : middle;
}
.rank_comment{
  padding: 10px;
  vertical-align : middle;
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
</style>