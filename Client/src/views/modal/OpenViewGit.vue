<template>
    <div class="modal-overlay">
      <div class="git-modal-content">
        <div class="git-modal-header">
          <p class="git-title">버전 관리</p>
          <button class="close-btn" @click="closeModal">X</button>
        </div>
        <table class="git-table">
          <thead>
            <tr>
              <th class="git-table-header">Version</th>
              <th class="git-table-header">Title</th>
              <th class="git-table-header">Comment</th>
              <th class="git-table-header">Time</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(version, index) in versions" :key="index" @click="selectVersion(version.version)" class="clickable-row">
              <td class="git-table-cell">{{ version.version }}</td>
              <td class="git-table-cell">{{ version.title }}</td>
              <td class="git-table-cell">{{ version.comment }}</td>
              <td class="git-table-cell">{{ formatDate(version.createdDate) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </template>
  
  <script>
  import apiClient from "@/axios/axios";

  export default {
    name: "GitModal",
    data() {
      return {
        versions: [],
      };
    },
    props: {
        projId: {
            type: String,
            required: true,
        },
    },
    methods: {
      closeModal() {
        this.$emit("close-modal");
      },
      selectVersion(version) {
        this.$emit('closeWithSelection', version);
        this.closeModal();
      },
      formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        const hours = String(date.getHours()).padStart(2, "0");
        const minutes = String(date.getMinutes()).padStart(2, "0");
        const seconds = String(date.getSeconds()).padStart(2, "0");
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
    getProjectVersions(){
      apiClient
        .get(`${process.env.VUE_APP_API_URL}/api/file-system/get-project-version?projectId=${this.projId}`)
        .then((response) => {
          if (response.data.success) {
            this.versions = response.data.result;
          } else {
            console.error("get-board-list failed");
          }
        })
        .catch(function (error) {
          console.error(error);
        });
        },
    },
    mounted() {
        this.getProjectVersions();
    },
  };
  </script>
  
  <style scoped>
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 999;
    animation: fadeIn 0.4s ease;
  }
  
  .git-modal-content {
    background-color: #fff;
    padding: 30px;
    border-radius: 10px;
    width: 90%;
    max-width: 800px;
    height: auto;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    text-align: center;
    position: relative;
    animation: slideUp 0.4s ease;
  }
  
  .git-modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .git-title {
    font-size: 26px;
    font-weight: 700;
    color: #333;
  }
  
  .close-btn {
    background-color: transparent;
    border: none;
    font-size: 20px;
    font-weight: bold;
    color: #888;
    cursor: pointer;
    transition: color 0.3s ease;
  }
  
  .close-btn:hover {
    color: #333;
  }
  
  .git-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 15px;
  }
  
  .git-table th,
  .git-table td {
    padding: 12px 15px;
    text-align: center;
    border: 1px solid #e0e0e0;
  }
  
  .git-table-header {
    background-color: #FAE9E9;
    font-weight: 600;
    color: #333;
  }
  
  .git-table-cell {
    background-color: #fff;
    font-size: 16px;
    color: #555;
    transition: background-color 0.2s ease, text-decoration 0.2s ease;
  }
  
  .clickable-row:hover .git-table-cell {
    background-color: #fef6f6 !important;
    text-decoration: underline;
    cursor: pointer;
    transform: scale(1.02);
    transition: transform 0.2s ease;
  }
  
  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
  
  @keyframes slideUp {
    from {
      transform: translateY(50px);
      opacity: 0;
    }
    to {
      transform: translateY(0);
      opacity: 1;
    }
  }
  
  @media (max-width: 768px) {
    .git-modal-content {
      width: 100%;
      padding: 20px;
    }
  
    .git-title {
      font-size: 22px;
    }
  
    .git-table th,
    .git-table td {
      padding: 10px;
      font-size: 14px;
    }
  }
  </style>