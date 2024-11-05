<template>
  <div class="editor-box" @click.self="deselectFolder">
    <div class="info-options">
      <p class="projectName" v-if="!isLabProject">{{ projectName }}</p>
      <p class="mode-name">{{ mode }}</p>
    </div>
    <div class="side-bar">
      <div class="devtool-box">
        <button @click="runCode" class="dev-button" title="코드 실행">
          <img class="devImg" src="../../src/assets/dev-tool/play_arrow.svg" />
        </button>
        <button @click="openNewFolderModal" class="dev-button" title="폴더 추가">
          <img class="devImg" src="../../src/assets/dev-tool/create_new_folder.svg" />
        </button>
        <button @click="openNewFileModal" class="dev-button" title="파일 추가">
          <img class="devImg" src="../../src/assets/dev-tool/note_add.svg" />
        </button>

        <button class="dev-button" @click="openCodeModal" title="폴더 열기">
          <img class="devImg" src="../../src/assets/dev-tool/folder_open.svg" />
        </button>

        <button class="dev-button" @click="deleteItem" title="삭제">
          <img class="devImg" src="../../src/assets/dev-tool/delete.svg" />
        </button>

        <button v-if="!isLabProject" class="dev-button" @click="openSaveModal" title="저장하기">
          <img class="devImg" src="../../src/assets/dev-tool/Save-05.svg" style="width: 18px; height: 18px; margin-left: 4px; margin-bottom: 3px;" />
        </button>

        <button v-if="!isLabProject" class="dev-button" @click="openGitModal" title="버전 관리">
          <img class="devImg" src="../../src/assets/dev-tool/Git-Icon-Black.svg" style="width: 20px; height: 20px; margin-bottom: 2px;" />
        </button>

        <button v-if="!isLabProject" class="dev-button" @click="toggleSpotlightSearch" title="AI 질의 응답">
          <img class="devImg" src="../../src/assets/dev-tool/OpenAI.svg" style="width: 20px; height: 20px; margin-bottom: 1px;" />
        </button>
      </div>

      <div class="folder_explorer" @click.stop>
        <FileExplorer :nodes="fileTree" @file-selected="onFileSelected" />
      </div>
    </div>
    <div class="tabs">
      <div
        v-for="(tab, index) in openFiles"
        :key="index"
        class="tab"
        :class="{ active: activeTab === index }"
        @click="selectTab(index)"
      >
        {{ tab.name }}
        <button @click.stop="closeTab(index)">x</button>
      </div>
    </div>
    <vue-monaco-editor
      class="code_editor"
      v-if="activeTab !== null && openFiles[activeTab]"
      :value="openFiles[activeTab].content"
      :width="config.width"
      :height="config.height"
      :defaultLanguage="config.language"
      :theme="config.theme"
      :options="editorOptions"
      @change="updateContent"
    />

    <SpotlightSearch
      :showSearch="showSpotlight"
      :currentCode="openFiles[activeTab]?.content"
      @close="toggleSpotlightSearch"
      @gpt-response="handleGptResponse"
      @loading-start="startLoadingMessage"
    />

    <!-- 결과보기/분석하기 버튼 -->
    <div class="code_result">
      <div class="view-toggle">
        <button @click="setActiveView('result')" :class="{ active: activeView === 'result' }">
          결과보기
        </button>
        <button @click="setActiveView('analysis')" :class="{ active: activeView === 'analysis' }">
          AI 답변
        </button>
      </div>
      
      <!-- 결과보기 탭 -->
      <div v-if="activeView === 'result'">
        {{ resultData }}
      </div>

      <!-- 분석하기 탭 -->
      <div v-if="activeView === 'analysis'">
        <p v-if="isLoading" class="loading-message">{{ loadingMessage }}</p>

        <div v-else v-html="renderedMarkdown"></div>
      </div>
    </div>

    <!-- 모달 컴포넌트 -->
    <FolderModal v-if="isNewFolderModalOpen" :isOpen="isNewFolderModalOpen" @create-folder="addNewFolder" @close-modal="closeFolderModal" />
    <FileModal v-if="isNewFileModalOpen" :isOpen="isNewFileModalOpen" @create-file="addNewFile" @close-modal="closeFileModal" />
    <GitModal v-if="isGitModalOpen" :projId="this.projectDto.id" @close-modal="closeGitModal" @closeWithSelection="handleGitSelection" />
    <CodeModal v-if="isCodeModalOpen" @close="closeCodeModal" @closeWithSelection="handleFolderSelection" />
    <SaveModal v-if="isSaveModalOpen" @close-modal="closeSaveModal" @closeWithSelection="handleSaveSelection"/>
    <CreateProject v-if="isCreateProjectOpen" :isGroup="this.isGroupProj" :isOpen="isCreateProjectOpen" @create-project="handleProjSelection" @close-modal="closeCreateProjectModal" />
    <ImportProject v-if="isImportProject" :isOpen="isImportProject" @load-project="loadProject" @close-modal="closeImportProjectModal" />
    <CodeEditerOpModal v-if="isCodeEditerOpModal" :isOpen="isCodeEditerOpModal" @new-file="openCreateProject" @load-file="openCodeModal" @select-lab="openLabMod"/>
  </div>
