import { defineStore } from 'pinia'

// 定义store
export const roleTreeStore = defineStore('roleTreeStore', {
  state: () => ({
    defaultExpandAll: false,
    currentTreeData: [] as any,
    selectedNodes: [] as any,
  }),
  getters: {
    getCollapse(state) {
      return state.defaultExpandAll
    },
    getSelectedNodes(state) {
      return state.selectedNodes
    },
  },
  actions: {
    toggleExpandAl() {
      this.defaultExpandAll = !this.defaultExpandAll
    },

  },
})
