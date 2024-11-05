import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: null,
    userInfo: null,
  }),
  actions: {
    setAuth(token, userInfo) {
      this.token = token;
      this.userInfo = userInfo;
      localStorage.setItem('token', token);
      localStorage.setItem('userInfo', JSON.stringify(userInfo));
    },
    clearAuth() {
      this.token = null;
      this.userInfo = null;
      localStorage.removeItem('token');
      localStorage.removeItem('userInfo');
    },
    loadAuthFromLocalStorage() {
      const token = localStorage.getItem('token');
      const userInfo = localStorage.getItem('userInfo');
      
      if (token && userInfo) {
        const parsedUserInfo = JSON.parse(userInfo);

        if (parsedUserInfo.id) {
          parsedUserInfo.id = Number(parsedUserInfo.id);
        }

        this.token = token;
        this.userInfo = parsedUserInfo;
      }
    },
  },
});
