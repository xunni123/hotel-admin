import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useCheckinStore = defineStore('checkin', {
  state: () => ({
    searchText: '',
    isSearched: false,
    isDetailVisible: false,
    selectedOrder: null as any,
    checkinData: [] as any[],
  }),
  persist: true,
  getters: {
    getSearchText: (state) => state.searchText,
    getIsSearched: (state) => state.isSearched,
    getIsDetailVisible: (state) => state.isDetailVisible,
    getSelectedOrder: (state) => state.selectedOrder,
    getCheckinData: (state) => state.checkinData,
  },
  actions: {
    setSearchText(text: string) {
      this.searchText = text
    },
    setIsSearched(searched: boolean) {
      this.isSearched = searched
    },
    setIsDetailVisible(visible: boolean) {
      this.isDetailVisible = visible
    },
    setSelectedOrder(order: any) {
      this.selectedOrder = order
    },
    setCheckinData(data: any[]) {
      this.checkinData = data
    },
    clearSearch() {
      this.searchText = ''
      this.isSearched = false
    },
    closeDetail() {
      this.isDetailVisible = false
      this.selectedOrder = null
    },
  },
})
