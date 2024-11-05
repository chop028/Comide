<template>
<RouterView></RouterView>
<div class="mypage_wrap">
    <div class="mypage_menubar">
        <div class="mypage_topic">마이페이지</div>
        <div class="mypage_sub" @click="linkToinfo">회원정보</div>
        <div class="mypage_sub" @click="linkToact">활동내역</div>
        <div class="mypage_sub" @click="linkToquiz">내 퀴즈</div>
        <div class="mypage_sub" @click="linkTooption" id="nowpage">환경설정</div>
    </div>
  <div class="mypage_right">
    <div class="Option">환경설정</div>
    <div class="option_line1"></div>
    <div class="option_menuwrap">
      <div class="interest_btn" v-bind:style="interest_btn" ref="interest_btn" @click="trans_interest" @mouseover="over_interest" @mouseleave="leave_interest">관심분야</div>
      <div class="alarm_btn" v-bind:style="alarm_btn" ref="alarm_btn" @click="trans_alarm" @mouseover="over_alarm" @mouseleave="leave_alarm">알림설정</div>
    </div>
    <div class="option_interestpage" v-if="show_interest">
      <div class="interestpage_headwrap">
        <select class="interest_combobox">
          <option value="ALL" selected>전체보기</option>
          <option value="SELECT">설정한 분야</option>
          <option value="NONE_SELECT">설정안한 분야</option>
        </select>
      </div>
      <div class="interest_list">
        <ul class="interest_ul">
        <li v-for="(interest, index) in interest" :key="index" class="interest_wrap">
          <input class="form-check-input-interest" type="checkbox" :value="interest" :id="'flexCheckDefault' + index" v-model="selectedInterest">
          <div class="lan_text"> {{ interest.lan }}</div>
        </li>
        </ul>
      </div>  
    </div>
  <div class="alarm_page" v-if="show_alarm">
      <div class="alarm_wrap">
        <div class="alarm_lan">게시글 알림</div>
        <div class="alarm_wall"></div>
        <div class="alarm_info">내 글에 댓글 달린 경우 알려줍니다.</div>
        <div class="form-check form-switch" id="off_switch">
          <input class="form-check-input" type="checkbox" style="width: 40px; height: 20px;" role="switch" id="flexSwitchCheckDefault">
        </div>
      </div>
      <div class="alarm_line"></div>
      <div class="alarm_wrap">
        <div class="alarm_lan">친구 알림</div>
        <div class="alarm_wall"></div>
        <div class="alarm_info">친구요청이 올 경우 경우 알려줍니다.</div>
        <div class="form-check form-switch" id="off_switch">
          <input class="form-check-input" type="checkbox" style="width: 40px; height: 20px;" role="switch" id="flexSwitchCheckDefault">
        </div>
      </div>
      <div class="alarm_line"></div>
      <div class="alarm_wrap">
        <div class="alarm_lan">개발정보 알림</div>
        <div class="alarm_wall"></div>
        <div class="alarm_info">새 개발정보가 올라올 경우 경우 알려줍니다.</div>
        <div class="form-check form-switch" id="on_switch">
          <input class="form-check-input" type="checkbox" style="width: 40px; height: 20px;" role="switch" id="flexSwitchCheckChecked" checked>
        </div>
      </div>
      <div class="alarm_line"></div>
      <div class="alarm_wrap">
        <div class="alarm_lan">그룹 알림</div>
        <div class="alarm_wall"></div>
        <div class="alarm_info">새 그룹에 참여될 경우 알려줍니다.</div>
        <div class="form-check form-switch" id="on_switch">
          <input class="form-check-input" type="checkbox" style="width: 40px; height: 20px;" role="switch" id="flexSwitchCheckChecked" checked>
        </div>
      </div>
      <div class="alarm_line"></div>
   </div>
 </div>
 </div>
</template>


<script>
export default {
  methods: {
    linkToinfo() {
      this.$router.push({ name: 'mypageuserinfo', params: { category: "mypage" } });
    },
    linkToact() {
        this.$router.push({ name: 'mypageactivity', params: { category: "mypage" } });
    },
    linkToquiz() {
      this.$router.push({ name: 'mypagequiz', params: { category: "mypage" } });
    },
    linkTooption() {
      this.$router.push({ name: 'mypageoption', params: { category: "mypage" } });
    },
    trans_interest(){
      this.show_interest = true;
      this.show_alarm = false;
      this.interestActivityHoverActive = false;
      this.intereststaticActivityHoverActive = true;
      this.interest_btn['background-color']='#FFBEBA';
      this.interest_btn['border']='1px solid #FFBEBA';
      this.interest_btn['color']='black';
      this.alarm_btn['background-color']='#FFFFFF';
      this.alarm_btn['border']='1px solid #B7B7B7';
      this.alarm_btn['color']='#898989';
    },
    trans_alarm(){
      this.show_interest = false;
      this.show_alarm = true;
      this.interestActivityHoverActive = true;
      this.intereststaticActivityHoverActive = false;
      this.interest_btn['background-color']='#FFFFFF';
      this.interest_btn['border']='1px solid #B7B7B7';
      this.interest_btn['color']='#898989';
      this.alarm_btn['background-color']='#FFBEBA';
      this.alarm_btn['border']='1px solid #FFBEBA';
      this.alarm_btn['color']='black';
    },
    over_interest(){
    if(this.interestActivityHoverActive) {
      this.interest_btn['background-color']='#FFBEBA';
      }
    },
    leave_interest(){
    if(this.interestActivityHoverActive) {
      this.interest_btn['background-color']='#FFFFFF';
      }
    },
    over_alarm(){
    if(this.intereststaticActivityHoverActive) {
      this.alarm_btn['background-color']='#FFBEBA';
      }
    },
    leave_alarm(){
    if(this.intereststaticActivityHoverActive) {
      this.alarm_btn['background-color']='#FFFFFF';
      }
    },
    toggleInterest(index) {
      this.interestStates[index] = !this.interestStates[index];
    },
  },
  data() {
    return{
      show_interest: true,
      show_alarm: false,
      interestActivityHoverActive: false,
      intereststaticActivityHoverActive: true,
      interest_btn: {
      width: '142px',
      height: '46px',
      'border-radius': '4px',
      border: '1px solid #FFBEBA',
      'background-color': '#FFBEBA',
      color: 'black',
      'font-size': '20px',
      'text-align': 'center',
      'line-height': '46px',
      cursor: 'pointer',
      'z-index': '1',
      position: 'relative',
      'margin-left': '5px'
    },
    alarm_btn: {
      width: '142px',
      height: '46px',
      'border-radius': '4px',
      border: '1px solid #B7B7B7',
      'background-color': '#FFFFFF',
      color: '#898989',
      'font-size': '20px',
      'text-align': 'center',
      'line-height': '46px',
      cursor: 'pointer',
      'z-index': '1',
      position: 'relative',
      'margin-left': '18px'
    },
    interest: [{lan: "c#"},{lan: "c++"},{lan: "c++"},{lan: "c++"},{lan: "c++"},{lan: "c++"},],
  };
  },
};
</script>

