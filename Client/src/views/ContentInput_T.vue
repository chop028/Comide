<template>
  <quill-editor
      class="content_input_box"
      v-model:value="state.content"
      :options="state.editorOption"
      @change="onEditorChange($event)"
  >
  </quill-editor>
</template>

<script>
import { reactive } from 'vue'
export default {
  props: ['value'],
  name: 'App',
  setup(props, { emit }) {
    const state = reactive({
      content: '',
      editorOption: {
        placeholder: '내용을 입력해주세요...',
        modules: {
          toolbar: [
            ['bold', 'italic', 'underline', 'strike'],
            ['blockquote', 'code-block'],
            [{ header: 1 }, { header: 2 }],
            [{ list: 'ordered' }, { list: 'bullet' }],
            [{ script: 'sub' }, { script: 'super' }],
            [{ indent: '-1' }, { indent: '+1' }],
            [{ direction: 'rtl' }],
            [{ size: ['small', false, 'large', 'huge'] }],
            [{ header: [1, 2, 3, 4, 5, 6, false] }],
            [{ color: [] }, { background: [] }],
            [{ font: [] }],
            [{ align: [] }],
            ['image', 'link'],
            ['clean'],
          ],
        },
      },
      disabled: false,
    });

    const onEditorChange = ({ html }) => {
      state.content = html;
      emit('update:modelValue', state.content);
    };

    return { state, onEditorChange };
  },
  data() {
    return {
      contentInput: this.value
    };
  },
  watch: {
    value(newValue) {
      this.contentInput = newValue;
    }
  },
  methods: {
    updateContentInput(event) {
      this.$emit('input', event.target.value);
    },
  },
  mounted() {
  }
};
</script>

<style scoped>
.ql-container {
    font-size: 18px;
}
.summernote{
  margin-top: 0;
}
</style>