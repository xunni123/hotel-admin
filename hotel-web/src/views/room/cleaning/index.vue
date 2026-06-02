<template>
  <div class="room-status">
    <div class="room-status__menu">
      <CleaningMenu ref="menuRef" @assign="handleAssign" />
    </div>
    <div class="room-status__table">
      <CleaningTable
        ref="tableRef"
        @update:selectedRooms="handleUpdateSelectedRooms"
        @refreshCleaners="handleRefreshCleaners"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onUnmounted } from 'vue'
import CleaningMenu from './CleaningMenu.vue'
import CleaningTable from './CleaningTable.vue'
import { MessagePrompt } from '@/utils/message'

const menuRef = ref<any>(null)
const tableRef = ref<any>(null)

//更新房间
const handleUpdateSelectedRooms = (rooms: number[]) => {
  menuRef.value?.setSelectedRooms(rooms)
}

//刷新清洁员工
const handleRefreshCleaners = () => {
  menuRef.value?.loadCleaners()
}

let assignTimer: ReturnType<typeof setTimeout> | null = null

// 派单tool
const handleAssign = (cleaner: any, roomIds: number[]) => {
  tableRef.value?.refreshRooms()
  menuRef.value?.loadCleaners()

  if (assignTimer) {
    clearTimeout(assignTimer)
  }
  assignTimer = setTimeout(() => {
    MessagePrompt(
      `已派单给 ${cleaner.cleanerName}，共 ${roomIds.length} 间房间`,
      'success',
    )
  }, 15000)
}

onUnmounted(() => {
  if (assignTimer) {
    clearTimeout(assignTimer)
    assignTimer = null
  }
})
</script>

<style lang="scss" scoped>
.room-status {
  display: flex;
  gap: 20px;
  height: 100%;

  .room-status__menu {
    width: 20vw;
    padding: 10px;
    background-color: #ffffff;
    border-radius: 5px;
    box-shadow: 0px 0px 20px 1px rgba(0, 0, 0, 0.1);
  }

  .room-status__table {
    width: 70vw;
    padding: 10px;
    background-color: #ffffff;
    border-radius: 5px;
    box-shadow: 0px 0px 20px 1px rgba(0, 0, 0, 0.1);
  }
}
</style>
