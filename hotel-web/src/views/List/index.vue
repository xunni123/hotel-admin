<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>入住列表</h2>
        <el-button style="display: none"></el-button>
        <div class="query-form">
          <el-input
            v-model="queryForm.searchText"
            placeholder="房间号/客人姓名/手机号"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
          />
          <el-button
            type="primary"
            @click="handleQuery"
            style="margin-left: 12px"
            >查询</el-button
          >
          <el-button @click="handleReset">重置</el-button>
        </div>
      </div>
      <MyTable :loading="loading" :data="data" :options="tableOptions">
        <template #roomNumber="{ scope }">
          <div style="display: flex; align-items: center">
            <el-icon><CaretRight /></el-icon>
            <span style="margin-left: 10px">{{ scope.row.roomNumber }}</span>
          </div>
        </template>

        <template #action="{ scope }">
          <el-button
            size="small"
            @click="handleEdit(scope.row)"
            :disabled="
              !loginStore.permissions.roomManagement ||
              !loginStore.permissions.canEdit
            "
            >编辑</el-button
          >
          <el-button
            size="small"
            @click="handleDelete(scope.row)"
            :disabled="
              !loginStore.permissions.roomManagement ||
              !loginStore.permissions.canDelete
            "
            >退房</el-button
          >
          <el-button
            size="small"
            @click="handleDetail(scope.row)"
            :disabled="!loginStore.permissions.roomManagement"
            >详情</el-button
          >
        </template>
      </MyTable>

      <MyDialog
        :title="'入住详情单'"
        :visible="visible"
        @confirm="handleConfirm"
        @cacel="handleCancel"
      >
        <template #content>
          <CheckinContent :selectedOrder="selectedOrder" />
        </template>
      </MyDialog>

      <Pagination
        v-model:page="current"
        v-model:limit="pageSize"
        :total="total"
        @query-change="handleQueryChange"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </Card>

    <CheckoutDialog
      v-model="checkoutVisible"
      :checkout-data="checkoutData"
      @success="handleCheckoutSuccess"
    />

    <!-- 编辑入住信息弹窗 -->
    <CheckinDrawer
      v-model="drawerVisible"
      :checkin-data="editCheckinData"
      @success="handleDrawerSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import MyTable from '@/components/MyTable.vue'
import Pagination from '@/components/Pagination.vue'
import MyDialog from '@/components/MyDialog.vue'
import CheckinContent from './CheckinContent.vue'
import CheckoutDialog from './CheckoutDialog.vue'
import CheckinDrawer from './CheckinDrawer.vue'
import { onMounted, onUnmounted, reactive, ref, watch } from 'vue'
import type { Table } from '@/types/table.ts'
import * as checkinApi from '@/api/room'
import { useTable } from '@/composables/role/useRole'
import { MessagePrompt } from '@/utils/message'
import { useLoginStore } from '@/store/login'
import { useCheckinStore } from '@/store/checkin'
import { useRoute } from 'vue-router'

const loginStore = useLoginStore()
const checkinStore = useCheckinStore()
const route = useRoute()

const visible = ref(false)

const {
  loading,
  data,
  current,
  pageSize,
  total,
  handleSizeChange,
  handleCurrentChange,
  fetchList,
  select,
  updateItem,
  deleteItem,
  loadCache,
  clearCache,
} = useTable(
  {
    fetchList: (query: any) => checkinApi.getCheckedInList(),
    update: (id: number, data: any) => checkinApi.updateCheckinInfo(data),
  },
  { cacheKey: 'checkin_table' },
)

const queryForm = reactive({
  searchText: '',
})

// drawer
const drawerVisible = ref(false)
const editCheckinData = ref<any>(null)

