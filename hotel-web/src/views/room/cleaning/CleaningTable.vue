<template>
  <div class="menu-table-wrapper">
    <!-- 标题栏 -->
    <div class="table-header">
      <div class="header-left">
        <h3>待打扫房间</h3>
        <span class="total-count">共 {{ displayRooms.length }} 间</span>
        <span v-if="selectedRooms.length > 0" class="selected-count"
          >已选 {{ selectedRooms.length }} 间</span
        >
      </div>
      <div class="header-right">
        <el-button
          v-if="selectedRooms.length > 0"
          size="small"
          @click="clearSelection"
        >
          取消选择
        </el-button>
      </div>
    </div>

    <!-- 房间区 -->
    <div class="room-container">
      <div class="room-grid">
        <div
          v-for="room in displayRooms"
          :key="room.roomId"
          class="room-card"
          :class="{
            selected: isSelected(room.roomId),
            assigned: isAssigned(room.roomId),
          }"
          @click="handleCardClick(room)"
        >
          <div class="card-header">
            <span class="room-number">{{ room.roomNumber }}</span>
            <el-icon v-if="isSelected(room.roomId)" class="check-icon">
              <Select />
            </el-icon>
          </div>
          <div class="card-body">
            <span class="room-type">{{ room.roomType }}</span>
          </div>
          <div class="card-footer">
            <span
              v-if="room.status?.toLowerCase() !== 'pending_cleaning'"
              class="status-text"
              :class="getStatusClass(room)"
              >{{ getStatusText(room) }}</span
            >
            <span
              v-else
              class="status-text assigned"
              @click.stop="revertRoomStatus(room.roomId)"
            >
              <el-icon class="revert-icon"><RefreshRight /></el-icon>
              已派单
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-area">
      <Pagination
        :total="total"
        v-model:page="currentPage"
        v-model:limit="pageSize"
        :page-sizes="[12, 24, 36, 48]"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { RefreshRight, Select } from '@element-plus/icons-vue'
import Pagination from '@/components/Pagination.vue'
import type { Rooms } from '@/types'
import { getAllMenu } from '@/api/room/index'
import { revertRoomStatus as revertRoomStatusApi } from '@/api/cleaning'

const emit = defineEmits<{
  (e: 'update:selectedRooms', rooms: number[]): void
  (e: 'refreshCleaners'): void
}>()

const rooms = ref<Rooms[]>([])
const selectedRooms = ref<number[]>([])
const assignedRooms = ref<number[]>([])

// 页
const currentPage = ref(1)
const pageSize = ref(24)
const total = computed(() => displayRooms.value.length)

// 获取显示的房间（待打扫和已派单）
const displayRooms = computed(() => {
  return rooms.value.filter((room) => {
    const status = room.status?.toLowerCase()
    return (
      status === 'dirty' ||
      status === 'empty_dirty' ||
      status === 'pending_cleaning'
    )
  })
})

// 待打扫的房间
const dirtyRooms = computed(() => {
  return rooms.value.filter((room) => {
    const status = room.status?.toLowerCase()
    return status === 'dirty' || status === 'empty_dirty'
  })
})

const isAssigned = (roomId: number) => {
  return assignedRooms.value.includes(roomId)
}

const isSelected = (roomId: number) => {
  return selectedRooms.value.includes(roomId)
}

const getStatusText = (room: Rooms) => {
  const status = room.status?.toLowerCase()
  if (status === 'pending_cleaning') {
    return '已派单'
  }
  return '待打扫'
}

const getStatusClass = (room: Rooms) => {
  const status = room.status?.toLowerCase()
  if (status === 'pending_cleaning') {
    return 'assigned'
  }
  return 'dirty'
}

const handleCardClick = (room: Rooms) => {
  if (room.status?.toLowerCase() === 'pending_cleaning') {
    return
  }
  toggleSelect(room.roomId)
}

const toggleSelect = (roomId: number) => {
  const index = selectedRooms.value.indexOf(roomId)
  if (index > -1) {
    selectedRooms.value.splice(index, 1)
  } else {
    selectedRooms.value.push(roomId)
  }
  emit('update:selectedRooms', selectedRooms.value)
}

const revertRoomStatus = async (roomId: number) => {
  try {
    await revertRoomStatusApi(roomId)
    await refreshRooms()
    emit('refreshCleaners')
  } catch (error) {
    console.error('恢复房间状态失败:', error)
  }
}

const clearSelection = () => {
  selectedRooms.value = []
  emit('update:selectedRooms', selectedRooms.value)
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
}

const refreshRooms = () => {
  return getAllMenu().then((res) => {
    rooms.value = res.data
  })
}

onMounted(() => {
  getAllMenu().then((res) => {
    rooms.value = res.data
  })
})

defineExpose({
  refreshRooms,
})
</script>

<style scoped lang="scss">
.menu-table-wrapper {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 标题栏 */
.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #fff;
  border-radius: 8px;
  border: 1px solid #e9ecef;

  .header-left {
    display: flex;
    align-items: center;
    gap: 12px;

    h3 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }

    .total-count {
      font-size: 13px;
      color: #6c757d;
      padding: 2px 8px;
      background: #f8f9fa;
      border-radius: 4px;
    }

    .selected-count {
      font-size: 13px;
      color: #38b2ac;
      padding: 2px 8px;
      background: #e6fffa;
      border-radius: 4px;
    }
  }
}

/* 房间容器 */
.room-container {
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
  padding: 20px;
}

.room-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 12px;
}

/* 房间卡片 */
.room-card {
  border-radius: 8px;
  padding: 14px;
  cursor: pointer;
  transition: all 0.25s ease;
  background: #fc8181;
  border: 2px solid transparent;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  }

  &.selected {
    border-color: var(--tabs);
    box-shadow: 0 0 0 3px rgba(56, 178, 172, 0.2);
  }

  &.assigned {
    background: linear-gradient(135deg, #90cdf4 0%, #68d391 100%);
    border: 2px dashed #3182ce;
    cursor: not-allowed;
    opacity: 0.9;

    &:hover {
      transform: none;
      box-shadow: none;
    }

    .room-number,
    .room-type {
      color: rgba(255, 255, 255, 0.85);
    }
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;

  .room-number {
    font-size: 16px;
    font-weight: 600;
    color: #ffffff;
  }

  .check-icon {
    font-size: 20px;
    color: #fff;
  }
}

.card-body {
  margin-bottom: 10px;

  .room-type {
    font-size: 12px;
    color: rgba(255, 255, 255, 0.9);
    line-height: 1.4;
  }
}

.card-footer {
  .status-text {
    font-size: 11px;
    padding: 2px 8px;
    border-radius: 4px;
    font-weight: 500;
    color: #ffffff;
    background: rgba(255, 255, 255, 0.25);
    display: inline-flex;
    align-items: center;
    gap: 4px;

    &.assigned {
      background: rgba(49, 130, 206, 0.6);
      border: 1px solid rgba(255, 255, 255, 0.3);
      color: #fff;
      cursor: pointer;
      transition: all 0.2s ease;

      &:hover {
        background: rgba(49, 130, 206, 0.8);
        transform: scale(1.05);
      }

      .revert-icon {
        font-size: 12px;
      }
    }
  }
}

/* 分页区 */
.pagination-area {
  padding-top: 8px;
}
</style>
