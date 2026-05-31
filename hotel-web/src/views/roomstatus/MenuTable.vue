<template>
  <div class="menu-table-wrapper">
    <!-- 标题栏 -->
    <div class="table-header">
      <div class="header-left">
        <h3>房间状态</h3>
        <span class="total-count">共 {{ total }} 间</span>
      </div>
      <div class="header-right">
        <div class="status-legend">
          <span class="legend-item">
            <span class="legend-dot available"></span>
            <span>可入住</span>
          </span>
          <span class="legend-item">
            <span class="legend-dot occupied"></span>
            <span>入住中</span>
          </span>
          <span class="legend-item">
            <span class="legend-dot dirty"></span>
            <span>待打扫</span>
          </span>
          <span class="legend-item">
            <span class="legend-dot booked"></span>
            <span>已预订</span>
          </span>
          <span class="legend-item">
            <span class="legend-dot maintenance"></span>
            <span>维修中</span>
          </span>
          <span class="legend-item">
            <span class="legend-dot locked"></span>
            <span>已锁定</span>
          </span>
          <span class="legend-item">
            <span class="legend-dot self-use"></span>
            <span>自用</span>
          </span>
        </div>
      </div>
    </div>

    <!-- 房间区 -->
    <div class="room-container">
      <div class="room-grid">
        <div
          v-for="room in currentPageRooms"
          :key="room.roomId"
          class="room-card"
          :class="room.status"
          @click="openDrawer(room)"
        >
          <div class="card-header">
            <span class="room-number">{{ room.roomNumber }}</span>
            <span class="status-indicator" :class="room.status"></span>
          </div>
          <div class="card-body">
            <span class="room-type">{{ room.roomType }}</span>
          </div>
          <div class="card-footer">
            <span class="status-text" :class="room.status">
              {{ getStatusText(room.status) }}
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

    <!-- 房间详情-->
    <MyDrawer
      v-model="drawerVisible"
      :title="`房间 ${selectedRoom?.roomNumber}`"
      :size="450"
      close-on-click-modal
    >
      <div v-if="selectedRoom" class="room-detail">
        <div class="detail-row main">
          <div class="room-info">
            <span class="room-num">{{ selectedRoom.roomNumber }}</span>
            <span class="room-category">{{ selectedRoom.roomType }}</span>
          </div>
          <span class="status-badge" :class="selectedRoom.status">
            {{ getStatusText(selectedRoom.status) }}
          </span>
        </div>

        <div class="detail-section">
          <h4 class="section-title">房间配置</h4>
          <div class="config-grid">
            <div class="config-item">
              <span>电视</span>
            </div>
            <div class="config-item">
              <span>WiFi</span>
            </div>
            <div class="config-item">
              <span>空调</span>
            </div>
            <div class="config-item">
              <span>独立卫浴</span>
            </div>
          </div>
        </div>

        <div class="detail-section info-section">
          <h4 class="section-title">房间具体信息</h4>
          <div class="info-grid">
            <div class="info-item">
              <span class="info-label">房间类型：</span>
              <span class="info-value">{{ selectedRoom.roomType }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">房间价格：</span>
              <span class="info-value">{{ selectedRoom.price }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">容量人数：</span>
              <span class="info-value">{{
                selectedRoom.currentGuests + '人'
              }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">入住时间：</span>
              <span
                :class="
                  selectedRoom.checkInTime ? 'info-value' : 'info-warning'
                "
                >{{ selectedRoom.checkInTime }}</span
              >
            </div>

            <div class="info-item">
              <span class="info-label">所在楼层：</span>
              <span class="info-value">{{ selectedRoom.floor }}</span>
            </div>
          </div>
        </div>
      </div>
    </MyDrawer>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed, onMounted } from 'vue'

import Pagination from '@/components/Pagination.vue'
import MyDrawer from '@/components/MyDrawer.vue'
import type { Rooms } from '@/types'
import { getAllMenu } from '@/api/room/index'
import { roomstatusStore } from '@/store/roomstatus'

const useRoomStore = roomstatusStore()
const rooms = ref<Rooms[]>([])

// 页
const currentPage = ref(1)
const pageSize = ref(24)
const total = computed(() => rooms.value.length)
const drawerVisible = ref(false)
const selectedRoom = ref<Rooms | null>(null)

// get data
const deduplicateRooms = (data: any[]) => {
  const seen = new Set()
  return data.filter((room) => {
    const key = room.roomId
    if (seen.has(key)) return false
    seen.add(key)
    return true
  })
}

onMounted(() => {
  // 如果已经搜索过，保持搜索结果不变；否则加载全部数据
  if (!useRoomStore.isSearched) {
    getAllMenu().then((res) => {
      const uniqueData = deduplicateRooms(res.data)
      useRoomStore.setOriginalRoomData(uniqueData)
      useRoomStore.setRoomData(uniqueData)
      rooms.value = uniqueData
    })
  } else {
    // 已经搜索过，直接使用 store 中的数据
    rooms.value = deduplicateRooms(useRoomStore.roomData)
  }
})

const currentPageRooms = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return rooms.value.slice(start, end)
})

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    clean: '可入住',
    idle: '可入住',
    empty_clean: '可入住',
    free: '可入住',
    occupied_clean: '入住中',
    occupied_dirty: '入住中',
    empty_dirty: '待打扫',
    maintenance: '维修中',
    repair: '维修中',
    locked: '已锁定',
    checked_in: '入住中',
    reserved: '已预订',
    self_use: '自用',
  }
  return statusMap[status] || status
}

