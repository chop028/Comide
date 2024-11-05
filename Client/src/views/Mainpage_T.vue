<template>
  <router-view></router-view>
  <div class="main_wrap">
    <div class="devInfoCard">
      <img src="@/assets/previcon.svg" width="60px" height="60px" id="car_prev" @click="prevSlide">
      <div id="customCarousel" class="carousel">
        <div v-for="(item, index) in carouselItems" :key="index" class="carousel-item" :class="{ active: index === currentIndex }">
          <img :src="item.image" class="d-block w-100 carousel-image" :alt="'Slide ' + (index + 1)">
          <div class="carousel-caption">
            <p id="car_header">{{ item.header }}</p>
            <p id="car_context">{{ item.context }}</p>
            <p id="car_date">{{ item.date }}</p>
          </div>
        </div>
        
        <div class="carousel-indicators">
          <button v-for="(item, index) in carouselItems" :key="index" :class="{ active: index === currentIndex }" @click="goToSlide(index)"></button>
        </div>
      </div>
      <img src="@/assets/nexticon.svg" width="60px" height="60px" id="car_next" @click="nextSlide">
    </div>

    <div class="coop-content">
      <div class="coop-text">협업 모집</div>
      <div class="coop-line"></div>
      <div class="row row-cols- g-4" id="cards">
        <div class="col" v-for="(card, index) in coopList" :key="index">
          <div class="card h-100" id="card">
            <div class="card-body" @click="getPostDetail(card.board.id)">
              <div class="category-wrap">
                <div v-for="(tag, tagIndex) in card.hashTags.slice(0, 3)" :key="tagIndex" class="hashtag_main">{{ tag.content }}</div>
              </div>
              <br>
              <h5 class="card-time">마감일 [{{ card.groupInfo?.endDate || '' }}]</h5>
              <h3 class="card-title">{{ card.board?.title || '' }}</h3>
              <br>
              <div class="icon-wrap">
                <div v-for="langObj in card.groupLangList" :key="langObj.lang" :class="['card-icon', 'rounded-pill']" :style="getBorderStyle(langObj.lang)">
                  <img :src="getIconPath(langObj.lang)" alt="" class="card-icon-img">
                </div>
              </div>
              <hr class="card-hr">
              <div class="foot-wrap">
                <img class="user-icon rounded-pill" :src="card.userImg || ''" alt="User Icon">
                <p class="user-name">{{ card.userName || '' }}</p>
                <div class="coop-comment-icon"></div>
                <p class="coop-comment-num">{{ card.board?.viewCnt || '' }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="hotpost-wrap">
      <div class="hotpost-text">최신글</div>
      <div class="hotpost-line"></div>
      <div v-for="(post, index) in hotposts.slice(0, 3)" :key="index" :class="'hotpost' + (index + 1)">
        <div class="hotpost-category">{{ post.category || '' }}</div>
        <p class="hotpost-name" @click="getPostDetail(post.id)">{{ post.title }}</p>
        <div class="hotpost-eye-icon"></div>
        <p class="hotpost-eye-num">{{ post.viewCnt }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import apiClient from '@/axios/axios';

export default {
  data() {
    return {
      borderColorMap: {
        c: '#669BD3',
        cpp: '#669BD3',
        cs: '#8E63CE',
        html: '#F36518',
        css: '#0067F5',
        spring: '#70AD51',
        js: '#F5DD1E',
        java: '#F89B22',
      },
      currentIndex: 0,
      autoSlideInterval: null,
      carouselItems: [
        {
          image: require('@/assets/carousel_IMG1.png'),
          header: '개발자의 역량을 강화해주는 새로운 사이트 공개',
          context: 'BCU의 천재 학생들이 오늘 혁신적인 신규 사이트를 공개했다. 이 사이트는 전 세계 개발자가 Window, macOS 전반에서 더욱 강력하고 효율적인 개발을할 수 있도록 마련되었다.',
          date: '2024.06.24'
        },
        {
          image: require('@/assets/carousel_IMG2.png'),
          header: '개인용 인공 지능 시스템인 Apple Intelligence 공개',
          context: 'Apple이 오늘 iPhone, iPad, Mac을 위한 개인용 지능 시스템인 Apple Intelligence 를 발표했다. iOS 18, iPadOS 18, macOS Sequoia에 심어지는 Apple Intelligence는 강력한 생성형 모델에 사용자의 상황과 맥락을 이해하는 능력을 더해 놀랍도록 유용하고 유의미한 지능형 기능을 제공한다.',
          date: '2024.06.10'
        },
        {
          image: require('@/assets/carousel_IMG3.png'),
          header: '제10회 대학생 프로그래밍 경진대회’ 참가자 모집',
          context: '삼성전자가 제10회 ‘삼성전자 대학생 프로그래밍 경진대회(Samsung Collegiate Programming Cup, SCPC)’ 참가자를 모집한다. 삼성전자는 소프트웨어 생태계 확대와 우수인재 발굴을 위해 2015년부터 매년 ‘삼성전자 대학생 프로그래밍 경진대회(SCPC)’를 개최했으며, 올해로 10회를 맞이했다.',
          date: '2024.06.04'
        }
      ],
      coopList: [],
      hotposts: []
    };
  },
  methods: {
    getBorderStyle(lang) {
      const color = this.borderColorMap[lang];
      return color ? `border: 3px solid ${color};` : '';
    },
    prevSlide() {
      this.currentIndex = (this.currentIndex === 0) ? this.carouselItems.length - 1 : this.currentIndex - 1;
    },
    nextSlide() {
      this.currentIndex = (this.currentIndex === this.carouselItems.length - 1) ? 0 : this.currentIndex + 1;
    },
    goToSlide(index) {
      this.currentIndex = index;
    },
    startAutoSlide() {
      this.autoSlideInterval = setInterval(this.nextSlide, 5000);
    },
    stopAutoSlide() {
      clearInterval(this.autoSlideInterval);
    },
    getIconPath(lang) {
      try {
        return require(`@/assets/main-icon/icon-main-${lang}.png`);
      } catch (e) {
        return '@/assets/main-icon/default-icon.png';
      }
    },
    getCoopList() {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/board/get-main-coop.do`)
        .then((response) => {
          if (response.data.success) {
            this.coopList = response.data.result;
          } else {
            console.error("get-board-list failed");
          }
        })
        .catch((error) => {
          console.error(error);
        });
    },
    getPostDetail(postid) {
      this.$router.push({ name: 'PostDetail', params: { id: postid } });
    },
    getBoardList() {
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/board/get-board-list.do`)
        .then((response) => {
          if (response.data.success) {
            this.hotposts = response.data.result
              .filter(post => post.category !== '협업모집')
              .slice(-3)
              .reverse();
          } else {
            console.error("get-board-list failed");
          }
        })
        .catch((error) => {
          console.error(error);
        });
    }
  },
  mounted() {
    this.startAutoSlide();
    this.getCoopList();
    this.getBoardList();
  }
};
</script>

<style>
.devInfoCard {
  display: flex;
  justify-content: center;
  align-items: center;
}
#car_prev {
  display: inline-block;
  margin-right: 20px;
  filter: invert(40%) sepia(0%) saturate(1702%) hue-rotate(351deg) brightness(98%) contrast(92%);
}
#car_prev:hover {
  filter: invert(7%) sepia(3%) saturate(43%) hue-rotate(341deg) brightness(90%) contrast(85%);
  cursor: pointer;
}
#car_next {
  display: inline-block;
  margin-left: 20px;
  filter: invert(40%) sepia(0%) saturate(1702%) hue-rotate(351deg) brightness(98%) contrast(92%);
}
#car_next:hover {
  filter: invert(7%) sepia(3%) saturate(43%) hue-rotate(341deg) brightness(90%) contrast(85%);
  cursor: pointer;
}
.main_wrap{
  margin: auto;
  margin-top: 230px;
  width: 100%;
  height: 1600px;
}
.coop-content{
  margin: auto;
  width: 1500px;
}
.hotpost-wrap {
  margin: auto;
  width: 1500px;
}
.color-nav{
  background-color: #f9e9e9;
}
.carousel {
  position: relative;
  width: 1500px;
  overflow: hidden;
  border: 3px solid #FF9A9A;
  border-radius: 15px;
  background-color: #f8f9fa;
}

