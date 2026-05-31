<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>预订管理</h2>
        <el-button style="display: none"></el-button>
        <div class="query-form">
          <el-input
            v-model="queryForm.orderTel"
            placeholder="请输入手机号"
            clearable
            style="width: 200px"
          />
          <el-button
            type="primary"
            @click="handleQuery"
            style="margin-left: 10px"
            >查询
            <el-icon style="margin-left: 10px"><Search /></el-icon>
          </el-button>
          <el-button
            type="primary"
            @click="handleCreate"
            :disabled="!loginStore.permissions.bookingManagement"
          >
            新建预订
            <el-icon style="margin-left: 10px"><Plus /></el-icon>
          </el-button>
        </div>
      </div>
      <MyTable
        :loading="loading"
        ref="bookingTableRef"
        :data="data"
        :options="tableOptions"
        :editIcon="'Edit'"
        :canEdit="
          loginStore.permissions.bookingManagement &&
          loginStore.permissions.canEdit
        "
        @confirm="confirm"
        @cancel="cancel"
        @row-save="handleSaveRow"
        @row-cancel="handleRowCacel"
      >
        <template #date="{ scope }">
          <div style="display: flex; align-items: center">
            <el-icon><CaretRight /></el-icon>
            <span style="margin-left: 10px">{{ scope.row.orderNo }}</span>
          </div>
        </template>

        <template #action="{ scope }">
          <el-button
            size="small"
            @click="startRowEdit(scope.$index)"
            :disabled="
              !loginStore.permissions.bookingManagement ||
              !loginStore.permissions.canEdit
            "
            >编辑</el-button
          >
          <el-button
            size="small"
            @click="handleDelete(scope.row)"
            :disabled="
              !loginStore.permissions.bookingManagement ||
              !loginStore.permissions.canDelete
            "
            >删除</el-button
          >
          <el-button
            size="small"
            @click="handleDetail(scope.row)"
            :disabled="!loginStore.permissions.bookingManagement"
            >详情</el-button
          >
          <MyDialog
            :title="'预定详情单'"
            :visible="visible"
            @confirm="handleConnfirm"
            @cacel="handleCacel"
          >
            <template #content>
              <DialogContent :selectedOrder="selectedOrder" />
            </template>
          </MyDialog>
        </template>
      </MyTable>
      <Pagination
        v-model:page="current"
        v-model:limit="pageSize"
        :total="total"
        @query-change="handleQueryChange"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </Card>

    <BookingDialog ref="bookingDialogRef" />
  </div>
</template>

<script setup lang="ts">
import MyTable from '@/components/MyTable.vue'
import Pagination from '@/components/Pagination.vue'
import BookingDialog from './BookingDialog.vue'
import MyDialog from '@/components/MyDialog.vue'
import DialogContent from './DialogContent.vue'
import { onMounted, reactive, ref } from 'vue'
import type { Table } from '@/types'
import * as bookingApi from '@/api/booking'
import { useTable } from '@/composables/role/useRole'
import { MessagePrompt } from '@/utils/message'
import { useLoginStore } from '@/store/login'

const loginStore = useLoginStore()

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
    fetchList: (query: any) =>
      bookingApi.getAllBooking(query.page, query.pageSize),
    update: (id: string | number, data: any) => bookingApi.updateBooking(data),
  },
  { cacheKey: 'booking_table' },
)

const queryForm = reactive({
  orderTel: '',
})

const bookingTableRef = ref<InstanceType<typeof MyTable>>()
const bookingDialogRef = ref(false)

const tableOptions: Table[] = [
  { type: 'selection', align: 'center' },
  { label: '订单编号', prop: 'orderNo', align: 'left', slot: 'date' },
  { label: '房间号', prop: 'roomNumber', align: 'left' },
  { label: '客人姓名', prop: 'guestName', align: 'left' },
  { label: '联系电话', prop: 'guestPhone', align: 'left', editable: true },
  { label: '入住日期', prop: 'checkInDate', align: 'left' },
  {
    label: '订单状态',
    prop: 'orderStatus',
    align: 'center',
    slot: 'orderStatus',
  },

  {
    label: '操作',
    prop: 'actions',
    actions: true,
    align: 'center',
    width: 250,
  },
]

const handleQuery = () => {
  const orderTel = queryForm.orderTel
  if (!orderTel) {
    MessagePrompt('请输入手机号', 'warning')
    return
  }
  bookingApi.getBookingByPhone(orderTel).then((res) => {
    if (res.data) {
      data.value = [res.data]
      total.value = 1
    } else {
      data.value = []
      total.value = 0
      MessagePrompt('未找到订单', 'info')
    }
  })
}

const handleDelete = (row: any) => {
  MessagePrompt('删除订单功能开发中', 'info')
}

// 存储选中的订单详情
const selectedOrder = ref<any>(null)

//详情
const handleDetail = (row: any) => {
  selectedOrder.value = row
  visible.value = true
}

//detail confirm
const handleConnfirm = () => {
  visible.value = false
}

// detail cancel
const handleCacel = () => {
  visible.value = false
}

const startRowEdit = (rowIndex: number) => {
  bookingTableRef.value?.startEdit(rowIndex)
}

const handleSaveRow = ({ rowIdx, newRow, oldRow }: any) => {
  updateItem(rowIdx, newRow, oldRow, (row: any) => row.orderId)
}

const handleRowCacel = ({ rowIdx, oldRow }: any) => {
  console.log('取消编辑', rowIdx)
}

const confirm = ({ Idx, row, prop, newVal, oldVal }: any) => {
  updateItem(Idx, row, data.value[Idx], (row: any) => row.orderId)
}

const cancel = ({ row, prop, oldVal }: any) => {}

const handleQueryChange = ({
  page,
  pageSize,
}: {
  page: number
  pageSize: number
}) => {
  fetchList({ page, pageSize })
}

// 新建预定
const handleCreate = () => {
  bookingDialogRef.value.visible = true
}

onMounted(() => {
  clearCache()
  fetchList()
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
  }

  .header-wrapper h2 {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }

  .query-form {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-shrink: 0;
  }

  .table-heade {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    gap: 12px;
    padding-top: 8px;
    margin-top: 0;
    margin-bottom: 16px;
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
  .order-detail {
    padding: 16px;
    height: 100%;
    overflow-y: auto;
    box-sizing: border-box;
  }
}
</style>
