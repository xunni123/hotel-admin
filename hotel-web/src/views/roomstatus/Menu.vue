<template>
  <div class="menu-struct">
    <div class="search-section">
      <div class="search-box">
        <el-icon class="search-icon"><Search /></el-icon>
        <el-input
          v-model="filters.searchText"
          placeholder="房号/订单号/手机号"
          class="search-input"
        />
      </div>
      <el-button class="action-btn" @click="handleSearch">搜索</el-button>
    </div>

    <!-- 位置筛选  -->
    <div class="search-wrapper">
      <div class="wrapper_title">
        <div class="menu-title">位置筛选</div>
      </div>
      <div class="wrapper_content">
        <LocationFilter v-model="filters.location" />
      </div>
    </div>
    <!-- 房态状态 -->
    <div class="search-wrapper">
      <div class="wrapper_title">
        <div class="menu-title">房间状态</div>
      </div>
      <div class="wrapper_content">
        <RoomStatusFilter v-model="filters.roomStatus" />
      </div>
    </div>

    <!-- 订单筛选 -->
    <div class="search-wrapper">
      <div class="wrapper_title">
        <div class="menu-title">订单筛选</div>
      </div>
      <div class="wrapper_content">
        <OrderFilter v-model="filters.order" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import LocationFilter from './LocationFilter.vue'
import RoomStatusFilter from './RoomStatusFilter.vue'
import OrderFilter from './OrderFilter.vue'
import MenuTable from './MenuTable.vue'
import { ref, reactive } from 'vue'
import type { FiltersParam } from '@/types/roomstatus.ts'
import { getMenuContent } from '@/api/room/index'
import { roomstatusStore } from '@/store/roomstatus'
import { MessagePrompt } from '@/utils/message.ts'

const useRoomStore = roomstatusStore()

// 搜索关键词
const filters: FiltersParam = reactive({
  searchText: '',
  location: {
    building: '',
    floor: '',
    roomType: '',
    features: [],
  },
  roomStatus: {
    idle: true,
    dirty: true,
    repair: true,
    booked: true,
    checkedIn: true,
    locked: false,
    selfUse: false,
    todayCheckout: false,
  },
  order: {
    checkinType: '',
    channel: '',
    specialTags: [],
  },
})

// 搜索tool
const handleSearch = () => {
  getMenuContent(filters)
    .then((res) => {
      useRoomStore.setRoomData(res.data)
      useRoomStore.setIsSearched(true)
    })
    .catch((err) => {
      MessagePrompt('查询失败', 'error')
    })
}
</script>

<style scoped lang="scss">
.menu-struct {
  width: 100%;
  height: 100%;
  display: flex;
  gap: 20px;
  flex-direction: column;
  overflow-y: auto;
  padding-right: 8px;

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background: #dcdfe6;
    border-radius: 3px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
  }

  .form-container {
    padding: 16px;
    background: #fff;
    border-radius: 8px;
  }

  .search-section {
    display: flex;
    align-items: center;
    gap: 16px;
    flex-shrink: 0;

    .search-box {
      flex: 1;
      position: relative;
      max-width: 380px;
      background: #fafafa;
      border-radius: 8px;
      border: 1px solid #e4e7ed;
      transition: all 0.2s ease;

      &:focus-within {
        border-color: var(--tabs);
        background: #fff;
        box-shadow: 0 0 0 2px rgba(56, 88, 191, 0.1);
      }
    }

    .search-icon {
      position: absolute;
      left: 14px;
      top: 50%;
      transform: translateY(-50%);
      color: #909399;
      font-size: 16px;
      pointer-events: none;
      transition: color 0.2s ease;
    }

    .search-box:focus-within .search-icon {
      color: var(--tabs);
    }

    .search-input {
      width: 100%;
      padding: 9px 14px 9px 44px;
      background: transparent;
      border-radius: 8px;
      font-size: 14px;
      outline: none;

      :deep(.el-input__wrapper) {
        border: none !important;
        background: transparent !important;
        box-shadow: none !important;
      }

      :deep(.el-input__wrapper.is-focus) {
        border: none !important;
        background: transparent !important;
        box-shadow: none !important;
      }

      :deep(.el-input__inner) {
        border: none !important;
        background: transparent !important;
      }
    }

    .action-btn {
      padding: 9px 24px;
      border-radius: 8px;
      font-size: 14px;
      font-weight: 500;

      :deep(.el-button--primary) {
        background: linear-gradient(135deg, var(--tabs) 0%, var(--card) 100%);
        border-color: var(--tabs);
        &:hover {
          background: var(--card);
          border-color: var(--card);
          transform: translateY(-1px);
        }
      }
    }
  }

  .filter-section {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 16px;
    padding-top: 12px;
    border-top: 1px solid #f0f0f0;
    flex-shrink: 0;

    .filter-label {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
    }

    .filter-value {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 14px;
      color: #606266;

      .text {
        color: #909399;
      }

      .badge {
        min-width: 22px;
        height: 22px;
        padding: 0 6px;
        font-size: 12px;
        font-weight: 500;
        color: #fff;
        background: var(--success);
        border-radius: 11px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
      }
    }

    .reset-btn {
      padding: 6px 16px;
      font-size: 13px;
      color: #606266;
      background: var(--bg-page);
      border: 1px solid #e4e7ed;
      border-radius: 4px;
      transition: all 0.2s ease;

      &:hover {
        background: rgba(30, 58, 138, 0.05);
        border-color: var(--primary);
        color: var(--primary);
      }
    }
  }
  .search-wrapper {
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px dashed #e4e7ed;
    display: flex;
    flex-direction: column;
    gap: 8px;
    flex-shrink: 0;

    .wrapper_title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 12px;
    }

    .wrapper_content {
      display: flex;
      align-items: center;
      gap: 16px;
    }
  }

  .room-grid-section {
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px dashed #e4e7ed;
  }
}
</style>
