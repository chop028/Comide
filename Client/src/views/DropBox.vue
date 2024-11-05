<template>
  <div
    v-if="hasCode"
    class="draggable-box"
    :style="{ left: boxLeft + 'px' }"
    draggable="true"
    @dragstart="onDragStartBox"
    @dragend="onDragEndBox"
  >
    <pre v-highlightjs><code class="java">{{ codeData }}</code></pre>
  </div>
</template>

<script>
import apiClient from '@/axios/axios';
import hljs from 'highlight.js';
import 'highlight.js/styles/atom-one-dark-reasonable.css';
export default {
  props: {
    codeId: {
      type: String,
      required: true,
    }
  },
  data() {
    return {
      boxLeft: 0,
      codeData: ''
    };
  },
  computed: {
    hasCode() {
      return this.codeData.length > 0;
    }
  },
  methods: {
    highlightCode() {
      this.$nextTick(() => {
        hljs.highlightElement(this.$refs.code);
      });
    },
    getCodeData(){
      apiClient
          .get(`${process.env.VUE_APP_API_URL}/api/code-editor/get-code-detail.do?codeId=${this.codeId}`)
          .then((response) => {
          if (response.data.success) {
              this.codeData = response.data.result.content;
          } else {
              console.error("코드 받아오기 실패!");
          }
        })
        .catch(function(error) {
        console.error(error);
        });
    },
    onDragStartBox(event) {
      event.dataTransfer.setData("text/plain", ""); // 드래그 시 브라우저 기본 이벤트 방지
      event.dataTransfer.effectAllowed = "move"; // 드래그 효과 설정
      event.dataTransfer.setData("text/box-left", this.boxLeft); // 드래그 시작 시 박스의 왼쪽 위치 정보 전달
      event.dataTransfer.setData("text/box-top", this.boxTop); // 드래그 시작 시 박스의 위쪽 위치 정보 전달
    },
    onDragEndBox(event) {
      // 드래그 종료 시, 박스의 위치 정보 업데이트
      this.boxLeft = parseInt(event.target.style.left);
      this.boxTop = parseInt(event.target.style.top);
    }
  },
  mounted(){
    this.getCodeData();
  }
};
</script>

<style scoped>
.draggable-box {
  display: block;
  cursor: move;
  width: 1000px;
  height: auto;
  max-width: 100%; 
  overflow: auto;
  border-radius: 10px;
}

pre {
  margin: 0; 
  white-space: pre-wrap; 
  font-size: 16px;
}
</style>