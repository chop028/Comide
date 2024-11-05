import { createApp } from 'vue';
import App from './App.vue';
import { quillEditor } from 'vue3-quill';
import router from "./router/router";
import VueHighlightJS from 'vue3-highlightjs';
import 'highlight.js/styles/intellij-light.css';
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { createPinia } from 'pinia';
import { useAuthStore } from '@/store/auth';
import { useNotificationStore } from '@/store/NotificationStore';
import { install as VueMonacoEditorPlugin } from '@guolao/vue-monaco-editor';
import Toast, { POSITION } from 'vue-toastification';
import '@/assets/css/Toast/Custom-toast.css';
import mitt from 'mitt';

const emitter = mitt();
export default emitter;

const app = createApp(App);
const pinia = createPinia();

app.use(router)
   .use(VueHighlightJS)
   .use(quillEditor)
   .use(pinia)
   .use(VueMonacoEditorPlugin, {
     paths: {
       vs: 'https://cdn.jsdelivr.net/npm/monaco-editor@0.43.0/min/vs'
     }
   })
   .use(Toast, {
     position: POSITION.TOP_RIGHT,
     timeout: 5000,
     closeOnClick: true,
     pauseOnHover: true,
     rtl: false
   })
   .mount('#app');

const authStore = useAuthStore();
authStore.loadAuthFromLocalStorage();

const notificationStore = useNotificationStore();
const urlParams = new URLSearchParams(window.location.search);
const token = urlParams.get('token');
const userInfo = {
  id: urlParams.get('id'),
  googleId: urlParams.get('googleId'),
  name: decodeURIComponent(urlParams.get('name')),
  email: urlParams.get('email'),
  picture: urlParams.get('picture'),
  comment: decodeURIComponent(urlParams.get('comment')),
  role: urlParams.get('role'),
};

if (token && userInfo.id) {
  authStore.setAuth(token, userInfo);
  notificationStore.connectToWebSocket(userInfo.id);

  router.push('/');
}

window.app = app;