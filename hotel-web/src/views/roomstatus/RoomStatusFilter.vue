<template>
  <div class="room-status-filter">
    <el-radio-group v-model="selectedStatus" class="radio-group">
      <el-radio :value="'all'">全部</el-radio>
      <el-radio
        v-for="item in roomStatusOption"
        :key="item.status"
        :value="item.status"
      >
        {{ item.status_name }}
      </el-radio>
    </el-radio-group>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import type { RoomStatus } from '@/types'
import * as roomApi from '@/api/room/index'

const props = defineProps<{ modelValue: RoomStatus }>()
const emit = defineEmits(['update:modelValue'])

const roomStatusOption = ref<any[]>([])
const selectedStatus = ref('all')

onMounted(() => {
  roomApi.getRoomStatus().then((res) => {
    roomStatusOption.value = res.data
  })
})

watch(selectedStatus, (newVal) => {
  const status: RoomStatus = {
    idle:
      newVal === 'all' ||
      newVal === 'empty_clean' ||
      newVal === 'free' ||
      newVal === 'available',
    dirty: newVal === 'all' || newVal === 'empty_dirty' || newVal === 'dirty',
    repair: newVal === 'all' || newVal === 'maintenance',
    booked: newVal === 'all' || newVal === 'booked' || newVal === 'reserved',
    checkedIn:
      newVal === 'all' ||
      newVal === 'occupied_clean' ||
      newVal === 'occupied_dirty' ||
      newVal === 'occupied' ||
      newVal === 'checked_in',
    locked: newVal === 'all' || newVal === 'locked',
    selfUse: newVal === 'all' || newVal === 'self_use',
    todayCheckout: newVal === 'all',
  }
  emit('update:modelValue', status)
})
</script>

<style scoped lang="scss">
.room-status-filter {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
}

.radio-group {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
}
</style>
