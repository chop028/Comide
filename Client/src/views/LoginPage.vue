<template>
  <div class="loginbox">
    <div class="logobox">
      <img src="@/assets/Logo.svg" width="300px" height="300px">
    </div>
    <a @click="loginWithGoogle" style="text-decoration-line: none;">
      <div class="googlebox">
        <img src="@/assets/google.png" style="width: 45px; height: 45px; margin-left: 30px; margin-bottom: 5px;">
        <span style="color: rgb(70, 70, 70); margin-left: 60px; font-size: 30px;">Google Login</span>
      </div>
    </a>
  </div>
</template>

<script>
import { useRouter } from 'vue-router';
import { useAuthStore } from '../store/auth';

export default {
  setup() {
    const router = useRouter();
    const authStore = useAuthStore();

    const loginWithGoogle = () => {
      window.location.href = `${process.env.VUE_APP_API_URL}/oauth2/authorization/google`;
    };

    if (window.location.href.includes('token')) {
      const token = new URLSearchParams(window.location.search).get('token');
      const userInfo = JSON.parse(new URLSearchParams(window.location.search).get('userInfo'));
      authStore.setAuth(token, userInfo);
      router.push('/');
    }

    return {
      loginWithGoogle,
    };
  },
};
</script>
  
<style scoped>
.loginbox{
    margin: auto;
    padding-top: 70px;
    margin-top: 200px;
    width: 700px;
    height: 700px;
    border: 5px solid #FAE9E9;
    background-color: #fdf7f7;
}
.logobox{
    margin-left: 200px;
    width: 300px;
    height: 300px;
}
.googlebox{
    padding-top: 16px;
    margin-top: 100px;
    margin-left: 155px;
    width: 400px;
    height: 80px;
    border: none;
    background-color:white;
    background: linear-gradient(180deg, rgb(255, 255, 255) 30%, rgb(246, 246, 246) 80%);
    border-radius: 10px;
    box-shadow: 0 3px 5px rgba(0, 0, 0, 0.3);
}
.googlebox:hover{
    background: #f3f3f3;
}
</style>