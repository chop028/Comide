<template>
<router-view></router-view>
<div class="admin_wrap">
  <div class="wrap2">
    <div class="multi_quiz_btn" @click="makemultiquiz">객관식 퀴즈 만들기</div>
    <div class="short_quiz_btn" @click="makeshortquiz">주관식 퀴즈 만들기</div>
    <div class="developinforcheck_btn" @click="uploadeddevelopinfor">개발정보 건의 확인</div>
    <div class="developinforupload_btn" @click="developInforPage">개발 정보 등록</div>
    <div class="developint_btn" @click="langKnwlPage">개발 지식 등록</div>
  </div>
</div>
</template>
<script>
export default {
  name: 'admin_page',
  methods: {
    langKnwlPage(){
      const intcate = "main"
      this.$router.push({ name: 'langKnwl', params: { category: intcate } });
    },
    makemultiquiz() {
      this.$router.push({ name: 'quizwritemulti' });
    },
    makeshortquiz() {
      this.$router.push({ name: 'quizwriteshort' });
    },
    uploadeddevelopinfor(){
      this.$router.push({ name: 'developinforadmin' });
    },
    developInforPage(){
      this.$router.push({ name: 'adminwritedevelopinfor' });
    },
  },
  data(){
    return {
  
    };
  },
}
</script>
<style>
.admin_wrap{
  margin: auto;
  margin-top: 180px;
  width: 1500px;
  height: 1600px;
}
.wrap2{
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}
.multi_quiz_btn{
    background-color: #FF847E;
    width: 400px;
    height: 200px;
    border: none;
    border-radius: 10px;
    transition: background-color 0.2s;
    font-size: 30px;
    font-weight: 800;
    text-align: center;
    line-height: 200px;
    color: white;
    box-shadow: 0 5px 5px #888686;
    cursor: pointer;
    margin-top: 170px;
}
.multi_quiz_btn:hover{
    background-color: #f07a74;
}
.short_quiz_btn{
    background-color: #FF847E;
    width: 400px;
    height: 200px;
    border: none;
    border-radius: 10px;
    transition: background-color 0.2s;
    font-size: 30px;
    font-weight: 800;
    text-align: center;
    line-height: 200px;
    color: white;
    box-shadow: 0 5px 5px #888686;
    cursor: pointer;
    margin-top: 170px;
    margin-left: 30px;
}
.short_quiz_btn:hover{
    background-color: #f07a74;
}
.developinforcheck_btn{
    background-color: #FF847E;
    width: 400px;
    height: 200px;
    border: none;
    border-radius: 10px;
    transition: background-color 0.2s;
    font-size: 30px;
    font-weight: 800;
    text-align: center;
    line-height: 200px;
    color: white;
    box-shadow: 0 5px 5px #888686;
    cursor: pointer;
    margin-top: 170px;
    margin-left: 30px;
}
.developinforcheck_btn:hover{
    background-color: #f07a74;
}
.developinforupload_btn{
    background-color: #FF847E;
    width: 400px;
    height: 200px;
    border: none;
    border-radius: 10px;
    transition: background-color 0.2s;
    font-size: 30px;
    font-weight: 800;
    text-align: center;
    line-height: 200px;
    color: white;
    box-shadow: 0 5px 5px #888686;
    cursor: pointer;
    margin-top: 50px;
    margin-left: 10px;
}
.developinforupload_btn:hover{
    background-color: #f07a74;
}
.developint_btn{
    background-color: #FF847E;
    width: 400px;
    height: 200px;
    border: none;
    border-radius: 10px;
    transition: background-color 0.2s;
    font-size: 30px;
    font-weight: 800;
    text-align: center;
    line-height: 200px;
    color: white;
    box-shadow: 0 5px 5px #888686;
    cursor: pointer;
    margin-top: 50px;
    margin-left: 30px;
}
.developint_btn:hover{
    background-color: #f07a74;
}
</style>