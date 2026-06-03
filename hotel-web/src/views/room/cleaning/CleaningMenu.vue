<template>
  <div class="cleaning-menu">
    <!-- 搜索和派单 -->
    <div class="search-section">
      <div class="search-box">
        <el-icon class="search-icon"><Search /></el-icon>
        <el-input
          v-model="filters.searchText"
          placeholder="房号/保洁员"
          class="search-input"
        />
      </div>
    </div>

    <!-- 保洁人员列表 -->
    <div class="cleaner-section">
      <div class="wrapper_title">
        <div class="menu-title">保洁人员</div>
      </div>
      <div class="wrapper_content">
        <div
          v-for="cleaner in filteredCleaners"
          :key="cleaner.cleanerId"
          class="cleaner-item"
          :class="{
            selected: selectedCleaner?.cleanerId === cleaner.cleanerId,
          }"
          @click="selectCleaner(cleaner)"
        >
          <el-avatar :size="40" :src="cleaner.avatar">
            {{ cleaner.cleanerName?.charAt(0) }}
          </el-avatar>
          <div class="cleaner-info">
            <div class="cleaner-name">{{ cleaner.cleanerName }}</div>
            <div class="cleaner-status">
              <span class="status-dot" :class="cleaner.status"></span>
              {{ getStatusText(cleaner.status, cleaner) }}
            </div>
          </div>
          <div class="cleaner-tasks">{{ cleaner.taskCount || 0 }} 任务</div>
        </div>
      </div>
    </div>

    <!-- 派单按钮 -->
    <el-button
      v-if="selectedCleaner && selectedRooms.length > 0"
      type="primary"
      class="assign-btn"
      @click="handleAssign"
      :loading="assignLoading"
    >
      派单给 {{ selectedCleaner.cleanerName }} ({{ selectedRooms.length }} 间)
    </el-button>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { MessagePrompt } from '@/utils/message'
import * as cleaningApi from '@/api/cleaning'

interface Cleaner {
  cleanerId: number
  cleanerName: string
  phone?: string
  avatar?: string
  status: 'available' | 'busy' | 'offline'
  taskCount: number
  originalStatus?: string
}

const emit = defineEmits<{
  (e: 'assign', cleaner: Cleaner, roomIds: number[]): void
}>()

//keyword
const filters = reactive({
  searchText: '',
})

const cleaners = ref<Cleaner[]>([])
const selectedCleaner = ref<Cleaner | null>(null)
const selectedRooms = ref<number[]>([])
const assignLoading = ref(false)

// 过滤员工
const filteredCleaners = computed(() => {
  if (!filters.searchText) return cleaners.value
  const search = filters.searchText.toLowerCase()
  return cleaners.value.filter((c) =>
    c.cleanerName.toLowerCase().includes(search),
  )
})

// 加载员工当前状态
const loadCleaners = async () => {
  try {
    const res = await cleaningApi.getCleanerWithTaskCount()
    cleaners.value = res.data.map((item: any) => {
      const taskCount = item.taskCount || 0
      const originalStatus = item.status || 'available'
      const displayStatus = taskCount >= 3 ? 'busy' : 'available'
      return {
        ...item,
        taskCount,
        originalStatus,
        status: displayStatus,
      }
    })
    cleaners.value.sort((a, b) => {
      if (a.status === 'available' && b.status !== 'available') return -1
      if (a.status !== 'available' && b.status === 'available') return 1
      return a.taskCount - b.taskCount
    })
  } catch (error) {
    MessagePrompt('加载保洁员失败:', error)
  }
}

//选择员工
const selectCleaner = (cleaner: Cleaner) => {
  selectedCleaner.value = cleaner
}

//获取状态
const getStatusText = (status: string, cleaner: Cleaner) => {
  if (cleaner.taskCount > 3) {
    return '忙碌'
  }
  const statusMap: Record<string, string> = {
    available: '空闲',
    busy: '忙碌',
    offline: '离线',
  }
  return statusMap[status] || status
}