.carousel-item {
  display: none;
  flex-direction: row;
  width: 100%;
  height: 400px;
  transition: transform 0.5s ease-in-out;
}

.carousel-item.active {
  display: flex;
}
.carousel-image {
  width: 70%;
  height: 100%;
}
.carousel-caption {
  width: 30%;
  height: 100%;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  top: 0;
  left: 0;
  background: rgb(255,225,225);
  background: linear-gradient(180deg, rgba(255,225,225,1) 1%, rgba(255,255,255,1) 60%);
  border-radius: 0px 10px 10px 0px;
  box-sizing: border-box;
}
#car_header{
  font-weight: bold;
  font-size:28px;
  color: #515151;
  text-align: left;
  margin-left: 15px;
  height: auto;
  max-height: 108px;
  overflow: hidden;
}
#car_context{
  font-size: 20px;
  color: black;
  text-align: left;
  margin-left: 15px;
  margin-top: 30px;
  max-height: 90px;
  overflow: hidden;
  height: auto;
}
#car_date{
  font-size: 16px;
  color: #838383;
  text-align: left;
  margin-left: 15px;
}
.carousel-control-prev {
  position: absolute;
  top: 40%;
  left: 9%;
  transform: translateY(-50%);
  border: none;
  cursor: pointer;
  border-radius: 50%;
  width: 30px;
  height: 30px;
}
.carousel-control-next {
  position: absolute;
  top: 40%;
  left: 89%;
  transform: translateY(-50%);
  border: none;
  cursor: pointer;
  border-radius: 50%;
  width: 30px;
  height: 30px;
}
.carousel-indicators {
  position: absolute;
  bottom: 10px;
  left: 38%;
  transform: translateX(-50%);
  display: flex;
}

