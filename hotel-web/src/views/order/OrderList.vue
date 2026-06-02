<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>订单列表</h2>
        <el-button style="display: none"></el-button>
        <div class="query-form">
          <el-input
            v-model="queryForm.searchText"
            placeholder="订单号/房间号/客人姓名"
            clearable
            style="width: 200px"
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
        <template #orderNo="{ scope }">
          <div style="display: flex; align-items: center">
            <el-icon><CaretRight /></el-icon>
            <span style="margin-left: 10px">{{ scope.row.orderNo }}</span>
          </div>
        </template>

        <template #action="{ scope }">
          <el-button
            size="small"
            @click="handleEdit(scope.row)"
            :disabled="
              !loginStore.permissions.orderManagement ||
              !loginStore.permissions.canEdit
            "
            >编辑</el-button
          >
          <el-button
            size="small"
            @click="handleDetail(scope.row)"
            :disabled="!loginStore.permissions.orderManagement"
            >详情</el-button
          >
        </template>
      </MyTable>

      <MyDialog
        :title="'订单详情'"
        :visible="visible"
        @confirm="handleConfirm"
        @cacel="handleCancel"
      >
        <template #content>
          <OrderContent :selectedOrder="selectedOrder" />
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

    <OrderDrawer
      v-model="drawerVisible"
      :order-data="editOrderData"
      :is-edit="isEdit"
      @success="handleDrawerSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import MyTable from '@/components/MyTable.vue'
import Pagination from '@/components/Pagination.vue'
import MyDialog from '@/components/MyDialog.vue'
import OrderContent from './OrderContent.vue'
import OrderDrawer from './OrderDrawer.vue'
import { onMounted, onUnmounted, reactive, ref, watch } from 'vue'
import type { Table } from '@/types'
import * as orderApi from '@/api/order'
import { useTable } from '@/composables/role/useRole'
import { MessagePrompt } from '@/utils/message'
import { useLoginStore } from '@/store/login'
import { useRoute } from 'vue-router'

const loginStore = useLoginStore()
const route = useRoute()

const visible = ref(false)
const drawerVisible = ref(false)
const editOrderData = ref<any>({})
const isEdit = ref(false)

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
    fetchList: (query: any) => orderApi.getOrderList(),
  },
  { cacheKey: 'order_table' },
)

const queryForm = reactive({
  searchText: '',
})

const tableOptions: Table[] = [
  { type: 'selection', align: 'center' },
  { label: '订单号', prop: 'orderNo', align: 'left', slot: 'orderNo' },
  { label: '房间号', prop: 'roomNumber', align: 'left' },
  { label: '客人姓名', prop: 'guestName', align: 'left' },
  { label: '入住日期', prop: 'checkInDate', align: 'left' },
  { label: '退房日期', prop: 'checkOutDate', align: 'left' },
  { label: '金额', prop: 'totalAmount', align: 'right' },
  { label: '状态', prop: 'orderStatus', align: 'center' },
  {
    label: '操作',
    prop: 'actions',
    actions: true,
    align: 'center',
    width: 200,
  },
]

const handleQuery = () => {
  const searchText = queryForm.searchText
  if (!searchText) {
    MessagePrompt('请输入搜索条件', 'warning')
    return
  }

  const filteredData = data.value.filter((item: any) => {
    const orderNo = item.orderNo?.toLowerCase() || ''
    const roomNumber = item.roomNumber?.toLowerCase() || ''
    const guestName = item.guestName?.toLowerCase() || ''
    const search = searchText.toLowerCase()

    return (
      orderNo.includes(search) ||
      roomNumber.includes(search) ||
      guestName.includes(search)
    )
  })

  if (filteredData.length > 0) {
    data.value = filteredData
    total.value = filteredData.length
    MessagePrompt('查询成功', 'success')
  } else {
    MessagePrompt('未找到匹配的订单', 'info')
  }
}

const handleReset = () => {
  queryForm.searchText = ''
  clearCache()
  fetchList({ page: 1, pageSize: pageSize.value })
}

const selectedOrder = ref<any>({})

const handleDetail = (row: any) => {
  selectedOrder.value = row
  visible.value = true
}

const handleEdit = (row: any) => {
  editOrderData.value = { ...row }
  isEdit.value = true
  drawerVisible.value = true
}

const handleDrawerSuccess = () => {
  fetchList()
}

const handleConfirm = () => {
  visible.value = false
}

const handleCancel = () => {
  visible.value = false
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
