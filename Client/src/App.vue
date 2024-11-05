<template>
  <div>
    <Header></Header>
    <router-view></router-view>
    <Fbtn v-if="isTokenExists"></Fbtn>
  </div>
</template>

<script>
import Header from './components/Header.vue';
import Fbtn from './components/float/Floating_F.vue';
import { useAuthStore } from '@/store/auth';
import { useNotificationStore } from '@/store/NotificationStore';

export default {
  name: 'App',
  components: {
    Header,
    Fbtn,
  },
  computed: {
    isTokenExists() {
      const authStore = useAuthStore();
      return authStore.token !== null;
    }
  },
  beforeMount() {
    const authStore = useAuthStore();
    authStore.loadAuthFromLocalStorage();

    const notificationStore = useNotificationStore();
    if (authStore.userInfo?.id) {
      notificationStore.connectToWebSocket(authStore.userInfo.id);
    }
  },
  beforeUnmount() {
    const notificationStore = useNotificationStore();
    notificationStore.disconnectWebSocket();
  },
};
</script>

<style>
</style>
