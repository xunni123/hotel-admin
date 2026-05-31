import { defineStore } from 'pinia'

// 定义store
export const roleTreeStore = defineStore('roleTreeStore', {
  state: () => ({
    defaultExpandAll: false,
    currentTreeData: [] as any,
  }),
  getters: {
    getCollapse(state) {
      return state.defaultExpandAll
    },
  },
  actions: {
    toggleExpandAl() {
      this.defaultExpandAll = !this.defaultExpandAll
    },
  },
})
