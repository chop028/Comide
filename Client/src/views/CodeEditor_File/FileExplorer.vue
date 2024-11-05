<template>
  <ul>
    <li v-for="(node, index) in nodes" :key="index">
      <div @click="toggleNode(node)" :class="{ 'active-node': isSelected(node) }">
        <span v-if="node.type === 'FOLDER'">
          {{ isOpen(node) ? '📂' : '📁' }} {{ node.name }}
        </span>
        <span v-else>
          📄 {{ node.name }}
        </span>
      </div>
      <ul v-if="isOpen(node)">
        <FileExplorer :nodes="node.children" @file-selected="$emit('file-selected', $event)" />
      </ul>
    </li>
  </ul>
</template>

<script>
export default {
  props: {
    nodes: Array
  },
  data() {
    return {
      openFolders: [],
      selectedNode: null
    };
  },
  methods: {
    toggleNode(node) {
      if (node.type === 'FOLDER') {
        const index = this.openFolders.indexOf(node);
        if (index !== -1) {
          this.openFolders.splice(index, 1);
        } else {
          this.openFolders.push(node);
        }
      }
      this.selectedNode = node;
      this.$emit('file-selected', node);
    },
    isOpen(node) {
      return this.openFolders.includes(node);
    },
    isSelected(node) {
      return this.selectedNode === node;
    }
  }
};
</script>

<style scoped>
li {
  cursor: pointer;
  list-style: none;
}

div {
  padding: 5px;
  transition: background-color 0.3s, font-weight 0.3s;
}

div:hover {
  font-weight: bold;
  background-color: rgba(248, 248, 248, 0.3);
}

.active-node {
  font-weight: bold;
  background-color: rgba(237, 237, 237, 0.5);
  border-radius: 5px;
  margin-top: 3px;
}

ul {
  padding: 0 0 0 15px;
}
</style>