.carousel-indicators button {
  background-color: rgba(255, 255, 255, 0.5);
  border: none;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  margin: 0 5px;
  cursor: pointer;
}

.carousel-indicators button.active {
  background-color: rgba(255, 255, 255, 1);
}
.coop-text {
  width: 160px;
  margin-left: 120px;
  margin-top: 50px;
  font-weight: 600;
  color: #515151;
  font-size: 28px;
  line-height: normal;
  position: relative;
  font-family: "Inter", Helvetica;
  letter-spacing: 0;
  font-weight: bold;
}
.coop-line{
  margin-left: 280px;
  margin-top: -20px;
  width: 1083px;
  border: 1px solid #FF9A9A;
  position: relative;
}
#cards{
margin-top: 0px;
margin-left: 75px;
width: 1300px;
}
#card{
  width: 381px;
  height: 281px;
  border-radius: 30px;
  border: 4px solid #FFBBBB;
}
#card:hover{
  cursor: pointer;
  border: 4px solid #ff8989;
}
.col{
  margin-left: 25px;
  margin-top: 45px;
}
.category-wrap{
  display: flex;
  align-content : space-between;
  width: 330px;
  height: 21px;
  margin-left: 10px;
  margin-top: 13px;
}
.hashtag_main{
  display: inline-block;
  width: 68px;
  max-width: 100px;
  overflow: hidden;
  height: 21px;
  margin-left: 5px;
  background-color: #FFBBBB;
  border-radius: 30px;
  color: #F55050;
  text-align: center;
  font-size: 12px;
  font-weight: bold;
  line-height: 21px;
}
.card-time{
  display: inline-block;
  margin-left: 10px;
  width: 170px;
  max-width:230px;
  color: #838383;
  font-size: 16px;
}
.card-title{
  display: inline-block;
  margin-left: 10px;
  width: 230px;
  max-width: 300px;
  height: 60px;
  max-height: 60px;
  font-size: 24px;
  font-weight: bold;
  color: #515151;
  overflow: hidden;
}
.icon-wrap{
  display: flex;
  align-content : space-between;
  width: 250px;
  height: 40px;
  margin-left: 10px;
  margin-top:5px;
} 
.card-icon{
  width: 40px;
  height: 40px;
  background-size: cover;
  overflow:hidden;
  margin-right: 10px;
}
.card-icon-img{
  object-fit: cover;
  height: 100%;
  width: 100%;
}
.card-hr{
  margin-left:10px;
  border: 1px solid;
  color: #838383
}
.foot-wrap{
  display: flex;
  align-content : space-between;
  width: 340px;
  height: 26px;
  margin-left: 10px;
}
.user-icon{
  width: 24px;
  height: 24px;
  background-color: #d8bebe;
  background-size: 24px 24px;
}
.user-name{
  display: inline-block;
  width: 150px;
  max-width: 250px;
  height: 20px;
  margin-left: 9px;
  font-size: 16px;
  color: #515151;
  overflow: hidden;
}
.coop-heart-icon{
  float: right;
  width: 24px;
  height: 24px;
  margin-left: 39px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='24' height='24' fill='gray' class='bi bi-heart' viewBox='0 0 16 16'%3E%3Cpath d='m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143q.09.083.176.171a3 3 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15'/%3E%3C/svg%3E");
}
.coop-heart-num{
  text-align: left;
  font-size: 16px;
  color: #838383;
  margin-left: 5px;
  display: inline-block;
  width: 30px;
  max-width: 30px;
  overflow: hidden;
  height: 20px;
}
.coop-comment-icon{
  width: 24px;
  height: 24px;
  float: right;
  margin-left: 100px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='24' height='24' fill='gray' class='bi bi-eye' viewBox='0 0 16 16'%3E%3Cpath d='M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8M1.173 8a13 13 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5s3.879 1.168 5.168 2.457A13 13 0 0 1 14.828 8q-.086.13-.195.288c-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5s-3.879-1.168-5.168-2.457A13 13 0 0 1 1.172 8z'/%3E%3Cpath d='M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5M4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0'/%3E%3C/svg%3E");
}
.coop-comment-num{
  text-align: left;
  font-size: 16px;
  color: #838383;
  margin-left: 5px;
  display: inline-block;
  width: 30px;
  max-width: 30px;
  overflow: hidden;
  height: 20px;
}
.hotpost-text {
  width: 150px;
  margin-left: 120px;
  margin-top: 50px;
  font-weight: 600;
  color: #515151;
  font-size: 28px;
  line-height: normal;
  position: relative;
  font-family: "Inter", Helvetica;
  letter-spacing: 0;
  font-weight: bold;
}
.hotpost-line{
  margin-left: 230px;
  margin-top: -20px;
  width: 1130px;
  border: 1px solid #FF9A9A;
  position: relative;
}
.hotpost1{
  display: flex;
  align-content : space-between;
  margin-left: 120px;
  margin-top: 40px;
  width: 1240px;
  height: 40px;
}
.hotpost-category{
  width: 76px;
  height: 30px;
  text-align: center;
  font-size: 16px;
  color: #FF9A9A;
  border-radius: 5px;
  border: 1px solid #FF9A9A;
  line-height: 30px;
}
.hotpost-name{
  position: relative;
  font-size: 23px;
  margin-top: -5px;
  margin-left: 20px;
  width: 460px;
}
.hotpost-name:hover{
  position: relative;
  font-weight: bold;
  cursor: pointer;
}
.hotpost-heart-icon{
  width: 18px;
  height: 18px;
  margin-left: 570px;
  margin-top: 5px;
  color:#A2A2A2;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='18' height='18' fill='gray' class='bi bi-heart' viewBox='0 0 16 16'%3E%3Cpath d='m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143q.09.083.176.171a3 3 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15'/%3E%3C/svg%3E");
}
.hotpost-heart-num{
  display: inline-block;
  width: 10px;
  max-width: 30px;
  height: 20px;
  text-align: right;
  margin-left:10px;
  color: #A2A2A2;
  font-size: 16px;
}
.hotpost-eye-icon{
  width: 18px;
  height: 18px;
  margin-left:640px;
  margin-top: 5px;
  color:#A2A2A2;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='18' height='18' fill='gray' class='bi bi-eye' viewBox='0 0 16 16'%3E%3Cpath d='M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8M1.173 8a13 13 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5s3.879 1.168 5.168 2.457A13 13 0 0 1 14.828 8q-.086.13-.195.288c-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5s-3.879-1.168-5.168-2.457A13 13 0 0 1 1.172 8z'/%3E%3Cpath d='M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5M4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0'/%3E%3C/svg%3E");
}
.hotpost-eye-num{
  display: inline-block;
  width: 10px;
  max-width: 30px;
  height: 20px;
  text-align: right;
  margin-left:10px;
  color:#A2A2A2;
}
.hotpost2{
  display: flex;
  align-content : space-between;
  margin-left: 120px;
  margin-top: 40px;
  width: 1240px;
  height: 40px;
}
.hotpost3{
  display: flex;
  align-content : space-between;
  margin-left: 120px;
  margin-top: 40px;
  width: 1240px;
  height: 40px;
}
</style>