const tableOptions: Table[] = [
  { type: 'selection', align: 'center' },
  { label: '房间号', prop: 'roomNumber', align: 'left', slot: 'roomNumber' },
  { label: '客人姓名', prop: 'guestName', align: 'left' },
  { label: '入住日期', prop: 'checkInTime', align: 'left' },
  { label: '退房日期', prop: 'checkOutTime', align: 'left' },
  { label: '房型', prop: 'roomType', align: 'left' },
  { label: '楼层', prop: 'floor', align: 'left' },
  { label: '楼栋', prop: 'building', align: 'left' },
  { label: '状态', prop: 'statusName', align: 'center' },
  {
    label: '操作',
    prop: 'actions',
    actions: true,
    align: 'center',
    width: 250,
  },
]

const handleQuery = () => {
  const searchText = queryForm.searchText
  if (!searchText) {
    MessagePrompt('请输入搜索条件', 'warning')
    return
  }

  // 前端过滤
  const filteredData = data.value.filter((item: any) => {
    const roomNumber = item.roomNumber?.toLowerCase() || ''
    const guestName = item.guestName?.toLowerCase() || ''
    const search = searchText.toLowerCase()

    return roomNumber.includes(search) || guestName.includes(search)
  })

  if (filteredData.length > 0) {
    data.value = filteredData
    total.value = filteredData.length
    // 保存搜索状态到 store
    checkinStore.setSearchText(searchText)
    checkinStore.setIsSearched(true)
    checkinStore.setCheckinData(filteredData)
    MessagePrompt('查询成功', 'success')
  } else {
    MessagePrompt('未找到匹配的入住记录', 'info')
  }
}

const handleReset = () => {
  queryForm.searchText = ''
  checkinStore.clearSearch()
  clearCache()
  fetchList({ page: 1, pageSize: pageSize.value })
}

const handleDelete = (row: any) => {
  checkoutData.value = row
  checkoutVisible.value = true
}

// 存储选中的入住详情
const selectedOrder = ref<any>(null)

// 退房相关
const checkoutVisible = ref(false)
const checkoutData = ref<any>({})

const handleCheckoutSuccess = () => {
  checkoutVisible.value = false
  checkoutData.value = {}
  // 刷新列表
  fetchList()
}

//详情
const handleDetail = (row: any) => {
  selectedOrder.value = row
  visible.value = true
  // 保存详情状态到 store
  checkinStore.setSelectedOrder(row)
  checkinStore.setIsDetailVisible(true)
}

// detail confirm
const handleConfirm = () => {
  visible.value = false
  checkinStore.closeDetail()
}

// detail cancel
const handleCancel = () => {
  visible.value = false
  checkinStore.closeDetail()
}

// 编辑入住信息
const handleEdit = (row: any) => {
  editCheckinData.value = row
  drawerVisible.value = true
}

// drawer 成功回调
const handleDrawerSuccess = async () => {
  await fetchList()
}

const handleQueryChange = ({
  page,
  pageSize,
}: {
  page: number
  pageSize: number
}) => {
  fetchList({ page, pageSize })
}

let routeWatcher: ReturnType<typeof watch>

onMounted(() => {
  clearCache()
  fetchList()

  // 如果之前有搜索过，恢复搜索状态
  if (checkinStore.isSearched && checkinStore.checkinData.length > 0) {
    data.value = checkinStore.checkinData
    total.value = checkinStore.checkinData.length
  }

  routeWatcher = watch(
    () => route.path,
    () => {
      clearCache()
      fetchList()
    },
  )
})

onUnmounted(() => {
  if (routeWatcher) {
    routeWatcher()
  }
})
</script>

<style scoped lang="scss">
.card-table {
  .card-item {
    display: flex;
    flex-direction: column;
    padding: 24px;
    width: 100%;
    box-sizing: border-box;
  }

  .header-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 1px solid #f0f0f0;

    h2 {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
      color: #303133;
    }

    .query-form {
      display: flex;
      align-items: center;
    }
  }
  :deep(.el-button) {
    background-color: var(--tabs);
    color: #fff;
    border: var(--tabs);
    transition: all 0.2s ease;
    transform-origin: center;
  }
  :deep(.el-button:nth-child(2)) {
    background-color: var(--danger);
  }
  :deep(.el-button:hover) {
    filter: brightness(1.1);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}
</style>
