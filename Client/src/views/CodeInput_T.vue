<template>
  <div 
    class="code-input-container"
    @dragover.prevent="handleDragOver"
    @drop.prevent="handleDrop"
  ><pre v-highlightjs style="margin: 0;"><code class="java">{{ defaultData }}</code></pre>
  </div>
</template>

<script>
import apiClient from '@/axios/axios';
import hljs from 'highlight.js';
import 'highlight.js/styles/atom-one-dark-reasonable.css';
export default {
  data() {
    return {
      defaultData: "코드를 이곳에 드래그 하거나 파일을 여세요.",
    }
  },
  props:{
    codeId: {
      type: String,
      required: true
    },
  },
  methods: {
    highlightCode() {
      this.$nextTick(() => {
        hljs.highlightElement(this.$refs.code);
      });
    },
    loadCodeData(){
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/code-editor/get-code-detail.do?codeId=${this.codeId}`)
        .then((response) => {
        if (response.data.success) {
            this.defaultData = response.data.result.content;
        } else {
            console.error("코드 받아오기 실패");
        }
      })
      .catch(function(error) {
      console.error(error);
      });
    },
    handleDragOver(event) {
      event.dataTransfer.dropEffect = 'copy';
    },
    handleDrop(event) {
      const files = event.dataTransfer.files;
      if (files.length > 0) {
        const file = files[0];
        this.readFile(file);
      }
    },
    readFile(file) {
      const reader = new FileReader();
      reader.onload = e => {
        this.processFileContent(e.target.result, file.type);
      };
      
      if (file.type.startsWith('image/')) {
        reader.readAsDataURL(file);
      } else if (file.type === 'application/pdf') {
        reader.readAsBinaryString(file);
      } else if (file.type === 'text/plain') {
        reader.readAsText(file);
      } else {
        alert('지원하지 않는 파일 형식입니다.');
      }
    },
    processFileContent(content, type) {
      let fileContent;
      if (type.startsWith('image/')) {
        fileContent = `이미지 파일: ${content.slice(0, 30)}...`; // 이미지 파일은 데이터 URL로 표시
      } else if (type === 'application/pdf') {
        fileContent = `PDF 파일: ${content.length} bytes`; // PDF 파일의 바이트 길이 표시
      } else {
        fileContent = content; // 텍스트 파일은 내용 표시
      }
      this.$emit('file-dropped', fileContent); // 파일 내용을 부모 컴포넌트로 전달
    },
  },
  mounted(){
    this.loadCodeData();
  }
}
</script>

<style scoped>
.code-input-container {
  border: 2px dashed #ccc;
  padding: 10px;
  position: relative;
  height: 100%;
  color: #828282;
}
.code-input-container.has-content {
  border-color: blue;
}
</style>