</template>

<script>
import FileExplorer from "@/views/CodeEditor_File/FileExplorer.vue";
import apiClient from "@/axios/axios";
import moment from "moment";
import { marked } from 'marked';
import { VueMonacoEditor } from "@guolao/vue-monaco-editor";
import FolderModal from "@/views/modal/AddNewFolder.vue";
import CreateProject from "@/views/modal/ProjectModal.vue";
import FileModal from "@/views/modal/AddNewFile.vue";
import GitModal from "../views/modal/OpenViewGit.vue";
import CodeModal from "../views/CodeModal_T.vue";
import SaveModal from "../views/modal/OpenSaveModal.vue";
import CodeEditerOpModal from "../views/modal/CodeEditOp.vue";
import { useAuthStore } from '@/store/auth';
import ImportProject from "@/views/modal/ImportProject.vue";
import emitter from '@/eventBus/eventBus.js';
import SpotlightSearch from "@/components/SpotlightSearch.vue";

export default {
  components: {
    FileExplorer,
    VueMonacoEditor,
    FolderModal,
    FileModal,
    GitModal,
    CodeModal,
    SaveModal,
    CreateProject,
    ImportProject,
    CodeEditerOpModal,
    SpotlightSearch
  },
  data() {
    const authStore = useAuthStore();
    return {
      config: {
        width: "1000px",
        height: "600px",
        theme: "vs-light",
        language: "java",
      },
      editorOptions: {
        scrollBeyondLastLine: false,
        fontSize: 20,
      },
      projectDto: {},
      openFiles: [],
      activeTab: null,
      resultData: "",
      showSpotlight: false,
      analysisData: "",
      isLoading: false,
      loadingMessage: '분석중',
      loadingInterval: null,
      activeView: 'result',
      selectedItem: null,
      fileTree: [],
      selectedFolder: null,
      isNewFolderModalOpen: false,
      isCreateProjectOpen: false,
      isImportProject: false,
      openedFromManageModal: false,
      projectFiles: [],
      isNewFileModalOpen: false,
      isGitModalOpen: false,
      isCodeModalOpen: false,
      selectedCodeId: null,
      isSaveModalOpen: false,
      isCodeEditerOpModal: false,
      isOpenCode: false,
      userId: authStore.userInfo?.id || '',
      isGroupProj: false,
      groupId: null,
      isLabProject: true,
      mode: '',
      projectName: '',
    };
  },
  computed: {
    renderedMarkdown() {
      return marked(this.analysisData);
    },
  },
  watch: {
    '$route.query.openCreateProject'(newValue) {
      if (newValue === 'true') {
        if (!confirm("기존 작업 중인 내용이 사라질 수 있습니다.\n반드시 저장 후 확인을 눌러주세요.")) {
          this.openedFromManageModal = false;
          this.$router.push({ query: { openCreateProject: null } });
          return;
        }
        this.isCodeEditerOpModal = false;
        this.groupId = this.$route.query.groupId;
        this.isGroupProj = true;
        this.isCreateProjectOpen = true;
      }
    },
    '$route.query.openImportProject'(newValue) {
      if (newValue === 'true') {
        if (!confirm("기존 작업 중인 내용이 사라질 수 있습니다.\n반드시 저장 후 확인을 눌러주세요.")) {
          this.openedFromManageModal = false;
          this.$router.push({ query: { openImportProject: null } });
          return;
        }
        this.isCodeEditerOpModal = false;
        this.isImportProject = true;
      }
    },
    '$route.query.openGroupProject'(newValue) {
      if (newValue === 'true') {
        if (!confirm("기존 작업 중인 내용이 사라질 수 있습니다.\n반드시 저장 후 확인을 눌러주세요.")) {
          this.openedFromManageModal = false;
          this.$router.push({ query: { openGroupProject: null } });
          return;
        }
        this.projectDto = {
          id: this.$route.query.projectId,
          name: this.$route.query.projectName,
        };
        this.getFileSystem(this.$route.query.projectId);
        if(this.$route.query.isPersonalDrive) {
          this.mode = '개인 프로젝트';
        } else {
          this.mode = this.$route.query.groupName;
        }
        this.isCodeEditerOpModal = false;
        this.projectName = this.$route.query.projectName;
        this.isLabProject = false;
        this.openedFromManageModal = false;
        this.$router.push({ query: { openGroupProject: null } });
      }
    },
  },
  mounted() {
    window.addEventListener("keydown", this.handleShortcut);
    emitter.on('open-create-project-from-manage', this.openCreateProject);
    emitter.on('open-create-project-from-manage', this.setOpenedFromManageModal);
    emitter.on('open-load-project-from-manage', this.setOpenedFromManageModal);
    emitter.on('open-group-project', this.setOpenedFromManageModal);
    emitter.on('open-code', this.setOpenedCode);
    if(this.$route.query.openCode === 'true') {
      this.isGroupProj = true;
      this.isCodeEditerOpModal = false;
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/code-editor/get-code-detail.do?codeId=${this.$route.query.codeId}`)
        .then((response) => {
          if (response.data.success) {
            this.addNewFile("FILE", response.data.result.content);
          } else {
            console.error("get-board-list failed");
          }
        })
        .catch(function (error) {
          console.error(error);
        });
      
    }
    if (this.$route.query.openCreateProject === 'true') {
      this.isCodeEditerOpModal = false;
      this.groupId = this.$route.query.groupId;
      this.isGroupProj = true;
      this.isCreateProjectOpen = true;
    }
    if (this.$route.query.openImportProject === 'true') {
      this.isCodeEditerOpModal = false;
      this.isImportProject = true;
    }
    if (this.$route.query.openGroupProject === 'true') {
      this.isCodeEditerOpModal = false;
      this.isGroupProj = true;
      this.projectDto = {
        id: this.$route.query.projectId,
        name: this.$route.query.projectName,
      };
      this.getFileSystem(this.$route.query.projectId);
      if(this.$route.query.isPersonalDrive) {
        this.mode = '개인 프로젝트';
      } else {
        this.mode = this.$route.query.groupName;
      }
      this.projectName = this.$route.query.projectName;
      this.openedFromManageModal = false;
      this.isLabProject = false;
      this.$router.push({ query: { openCreateProject: null } });
    }
    if(!this.isGroupProj) {
      this.openCodeEditOp();
    }
  },
  beforeUnmount() {
    window.removeEventListener("keydown", this.handleShortcut);
    emitter.off('open-create-project-from-manage', this.openCreateProject);
    emitter.off('open-create-project-from-manage', this.setOpenedFromManageModal);
    emitter.off('oopen-load-project-from-manage', this.setOpenedFromManageModal);
    emitter.off('open-group-project', this.setOpenedFromManageModal);
    emitter.off('open-code', this.setOpenedCode);
  },
  methods: {
    analyzeCode() {
      if (this.activeTab !== null) {
        const code = this.openFiles[this.activeTab].content;

        this.isLoading = true;  // 로딩 상태 설정
        this.startLoadingMessage();  // 로딩 메시지 시작

        apiClient.post(`${process.env.VUE_APP_API_URL}/api/gpt/analyze-code`, { content: code })
          .then((response) => {
            if (response.data.success) {
              const gptResponse = JSON.parse(response.data.result);
              let formattedContent = gptResponse.choices[0].message.content;

              formattedContent = this.convertMarkdownToHTML(formattedContent);

              this.analysisData = formattedContent;
            } else {
              console.error("Code analysis failed");
            }
          })
          .catch((error) => {
            console.error(error);
          })
          .finally(() => {
            this.isLoading = false;  // 로딩 완료
            this.stopLoadingMessage();  // 로딩 메시지 정지
          });
      }
      this.setActiveView('analysis');
    },
    handleShortcut(event) {
      const isMac = navigator.platform.toUpperCase().indexOf("MAC") >= 0;

      const openKey = isMac
        ? event.altKey && event.shiftKey // Mac: Option(Alt) + Shift
        : event.ctrlKey && event.code === "Space"; // Windows/Linux: Ctrl + Space

      if (openKey && this.openFiles[this.activeTab]?.content) {
        this.toggleSpotlightSearch();
      }
    },
    toggleSpotlightSearch() {
      if (this.openFiles[this.activeTab]?.content) {
        this.showSpotlight = !this.showSpotlight;
      }
    },
    handleGptResponse(response) {
      this.analysisData = response;
      this.stopLoadingMessage();
      this.isLoading = false;
    },
    updateFileContent(fileName, newContent) {
      // fileTree에서 파일을 찾아서 content 업데이트
      const updateContentInTree = (nodes) => {
        for (let node of nodes) {
          if (node.type === "FILE" && node.name === fileName) {
            node.content = newContent;
            break;
          } else if (node.type === "FOLDER" && node.children) {
            updateContentInTree(node.children);
          }
        }
      };
      updateContentInTree(this.fileTree);
    },
    updateContent(newValue) {
      if (this.activeTab !== null) {
        this.openFiles[this.activeTab].content = newValue;
        // fileTree의 해당 파일에도 업데이트
        this.updateFileContent(this.openFiles[this.activeTab].name, newValue);
      }
    },
    runCodeBtn() {
      this.runCode();
      this.setActiveView('result');
    },
    setActiveView(view) {
      this.activeView = view;
    },
    convertMarkdownToHTML(markdownText) {
      let htmlText = markdownText;

      // 제목 변환 (#, ##, ###, ####, #####, ######)
      htmlText = htmlText.replace(/^###### (.+)/gm, '<h6>$1</h6>');
      htmlText = htmlText.replace(/^##### (.+)/gm, '<h5>$1</h5>');
      htmlText = htmlText.replace(/^#### (.+)/gm, '<h4>$1</h4>');
      htmlText = htmlText.replace(/^### (.+)/gm, '<h3>$1</h3>');
      htmlText = htmlText.replace(/^## (.+)/gm, '<h2>$1</h2>');
      htmlText = htmlText.replace(/^# (.+)/gm, '<h1>$1</h1>');

      // 굵은 글씨 (Bold)
      htmlText = htmlText.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>');

      // 기울임 글씨 (Italic)
      htmlText = htmlText.replace(/\*(.+?)\*/g, '<em>$1</em>');

      // 코드 블록 (```언어 코드```)
      htmlText = htmlText.replace(/```(\w+)([\s\S]*?)```/g, '<pre><code class="language-$1">$2</code></pre>');

      // 인라인 코드 (`코드`)
      htmlText = htmlText.replace(/`([^`]+)`/g, '<code>$1</code>');

      // 링크 [텍스트](URL)
      htmlText = htmlText.replace(/\[([^\]]+)\]\((https?:\/\/[^)]+)\)/g, '<a href="$2">$1</a>');

      // 이미지 ![대체 텍스트](URL)
      htmlText = htmlText.replace(/!\[([^\]]*)\]\((https?:\/\/[^)]+)\)/g, '<img src="$2" alt="$1">');

      // 순서 있는 목록 (1. 항목)
      htmlText = htmlText.replace(/^\d+\.\s(.+)/gm, '<ol><li>$1</li></ol>');

      // 순서 없는 목록 (- 항목)
      htmlText = htmlText.replace(/^- (.+)/gm, '<ul><li>$1</li></ul>');

      // 인용문 (Blockquote)
      htmlText = htmlText.replace(/^> (.+)/gm, '<blockquote>$1</blockquote>');

      // 수평선 (Horizontal Rule)
      htmlText = htmlText.replace(/^---/gm, '<hr>');

      // 체크리스트 (- [x] 완료된 작업)
      htmlText = htmlText.replace(/- \[x\] (.+)/g, '<ul><li><input type="checkbox" checked disabled> $1</li></ul>');
      htmlText = htmlText.replace(/- \[ \] (.+)/g, '<ul><li><input type="checkbox" disabled> $1</li></ul>');

      // 표 (| 헤더1 | 헤더2 |)
      htmlText = htmlText.replace(/\|(.+?)\|/g, function(_, row) {
        const cells = row.split('|').map(cell => `<td>${cell.trim()}</td>`).join('');
        return `<tr>${cells}</tr>`;
      });

      // 줄바꿈 (\n -> <br>)
      htmlText = htmlText.replace(/\n/g, '<br>');

      return htmlText;
    },
    startLoadingMessage() {
      this.activeView = 'analysis';
      this.isLoading = true;

      let dots = 1;
      this.loadingInterval = setInterval(() => {
        if (dots > 3) dots = 1;
        this.loadingMessage = '분석중' + '.'.repeat(dots); 
        dots++;
      }, 500);
    },
    stopLoadingMessage() {
      if (this.loadingInterval) {
        clearInterval(this.loadingInterval);
        this.loadingInterval = null;
        this.loadingMessage = '분석중.';
      }
    },
    onFileSelected(file) {
      if (file.type === "FOLDER") {
        this.selectedFolder = file;
      }
      this.selectedItem = file;

      const fileExists = this.openFiles.find((openFile) => openFile.name === file.name);
      if (!fileExists && file.type === "FILE") {
        // 파일이 열려 있지 않으면 파일을 추가
        this.openFiles.push({ name: file.name, content: file.content });
        this.activeTab = this.openFiles.findIndex((openFile) => openFile.name === file.name);
      }
       // 파일을 선택한 경우 해당 탭으로 전환
    },
    getFileContent(fileName) {
      const fileContents = {
        "index.html": "<!DOCTYPE html><html><body><h1>Index</h1></body></html>",
      };
      return fileContents[fileName] || "No content available";
    },
    selectTab(index) {
      this.activeTab = index;
    },
    openLabMod() {
      this.mode = '실험실';
    },
    closeTab(index) {
      this.openFiles.splice(index, 1);
      if (this.activeTab === index) {
        this.activeTab = this.openFiles.length ? 0 : null;
      } else if (this.activeTab > index) {
        this.activeTab--;
      }
    },
    openCodeEditOp() {
      this.isCodeEditerOpModal = true;
    },
    openNewFolderModal() {
      this.isNewFolderModalOpen = true;
    },
    setOpenedFromManageModal() {
      this.openedFromManageModal = true;
    },
    setOpenedCode(){
      this.isOpenCode = true;
    },
    ImportProjectModal() {
      this.openedFromManageModal = false;
      this.isCreateProjectOpen = true;
    },
    openLoadModal() {
      this.openedFromManageModal = false;
      this.isImportProject = true;
    },
    closeCreateProjectModal() {
    this.isCreateProjectOpen = false;
    this.openedFromManageModal = false;
    this.isGroupProj = false;
    this.$router.push({ query: { openCreateProject: null } });
    },
    closeImportProjectModal() {
    this.isImportProject = false;
    this.openedFromManageModal = false;
    this.$router.push({ query: { openImportProject: null } });
    },
    openCreateProject() {
      this.isCodeEditerOpModal = false;
      this.isCreateProjectOpen = true;
    },
    openImportProject() {
      this.isImportProject = true;
    },
    closeFolderModal() {
      this.isNewFolderModalOpen = false;
    },
    openCodeModal() {
      this.isCodeEditerOpModal = false;
      this.isCodeModalOpen = true;
    },
    closeCodeModal() {
      this.isCodeModalOpen = false;
    },
    handleFolderSelection(codeId, codeName) {
      this.selectedCodeId = codeId;
      this.projectDto.id = codeId;
      this.getFileSystem(codeId);
      this.mode = '개인 프로젝트';
      this.projectName = codeName;
      this.isCodeModalOpen = false;
      this.isLabProject = false;
    },
    handleSaveSelection(saveData) {
      this.saveProject(saveData.title, saveData.comment)
      this.isSaveModalOpen = false;
    },
    handleGitSelection(version) {
      this.getFileSystemWithVersion(this.projectDto.id, version);
      this.isGitModalOpen = false;
    },
    handleProjSelection(project) {
      this.createProject(project.name, project.language, project.ownerId);
      if(this.isGroupProj) {
        this.mode = '그룹 프로젝트'
      } else {
        this.mode = '개인 프로젝트'
      }
      this.fileTree = [];
      this.openFiles = [];
      this.projectName = project.name;
      this.isCreateProjectOpen = false;
      this.isGroupProj = false;
      this.isLabProject = false;
    },
    openNewFileModal() {
      this.isNewFileModalOpen = true;
    },
    closeFileModal() {
      this.isNewFileModalOpen = false;
    },
    openSaveModal() { 
      this.isSaveModalOpen = true;
    },
    closeSaveModal() { 
      this.isSaveModalOpen = false;
    },
    addNewFolder(folderName) {
      if (!folderName) return;

      if (this.selectedFolder && this.selectedFolder.type === "FOLDER") {
        this.selectedFolder.children.push({
          name: folderName,
          type: "FOLDER",
          children: [],
          content: "",
        });
      } else {
        this.fileTree.push({
          name: folderName,
          type: "FOLDER",
          children: [],
          content: "",
        });
      }
      this.closeFolderModal();
    },
    addNewFile(fileName, fileContent) {
      let content = "";
      if(fileContent != null) {
        content = fileContent;
      }
      if (!fileName) return;

      if (this.selectedFolder && this.selectedFolder.type === "FOLDER") {
        this.selectedFolder.children.push({
          name: fileName,
          type: "FILE",
          children: [],
          content: content,
        });
      } else {
        this.fileTree.push({
          name: fileName,
          type: "FILE",
          children: [],
          content: content,
        });
      }
      this.closeFileModal();
    },
    deselectFolder() {
      this.selectedFolder = null;
    },
    testmodal(){
      this.isCodeEditerOpModal = true;
    },
    runCode() {
      this.codeData = "";
      let importStatements = []; // import 문장을 저장할 배열
      if (this.activeTab !== null) {
        const code = this.openFiles;
        try {
          this.resultData = code;

          code.forEach((element) => {
            const contentLines = element.content.split('\n'); // 각 파일의 내용을 줄 단위로 분리

            contentLines.forEach((line) => {
              // import로 시작하고 ;로 끝나는 문장을 찾아 importStatements에 추가
              if (line.trim().startsWith('import') && line.trim().endsWith(';')) {
                importStatements.push(line.trim());
              } else {
                this.codeData += line + '\n'; // 나머지 내용은 codeData에 추가
              }
            });
          });

          // importStatements를 최상단에 추가
          if (importStatements.length > 0) {
            this.codeData = importStatements.join('\n') + '\n' + this.codeData;
          }

          const createdDateFormatted = moment(new Date()).format("YYYY.MM.DD hh:mm:ss");
          const postData = {
            content: this.codeData,
            createdTime: createdDateFormatted,
            langId: "java",
            userId: this.userId,
          };

          apiClient
            .post(`${process.env.VUE_APP_API_URL}/api/code-editor/compile-code.do`, postData, {
              headers: {
                'Content-Type': 'application/json; charset=UTF-8'
              }
            })
            .then((response) => {
              if (response.data.success) {
                this.resultData = response.data.result;
              } else {
                console.error("get-board-list failed");
              }
            })
            .catch(function (error) {
              console.error(error);
            });
        } catch (error) {
          this.resultData = `Error: ${error.message}`;
        }
      }
      this.setActiveView('result');
    },
    openGitModal() {
      this.isGitModalOpen = true;
    },
    closeGitModal() {
      this.isGitModalOpen = false;
    },
    // 파일이나 폴더 삭제 메소드
    deleteItem() {
    if (this.selectedItem) {
      const deleteRecursively = (nodes, target) => {
        for (let i = 0; i < nodes.length; i++) {
          const node = nodes[i];

          if (node.name === target.name && node.type === target.type) {
            if (node.type === 'FILE') {
              const tabIndex = this.openFiles.findIndex(tab => tab.name === node.name);
              if (tabIndex !== -1) {
                this.closeTab(tabIndex);
              }
            }
            nodes.splice(i, 1);
            return true;
          }

          if (node.type === 'FOLDER' && node.children) {
            const deleted = deleteRecursively(node.children, target);
            if (deleted && node.children.length === 0) {
              return false;
            }
          }
        }
        return false;
      };
      // 삭제 함수 호출
      deleteRecursively(this.fileTree, this.selectedItem);
      // 선택된 항목 초기화
      this.selectedItem = null;
      this.selectedFolder = null;
    }
  },
    getFileSystemWithVersion(codeId, version){
      const projectId = codeId;
      this.openFiles = [];
      this.activeTab = null;
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/file-system/get-file-system?projectId=${projectId}&version=${version}`)
        .then((response) => {
          if (response.data.success) {
            this.fileTree = response.data.result;
          } else {
            console.error("get-board-list failed");
          }
        })
        .catch(function (error) {
          console.error(error);
        });
    },
    getMyProject(){
      const userId = this.userId;
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/file-system/get-individual-project?userId=${userId}`)
        .then((response) => {
          if (!response.data.success) {
            console.error("get-board-list failed");
          }
        })
        .catch(function (error) {
          console.error(error);
        });
    },
    saveProject(title, comment) {
      const postData = {
        fileSystemDtos: this.fileTree,
        projectDto: this.projectDto,
        versionInfoDto: {
          title: title,
          comment: comment
        }
      };
      apiClient
        .post(`${process.env.VUE_APP_API_URL}/api/file-system/file-save`, postData)
        .then((response) => {
          if (!response.data.success) {
            console.error("get-board-list failed");
          }
        })
        .catch(function (error) {
          console.error(error);
        });
    },
    getFileSystem(codeId){
      const projectId = codeId;
      const version = -1;
      this.openFiles = [];
      this.activeTab = null;
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/file-system/get-file-system?projectId=${projectId}&version=${version}`)
        .then((response) => {
          if (response.data.success) {
            this.fileTree = response.data.result;
          } else {
            console.error("get-board-list failed");
          }
        })
        .catch(function (error) {
          console.error(error);
        });
    },
    createProject(title, type, ownerId) {
        const projectDto = {
          name: title,
          ownerId: ownerId, // 숫자로 변환
          type: type,
          groupId: this.groupId,
        };
        apiClient
          .post(`${process.env.VUE_APP_API_URL}/api/file-system/create-project`, projectDto)
          .then((response) => {
            if (response.data.success) {
              this.projectDto = response.data.result;
            } else {
              console.error("Failed to create project");
            }
          })
          .catch((error) => {
            console.error("Error creating project:", error);
          });
    },
    findParentFolder(nodes, target) {
      for (const node of nodes) {
        if (node.type === 'folder') {
          if (node.children.some(child => child.name === target.name)) {
            return node;
          }
          const found = this.findParentFolder(node.children, target);
          if (found) {
            return found;
          }
        }
      }
      return null;
    }
  },
};
</script>

<style scoped>
@import '~@/assets/css/code-editor.css'
</style>