//开始派单
const handleAssign = async () => {
  if (!selectedCleaner.value || selectedRooms.value.length === 0) {
    MessagePrompt('请选择保洁人员和房间', 'warning')
    return
  }

  assignLoading.value = true
  try {
    await cleaningApi.assignTask({
      cleanerId: selectedCleaner.value.cleanerId,
      roomIds: selectedRooms.value,
      assignedBy: 'admin',
    })
    MessagePrompt('派单成功', 'success')
    emit('assign', selectedCleaner.value, selectedRooms.value)
    selectedRooms.value = []
    await loadCleaners()
  } catch (error) {
    MessagePrompt('派单失败', 'error')
  } finally {
    assignLoading.value = false
  }
}

defineExpose({
  setSelectedRooms: (rooms: number[]) => {
    selectedRooms.value = rooms
  },
  loadCleaners: () => {
    loadCleaners()
  },
})

onMounted(() => {
  loadCleaners()
})
</script>

<style scoped lang="scss">
.cleaning-menu {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  padding-right: 8px;
  background: var(--bg-page);

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background: var(--border);
    border-radius: 3px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
  }

  .search-section {
    display: flex;
    align-items: center;
    gap: 16px;
    flex-shrink: 0;
    margin-bottom: 20px;

    .search-box {
      flex: 1;
      position: relative;
      background: #fff;
      border-radius: 8px;
      border: 1px solid var(--border);
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
      color: var(--info);
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
    }
  }

  .wrapper_title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 12px;
    margin-bottom: 12px;
    padding: 0 4px;

    .menu-title {
      font-size: 16px;
      font-weight: bold;
      color: var(--text-primary);
    }
  }

  .cleaner-section {
    flex: 1;
    display: flex;
    flex-direction: column;
  }

  .cleaner-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 14px;
    background: #fff;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.25s ease;
    border: 2px solid transparent;
    margin-bottom: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

    &:hover {
      background: #fff;
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    &.selected {
      background: linear-gradient(135deg, var(--tabs) 0%, var(--card) 100%);
      border-color: var(--card);
      box-shadow: 0 4px 15px rgba(56, 88, 191, 0.3);

      .cleaner-name {
        color: #fff;
      }

      .cleaner-status {
        color: rgba(255, 255, 255, 0.9);

        .status-dot {
          &.available {
            background: #90cdf4;
            box-shadow: 0 0 6px rgba(144, 205, 244, 0.6);
          }
          &.busy {
            background: #fbd38d;
            box-shadow: 0 0 6px rgba(251, 211, 141, 0.6);
          }
          &.offline {
            background: rgba(255, 255, 255, 0.5);
          }
        }
      }

      .cleaner-tasks {
        background: rgba(255, 255, 255, 0.2);
        color: #fff;
      }
    }

    .cleaner-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 6px;

      .cleaner-name {
        font-size: 15px;
        font-weight: 600;
        color: var(--text-primary);
      }

      .cleaner-status {
        font-size: 13px;
        color: var(--info);
        display: flex;
        align-items: center;
        gap: 6px;

        .status-dot {
          width: 8px;
          height: 8px;
          border-radius: 50%;
          transition: all 0.3s ease;

          &.available {
            background: var(--success);
            box-shadow: 0 0 6px rgba(39, 174, 96, 0.5);
          }
          &.busy {
            background: var(--warning);
            box-shadow: 0 0 6px rgba(230, 126, 34, 0.5);
          }
          &.offline {
            background: #909399;
          }
        }
      }
    }

    .cleaner-tasks {
      font-size: 12px;
      font-weight: 500;
      color: var(--card);
      background: var(--bg-page);
      padding: 6px 12px;
      border-radius: 6px;
      transition: all 0.2s ease;
    }
  }

  .assign-btn {
    width: 100%;
    margin-top: 20px;
    padding: 14px;
    font-size: 15px;
    font-weight: 600;
    border-radius: 8px;
    background: linear-gradient(135deg, var(--tabs) 0%, var(--card) 100%);
    border: none;
    color: #fff;
    transition: all 0.25s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(56, 88, 191, 0.4);
    }

    &:active {
      transform: translateY(0);
    }
  }
}
</style>
