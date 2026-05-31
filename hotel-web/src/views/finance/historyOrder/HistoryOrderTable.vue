<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>历史订单</h2>
        <div class="query-form">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 300px"
          />
          <el-input
            v-model="queryForm.keyword"
            placeholder="订单号/房间号/客人姓名"
            clearable
            style="width: 200px"
          />
          <el-select
            v-model="queryForm.status"
            placeholder="订单状态"
            clearable
            style="width: 150px"
          >
            <el-option label="全部" value="" />
            <el-option label="已完成" value="completed" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleExport">导出</el-button>
        </div>
      </div>

      <MyTable :loading="loading" :data="data" :options="tableOptions">
        <template #status="{ scope }">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
        <template #totalAmount="{ scope }">
          <span style="color: #f56c6c; font-weight: bold"
            >¥{{ scope.row.totalAmount }}</span
          >
        </template>
      </MyTable>

      <Pagination
        v-model:page="current"
        v-model:limit="pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import MyTable from '@/components/MyTable.vue'
import Pagination from '@/components/Pagination.vue'
import Card from '@/components/Card.vue'
import { ElMessage } from 'element-plus'
import { MessagePrompt } from '@/utils/message'
import { exportToExcel } from '@/utils/export'
import type { Table } from '@/types'
import dayjs from 'dayjs'

const loading = ref(false)
const allData = ref<any[]>([])
const data = ref<any[]>([])
const current = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dateRange = ref<string[]>([])
const queryForm = reactive({
  keyword: '',
  status: '',
})

// 导出列定义
const exportColumns = [
  { prop: 'orderNo', label: '订单号' },
  { prop: 'roomNumber', label: '房间号' },
  { prop: 'guestName', label: '客人姓名' },
  { prop: 'guestPhone', label: '联系电话' },
  { prop: 'checkInDate', label: '入住日期' },
  { prop: 'checkOutDate', label: '退房日期' },
  { prop: 'nights', label: '入住天数' },
  { prop: 'totalAmount', label: '订单金额' },
  { prop: 'paymentMethod', label: '支付方式' },
  { prop: 'status', label: '状态' },
  { prop: 'createTime', label: '创建时间' },
]

const tableOptions: Table[] = [
  { label: '订单号', prop: 'orderNo', align: 'left' },
  { label: '房间号', prop: 'roomNumber', align: 'center' },
  { label: '客人姓名', prop: 'guestName', align: 'left' },
  { label: '联系电话', prop: 'guestPhone', align: 'center' },
  { label: '入住日期', prop: 'checkInDate', align: 'center' },
  { label: '退房日期', prop: 'checkOutDate', align: 'center' },
  { label: '入住天数', prop: 'nights', align: 'center' },
  {
    label: '订单金额',
    prop: 'totalAmount',
    align: 'right',
    slot: 'totalAmount',
  },
  { label: '支付方式', prop: 'paymentMethod', align: 'center' },
  { label: '状态', prop: 'status', align: 'center', slot: 'status' },
  { label: '创建时间', prop: 'createTime', align: 'center' },
]

const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    completed: 'success',
    cancelled: 'info',
  }
  return map[status] || 'info'
}

const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    completed: '已完成',
    cancelled: '已取消',
  }
  return map[status] || status
}

// 过滤数据
const filterData = () => {
  let filtered = [...allData.value]

  // 按关键词过滤
  if (queryForm.keyword) {
    const keyword = queryForm.keyword.toLowerCase()
    filtered = filtered.filter(
      (item) =>
        item.orderNo?.toLowerCase().includes(keyword) ||
        item.roomNumber?.toLowerCase().includes(keyword) ||
        item.guestName?.toLowerCase().includes(keyword),
    )
  }

  // 按状态过滤
  if (queryForm.status) {
    filtered = filtered.filter((item) => item.status === queryForm.status)
  }

  // 按日期范围过滤
  if (dateRange.value && dateRange.value.length === 2) {
    const [startDate, endDate] = dateRange.value
    filtered = filtered.filter((item) => {
      const checkInDate = item.checkInDate
      return checkInDate >= startDate && checkInDate <= endDate
    })
  }

  // 分页
  const start = (current.value - 1) * pageSize.value
  const end = start + pageSize.value
  data.value = filtered.slice(start, end)
  total.value = filtered.length
}

const fetchList = async () => {
  loading.value = true
  try {
    // 模拟数据 - 实际应该从API获取
    allData.value = [
      {
        orderNo: 'ORD20240501001',
        roomNumber: '101',
        guestName: '张三',
        guestPhone: '13800138001',
        checkInDate: '2024-05-01',
        checkOutDate: '2024-05-03',
        nights: 2,
        totalAmount: 598,
        paymentMethod: '微信支付',
        status: 'completed',
        createTime: '2024-05-01 14:30:00',
      },
      {
        orderNo: 'ORD20240502002',
        roomNumber: '205',
        guestName: '李四',
        guestPhone: '13800138002',
        checkInDate: '2024-05-02',
        checkOutDate: '2024-05-05',
        nights: 3,
        totalAmount: 888,
        paymentMethod: '支付宝',
        status: 'completed',
        createTime: '2024-05-02 09:15:00',
      },
    ]

    // 将状态转换为文本
    allData.value = allData.value.map((item) => ({
      ...item,
      status: getStatusText(item.status),
    }))

    filterData()
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  current.value = 1
  filterData()
}

const handleReset = () => {
  queryForm.keyword = ''
  queryForm.status = ''
  dateRange.value = []
  current.value = 1
  filterData()
  MessagePrompt('已重置', 'success')
}

const handleExport = () => {
  if (total.value === 0) {
    MessagePrompt('没有数据可导出', 'warning')
    return
  }

  ElMessageBox.confirm(
    `确定要导出 ${total.value} 条历史订单数据吗？`,
    '确认导出',
    {
      confirmButtonText: '确定导出',
      cancelButtonText: '取消',
      type: 'info',
    },
  )
    .then(() => {
      // 获取所有过滤后的数据（不分页）
      let exportData = [...allData.value]

      if (
        queryForm.keyword ||
        queryForm.status ||
        (dateRange.value && dateRange.value.length === 2)
      ) {
        // 如果有查询条件，重新过滤
        filterData()
        exportData = [...data.value]
        // 恢复分页
        const start = (current.value - 1) * pageSize.value
        const end = start + pageSize.value
        data.value = exportData.slice(start, end)
      }

      exportToExcel(exportData, exportColumns, '历史订单')
      MessagePrompt('导出成功', 'success')
    })
    .catch(() => {
      MessagePrompt('已取消导出', 'info')
    })
}

const handleCurrentChange = (page: number) => {
  current.value = page
  fetchList()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  fetchList()
}

onMounted(() => {
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
    font-size: 20px;
    font-weight: 600;
    color: #303133;
  }

  .query-form {
    display: flex;
    align-items: center;
    gap: 12px;
    flex-wrap: wrap;
  }

  :deep(.el-button) {
    background-color: var(--tabs);
    color: #fff;
    border: var(--tabs);
    transition: all 0.2s ease;
  }

  :deep(.el-button:nth-child(2)) {
    background-color: #909399;
  }

  :deep(.el-button:nth-child(3)) {
    background-color: var(--success);
  }

  :deep(.el-button:hover) {
    filter: brightness(1.1);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}
</style>