<style scoped>
.mypage_wrap {
    width: 1810px;
    height: 3000px;
    margin: 0 auto;
}
.mypage_menubar {
    float: left;
    margin-left: 100px;
    margin-top: 230px;
    width: 200px;
    height: 600px;
}
.mypage_topic {
  width: 200px;
  height: 50px;
  font-size: 32px;
  font-weight: 700;
  color: #000000;
  border-bottom: 2px solid #888686;
}
.mypage_sub:hover {
  color: black;
  font-weight: bold;
  cursor: pointer;
}
.mypage_sub {
  width: 200px;
  font-size: 22px;
  padding-top: 10px;
  line-height: 50px;
  color: #888686;
  transition: color 0.2s, font-weight 0.2s;
}
#nowpage {
    color: #000000;
}
.mypage_right {
    float: left;
    width: 1210px;
    height: 1100px;
}
.Option {
  font-size: 32px;
  color: black;
  margin-top: 220px;
  margin-left: 140px;
}
.option_line1 {
  width: 1199px;
  border: 3px solid #FFBEBA;
  margin-left: 125px;
  margin-top: 20px;
}
.option_menuwrap {
  display: flex;
  align-content: space-between;
  width: 321px;
  height: 100px;
  margin-left: 125px;
  margin-top: 20px; 
}
.option_interestpage {
  width: 1199px;
  height: 679px;
  border-radius: 8px;
  background-color: #FFFFFF;
  border: 1px solid #FFBEBA;
  box-shadow: inset 0px -1px 1px 1px rgba(0, 0, 0, 0.5);
  margin-top: -60px;
  margin-left: 125px;
  z-index: 2;
  position: relative;
}
.interestpage_headwrap {
  display: flex;
  align-content: space-between;
  width: 1175px;
  height: 50px;
  margin-left: 20px;
  margin-top: 35px;
}
.interest_combobox {
    font-size: 15px;
    text-align: center;
    border-radius: 5px;
    border: 1px solid #515151;
    width: 118px;
    height: 27px;
}
.interest_list {
  display: flex;
  align-content: space-between;
  flex-wrap: wrap;
  width: 1175px;
  height: 590px;
  margin-left: 20px;
}
.interest_ul {
  display: flex;
  flex-wrap: wrap;
  padding: 0;
  margin: 0;
  list-style-type: none;
  width: 100%;
}
.interest_wrap {
  display: flex;
  align-content: space-between;
  width: 202px;
  height: 93px;
  margin: 10px;
}
.form-check-input-interest {
  margin: auto;
  width: 39px;
  height: 39px;
  border-radius: 5px;
  background-color: white;
  border: 1px solid #FFBEBA;
  cursor: pointer;
}
.lan_text {
  margin: auto;
  width: 95px;
  height: 39px;
  font-size: 30px;
  color: #515151;
  font-weight: bold;
  margin-left: 5px;
}
.alarm_page {
  width: 1199px;
  height: 679px;
  border-radius: 8px;
  background-color: #FFFFFF;
  border: 1px solid #FFBEBA;
  box-shadow: inset 0px -1px 1px 1px rgba(0, 0, 0, 0.5);
  margin-top: -60px;
  margin-left: 125px;
  z-index: 2;
  position: relative;
}
.alarm_wrap {
  display: flex;
  align-content: space-between;
  width: 1400px;
  height: 100px;
  margin-left: 45px;
  margin-top: 10px;
}
.alarm_lan {
  width: auto;
  max-width: 400px;
  display: inline-block;
  overflow: hidden;
  height: 26px;
  font-size: 20px;
  color: black;
  margin-top: 28px;
}
.alarm_wall {
  width: 2px;
  height: 26px;
  background-color: #FFBEBA;
  border: 1px solid #FFBEBA;
  margin-left: 5px;
  margin-top: 30px;
}
.alarm_info {
  width: auto;
  max-width: 800px;
  overflow: hidden;
  display: inline-block;
  height: 26px;
  font-size: 20px;
  color: #727272;
  margin-top: 28px;
  margin-left: 5px;
}
#off_switch {
  margin-top: 30px;
  margin-left: 6px;
}
#on_switch {
  margin-top: 30px;
  margin-left: 6px;
}
.alarm_line {
  width: 1070px;
  height: 1px;
  border: 1px solid #FAE9E9;
  margin-left: 45px;
}
</style>