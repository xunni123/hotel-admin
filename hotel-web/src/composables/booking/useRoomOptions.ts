import { getFreeRoom } from '@/api/room'
import { computed, ref } from 'vue'

export const useRoomOptions = () => {
  const roomData = ref([])

  // roomId
  const roomIdOptions = computed(() => {
    if (!roomData.value) return []
    return roomData.value.map((item) => ({
      value: item.roomId,
      label: item.roomId,
    }))
  })

  // roomNm
  const roomNumberOptions = computed(() => {
    if (!roomData.value) return []
    return roomData.value.map((item) => ({
      value: item.roomNumber,
      label: item.roomNumber,
    }))
  })

  //get 列表
  const fetchRooms = async () => {
    try {
      const res = await getFreeRoom()
      roomData.value = res.data || []
    } catch (err) {
      console.error('获取房间失败!', err)
      roomData.value = []
    }
  }

  return {
    roomData,
    roomIdOptions,
    roomNumberOptions,
    fetchRooms,
  }
}
