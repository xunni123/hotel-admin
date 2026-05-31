import { defineStore } from 'pinia'

export const roomstatusStore = defineStore('roomstatusStore', {
  state: () => ({
    roomData: [],
    originalRoomData: [],
    isSearched: false,
  }),
  getters: {
    getRoomData(state) {
      return state.roomData
    },
    getOriginalRoomData(state) {
      return state.originalRoomData
    },
    getIsSearched(state) {
      return state.isSearched
    },
  },
  actions: {
    setRoomData(data: any) {
      this.roomData = data
    },
    setOriginalRoomData(data: any) {
      this.originalRoomData = data
    },
    setIsSearched(value: boolean) {
      this.isSearched = value
    },
  },
  persist: true,
})
