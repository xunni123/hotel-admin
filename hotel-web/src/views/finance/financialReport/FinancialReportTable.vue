<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>财务报表</h2>
      </div>

      <!-- 查询表单 -->
      <div class="query-form-wrapper">
        <el-form :inline="true" :model="queryForm">
          <el-form-item label="类型">
            <el-select
              v-model="queryForm.type"
              placeholder="全部类型"
              style="width: 120px"
            >
              <el-option label="全部" value="" />
              <el-option label="收入" value="income" />
              <el-option label="支出" value="expense" />
            </el-select>
          </el-form-item>

          <el-form-item label="支付方式">
            <el-select
              v-model="queryForm.paymentMethod"
              placeholder="全部方式"
              style="width: 140px"
            >
              <el-option label="全部" value="" />
              <el-option label="微信支付" value="wechat" />
              <el-option label="支付宝" value="alipay" />
              <el-option label="现金" value="cash" />
              <el-option label="银行卡" value="bank" />
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
            <el-button type="success" @click="handleAdd">新增记录</el-button>
            <el-button @click="handleExport">导出</el-button>
          </el-form-item>
        </el-form>
      </div>

      <h3 style="margin-top: 0; margin-bottom: 16px">收支明细</h3>
      <MyTable :loading="loading" :data="data" :options="tableOptions">
        <template #type="{ scope }">
          <el-tag :type="scope.row.type === 'income' ? 'success' : 'danger'">
            {{ scope.row.type === 'income' ? '收入' : '支出' }}
          </el-tag>
        </template>
        <template #amount="{ scope }">
          <span
            :style="{
              color: scope.row.type === 'income' ? '#67c23a' : '#f56c6c',
              fontWeight: 'bold',
            }"
          >
            {{ scope.row.type === 'income' ? '+' : '-' }}¥{{ scope.row.amount }}
          </span>
        </template>
        <template #paymentMethod="{ scope }">
          <span>{{ getPaymentMethodText(scope.row.paymentMethod) }}</span>
        </template>
        <template #action="{ scope }">
          <el-button
            size="small"
            @click="handleEdit(scope.row)"
            :disabled="
              !loginStore.permissions.financialManagement ||
              !loginStore.permissions.canEdit
            "
            >编辑</el-button
          >
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.row)"
            :disabled="
              !loginStore.permissions.financialManagement ||
              !loginStore.permissions.canDelete
            "
            >删除</el-button
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

      <div class="stats-cards">
        <div class="stat-card">
          <div class="stat-label">今日收入</div>
          <div class="stat-value">¥{{ todayRevenue }}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">今日订单数</div>
          <div class="stat-value">{{ todayOrders }}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">本月收入</div>
          <div class="stat-value">¥{{ monthRevenue }}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">本月订单数</div>
          <div class="stat-value">{{ monthOrders }}</div>
        </div>
      </div>

      <div class="charts-container">
        <RevenueChart />
        <PaymentChart />
      </div>
    </Card>

    <!-- 新增/编辑弹窗 -->
    <FinancialDialog
      v-model:visible="dialogVisible"
      :is-edit="isEdit"
      :row-data="currentRow"
      @confirm="handleDialogConfirm"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, reactive } from 'vue'
import MyTable from '@/components/MyTable.vue'
import Pagination from '@/components/Pagination.vue'
import Card from '@/components/Card.vue'
import { ElMessageBox } from 'element-plus'
import { MessagePrompt } from '@/utils/message'
import { exportToExcel } from '@/utils/export'
import type { Table } from '@/types/table'
import * as financialApi from '@/api/financialRecord'
import { useLoading } from '@/composables/useLoading'
import { useLoginStore } from '@/store/login'
import RevenueChart from './RevenueChart.vue'
import PaymentChart from './PaymentChart.vue'
import FinancialDialog from './FinancialDialog.vue'

const loginStore = useLoginStore()
const { loading, startLoading, stopLoading } = useLoading(500)
const allData = ref<any[]>([])
const data = ref<any[]>([])
const current = ref(1)
const pageSize = ref(10)
const total = ref(0)

const dialogVisible = ref(false)
const isEdit = ref(false)
const currentRow = ref<any>(null)

// 查询表单
const queryForm = reactive({
  type: '',
  paymentMethod: '',
})

const exportColumns = [
  { prop: 'id', label: '记录ID' },
  { prop: 'type', label: '类型' },
  { prop: 'amount', label: '金额' },
  { prop: 'paymentMethod', label: '支付方式' },
  { prop: 'orderNo', label: '关联订单' },
  { prop: 'remark', label: '备注' },
  { prop: 'operator', label: '操作人' },
  { prop: 'createTime', label: '创建时间' },
]

const todayRevenue = ref(8888)
const todayOrders = ref(25)
const monthRevenue = ref(258600)
const monthOrders = ref(680)

