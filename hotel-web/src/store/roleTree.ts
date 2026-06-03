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
    // appendNode(item: any) {
    //   if (item && !this.selectedNodes.find((node: any) => node.id === item.id)) {
    //     this.selectedNodes.push(item)
    //   }
    // },
    // removeNode(item: any) {
    //   const index = this.selectedNodes.findIndex((node: any) => node.id === item.id)
    //   if (index > -1) {
    //     this.selectedNodes.splice(index, 1)
    //   }
    // },
    // clearSelectedNodes() {
    //   this.selectedNodes = []
    // },
    // setSelectedNodes(nodes: any[]) {
    //   this.selectedNodes = nodes
    // },
  },
})
