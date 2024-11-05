<template>
  <div v-if="showSearch && currentCode" class="spotlight-container show">
    <div class="spotlight">
      <input
        ref="searchInput"
        type="text"
        v-model="query"
        @keyup.enter="searchGPT"
        placeholder="궁금한 것이 있으면 물어보세요."
        class="spotlight-input"
      />
    </div>
  </div>
</template>

<script>
import apiClient from '@/axios/axios';

export default {
  name: "SpotlightSearch",
  props: {
    currentCode: {
      type: String,
      required: true
    },
    showSearch: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      query: "",
      isLoading: false
    };
  },
  watch: {
    showSearch(newVal) {
      if (newVal) {
        this.$nextTick(() => {
          this.$refs.searchInput.focus();
        });
      }
    }
  },
  methods: {
    async searchGPT() {
      if (this.query.trim() === "" || !this.currentCode) {
        return;
      }

      const prompt = `다음 코드에 대해 질문합니다:\n\n${this.currentCode}\n\n요구 사항: ${this.query}\n\n모든 답변은 한글로 부탁해`;

      this.$emit('close');
      this.$emit('loading-start');
      this.query = "";

      this.isLoading = true;

      try {
        const response = await apiClient.post(`${process.env.VUE_APP_API_URL}/api/gpt/ask-about-code`, {
          query: prompt
        });

        if (response.status === 200) {
          const analysisContent = response.data.choices[0].message.content;
          this.$emit("gpt-response", analysisContent);
        } else {
          console.error("GPT 응답 처리 중 오류 발생");
        }
      } catch (error) {
        console.error("API 요청 중 오류 발생:", error.message);
      }
    }
  }
};
</script>

<style scoped>
.spotlight-container {
  position: fixed;
  top: 20%;
  left: 50%;
  transform: translate(-50%, 0);
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  z-index: 1000;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.spotlight-container.show {
  opacity: 1;
}

.spotlight {
  width: 50%;
  max-width: 600px;
  height: 65px;
  padding: 15px 25px;
  background: #fff;
  border: none;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
}

.spotlight-input {
  width: 100%;
  padding: 10px;
  font-size: 1.4rem;
  border: none;
  outline: none;
  background: transparent;
  color: #333;
}

.spotlight-input::placeholder {
  color: #aaa;
  font-style: italic;
}
</style>