const tableOptions: Table[] = [
  { label: '记录ID', prop: 'id', align: 'center' },
  { label: '类型', prop: 'type', align: 'center', slot: 'type' },
  { label: '金额', prop: 'amount', align: 'right', slot: 'amount' },
  {
    label: '支付方式',
    prop: 'paymentMethod',
    align: 'center',
    slot: 'paymentMethod',
  },
  { label: '关联订单', prop: 'orderNo', align: 'center' },
  { label: '备注', prop: 'remark', align: 'left' },
  { label: '操作人', prop: 'operator', align: 'center' },
  { label: '创建时间', prop: 'createTime', align: 'center' },
  {
    label: '操作',
    prop: 'actions',
    actions: true,
    align: 'center',
    width: 160,
  },
]

const getPaymentMethodText = (method: string) => {
  const map: Record<string, string> = {
    wechat: '微信支付',
    alipay: '支付宝',
    cash: '现金',
    bank: '银行卡',
  }
  return map[method] || method
}

const filterData = () => {
  const start = (current.value - 1) * pageSize.value
  const end = start + pageSize.value
  data.value = allData.value.slice(start, end)
  total.value = allData.value.length
}

const fetchList = async (queryParams?: any) => {
  startLoading()
  try {
    const params: any = { ...queryParams }

    // 如果有日期范围，添加到参数中
    if (params.startDate || params.endDate) {
      if (params.startDate) delete params.dateValue
      if (params.endDate) delete params.reportType
    }

    // 移除空参数
    Object.keys(params).forEach((key) => {
      if (!params[key]) delete params[key]
    })

    const result = await financialApi.getFinancialRecordList(params)
    if (result.code === 200) {
      allData.value = result.data
      filterData()
      // 更新统计数据
      const summaryResult = await financialApi.getFinancialSummary()
      if (summaryResult.code === 200) {
        todayRevenue.value = summaryResult.data.income
        todayOrders.value = summaryResult.data.orderCount
        monthRevenue.value = summaryResult.data.income * 30
        monthOrders.value = summaryResult.data.orderCount * 30
      }
    }
  } catch (error) {
    MessagePrompt('获取数据失败', 'error')
  } finally {
    stopLoading()
  }
}

const handleAdd = () => {
  isEdit.value = false
  currentRow.value = null
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  currentRow.value = row
  dialogVisible.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除该记录吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      try {
        const result = await financialApi.deleteFinancialRecord(row.recordId)
        if (result.code === 200) {
          MessagePrompt('删除成功', 'success')
          fetchList()
        } else {
          MessagePrompt(result.message || '删除失败', 'error')
        }
      } catch (error) {
        MessagePrompt('删除失败', 'error')
      }
    })
    .catch(() => {})
}

const handleDialogConfirm = () => {
  handleQuery()
}

const handleQuery = () => {
  current.value = 1

  // 构建查询参数
  const params: any = {
    type: queryForm.type || undefined,
    paymentMethod: queryForm.paymentMethod || undefined,
  }

  fetchList(params)
  MessagePrompt('查询成功', 'success')
}

const handleReset = () => {
  queryForm.type = ''
  queryForm.paymentMethod = ''
  current.value = 1
  fetchList()
}

const handleExport = () => {
  if (total.value === 0) {
    MessagePrompt('没有数据可导出', 'warning')
    return
  }
  ElMessageBox.confirm(
    `确定要导出 ${total.value} 条财务记录数据吗？`,
    '确认导出',
    {
      confirmButtonText: '确定导出',
      cancelButtonText: '取消',
      type: 'info',
    },
  )
    .then(() => {
      exportToExcel([...allData.value], exportColumns, '财务报表')
      MessagePrompt('导出成功', 'success')
    })
    .catch(() => {
      MessagePrompt('已取消导出', 'info')
    })
}

const handleCurrentChange = (page: number) => {
  current.value = page
  filterData()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  current.value = 1
  filterData()
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

  .query-form-wrapper {
    margin-bottom: 24px;
    padding: 16px;
    background: #f5f7fa;
    border-radius: 8px;
  }

  :deep(.el-form-item) {
    margin-bottom: 0;
  }

  .stats-cards {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
    margin-bottom: 24px;
  }

  .stat-card {
    background: var(--tabs);
    padding: 24px;
    border-radius: 8px;
    color: #fff;
  }

  .stat-card:nth-child(2) {
    background: var(--card);
  }

  .stat-card:nth-child(3) {
    background: var(--info);
  }

  .stat-card:nth-child(4) {
    background: var(--success);
  }

  .stat-label {
    font-size: 14px;
    opacity: 0.9;
    margin-bottom: 8px;
  }

  .stat-value {
    font-size: 28px;
    font-weight: bold;
  }

  .charts-container {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 24px;
    margin-bottom: 24px;
  }

  :deep(.el-button) {
    margin-right: 8px;
  }
}
</style>