const openDrawer = (room: Rooms) => {
  selectedRoom.value = room
  drawerVisible.value = true
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
}

// 监听 roomData 的变化，显示搜索结果，但 originalRoomData 保持不变
watch(
  () => useRoomStore.roomData,
  (newVal) => {
    rooms.value = deduplicateRooms(newVal)
  },
)
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
      color: #343a40;
    }

    .total-count {
      font-size: 13px;
      color: #6c757d;
      padding: 2px 8px;
      background: #f8f9fa;
      border-radius: 4px;
    }
  }

  .header-right {
    .status-legend {
      display: flex;
      gap: 16px;

      .legend-item {
        display: flex;
        align-items: center;
        gap: 6px;
        font-size: 12px;
        color: #6c757d;

        .legend-dot {
          width: 8px;
          height: 8px;
          border-radius: 50%;

          &.available {
            background: #48bb78;
          }
          &.occupied {
            background: #ed8936;
          }
          &.dirty {
            background: #fc8181;
          }
          &.booked {
            background: #9f7aea;
          }
          &.maintenance {
            background: #6c757d;
          }
          &.locked {
            background: #2d3748;
          }
          &.self-use {
            background: #805ad5;
          }
        }
      }
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

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  }

  &.available,
  &.clean,
  &.idle,
  &.empty_clean,
  &.free {
    background: #48bb78;
  }

  &.occupied,
  &.checked_in,
  &.occupied_clean,
  &.occupied_dirty {
    background: #ed8936;
  }

  &.dirty,
  &.empty_dirty {
    background: #fc8181;
  }

  &.booked,
  &.reserved {
    background: #9f7aea;
  }

  &.maintenance,
  &.repair {
    background: #6c757d;
  }

  &.locked {
    background: #2d3748;
  }

  &.self_use {
    background: #805ad5;
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

  .status-indicator {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.8);
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
  }
}

/* 分页区 */
.pagination-area {
  padding-top: 8px;
}

/* 抽屉内容 */
.room-detail {
  padding: 16px;

  .detail-row {
    &.main {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      padding: 20px;
      background: linear-gradient(135deg, #f8f9fa 0%, #fff 100%);
      border-radius: 12px;
      border: 1px solid #e9ecef;
      margin-bottom: 16px;

      .room-info {
        display: flex;
        flex-direction: column;
        gap: 6px;

        .room-num {
          font-size: 24px;
          font-weight: 700;
          color: #2d3748;
        }

        .room-category {
          font-size: 14px;
          color: #718096;
        }
      }

      .status-badge {
        font-size: 12px;
        padding: 6px 14px;
        border-radius: 20px;
        font-weight: 500;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

        &.available,
        &.clean,
        &.idle,
        &.empty_clean,
        &.free {
          background: linear-gradient(135deg, #c6f6d5 0%, #9ae6b4 100%);
          color: #22543d;
        }
        &.occupied,
        &.checked_in,
        &.occupied_clean,
        &.occupied_dirty {
          background: linear-gradient(135deg, #feebc8 0%, #fbd38d 100%);
          color: #744210;
        }
        &.dirty,
        &.empty_dirty {
          background: linear-gradient(135deg, #fed7d7 0%, #fc8181 100%);
          color: #742a2a;
        }
        &.booked,
        &.reserved {
          background: linear-gradient(135deg, #e6fffa 0%, #b2f5ea 100%);
          color: #234e52;
        }
        &.maintenance,
        &.repair {
          background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e0 100%);
          color: #4a5568;
        }
        &.locked {
          background: linear-gradient(135deg, #4a5568 0%, #2d3748 100%);
          color: #e2e8f0;
        }
      }
    }
  }

  .detail-section {
    margin-bottom: 24px;

    &:last-child {
      margin-bottom: 0;
    }

    .section-title {
      font-size: 14px;
      font-weight: 600;
      color: #2d3748;
      margin-bottom: 14px;
      display: flex;
      align-items: center;
      gap: 6px;
    }
  }

  .config-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
  }

  .config-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 14px 16px;
    background: #fff;
    border: 1px solid #e2e8f0;
    border-radius: 10px;
    transition: all 0.2s ease;

    &:hover {
      border-color: #cbd5e0;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
    }

    .icon {
      font-size: 18px;
      color: #4299e1;
    }

    span {
      font-size: 14px;
      color: #4a5568;
      font-weight: 500;
    }
  }

  .info-section {
    padding-top: 8px;
  }

  .info-grid {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }

  .info-item {
    display: flex;
    justify-content: space-between;
    padding: 8px 0;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .info-label {
      font-size: 14px;
      color: #6c757d;
      font-weight: 500;
    }

    .info-value,
    .info-warning {
      font-size: 14px;
      font-weight: 500;
    }
    .info-value {
      color: #343a40;
    }
    .info-warning {
      color: var(--tabs);
    }
  }
}
</style>
