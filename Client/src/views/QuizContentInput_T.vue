<template>
    <textarea v-model="quizcontentInput" @input="updatequizContentInput" placeholder="내용을 입력하세요." ref="textarea" onkeydown="if(event.keyCode===9){var v=this.value,s=this.selectionStart,e=this.selectionEnd;this.value=v.substring(0, s)+'\t'+v.substring(e);this.selectionStart=this.selectionEnd=s+1;return false;}"></textarea>
  </template>
  
  <script>
  export default {
    props: ['value'],
    data() {
      return {
        quizcontentInput: this.value
      };
    },
    watch: {
      value(newValue) {
        this.quizcontentInput = newValue;
        this.adjustHeight();
      }
    },
    methods: {
      updatequizContentInput(event) {
        this.$emit('input', event.target.value);
        this.adjustHeight();
      },
      adjustHeight() {
        const textarea = this.$refs.textarea;
        textarea.style.height = 'auto';
        textarea.style.height = `${textarea.scrollHeight}px`;
      }
    },
    mounted() {
      this.adjustHeight();
    }
  };
  </script>
  
  <style scoped>
  textarea {
    width: 100%;
    overflow: hidden;
    resize: none;
  }
  </style>