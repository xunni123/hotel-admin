<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>财务报表</h2>
        <div class="query-form">
          <el-radio-group v-model="reportType" @change="handleReportTypeChange">
            <el-radio-button value="day">日报</el-radio-button>
            <el-radio-button value="week">周报</el-radio-button>
            <el-radio-button value="month">月报</el-radio-button>
            <el-radio-button value="year">年报</el-radio-button>
          </el-radio-group>
          <el-date-picker
            v-model="dateValue"
            :type="datePickerType"
            :placeholder="datePickerPlaceholder"
            value-format="YYYY-MM-DD"
            style="width: 200px"
          />
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button type="success" @click="handleAdd">新增记录</el-button>
          <el-button @click="handleExport">导出</el-button>
        </div>
      </div>

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
        <div class="chart-item">
          <h3>收入趋势</h3>
          <div ref="revenueChartRef" class="chart-wrapper"></div>
        </div>
        <div class="chart-item">
          <h3>支付方式占比</h3>
          <div ref="paymentChartRef" class="chart-wrapper"></div>
        </div>
      </div>

      <h3 style="margin-top: 24px; margin-bottom: 16px">收支明细</h3>
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
    </Card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑记录' : '新增记录'"
      width="550px"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="formData.type">
            <el-radio label="income">收入</el-radio>
            <el-radio label="expense">支出</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number
            v-model="formData.amount"
            :min="0"
            :precision="2"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="支付方式" prop="paymentMethod">
          <el-select
            v-model="formData.paymentMethod"
            placeholder="请选择支付方式"
            style="width: 100%"
          >
            <el-option label="微信支付" value="wechat" />
            <el-option label="支付宝" value="alipay" />
            <el-option label="现金" value="cash" />
            <el-option label="银行卡" value="bank" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联订单" prop="orderNo">
          <el-input v-model="formData.orderNo" placeholder="请输入关联订单号" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
        <el-form-item label="操作人" prop="operator">
          <el-input v-model="formData.operator" placeholder="请输入操作人" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting"
          >确定</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, computed, reactive } from 'vue'
import MyTable from '@/components/MyTable.vue'
import Pagination from '@/components/Pagination.vue'
import Card from '@/components/Card.vue'
import { ElMessageBox, ElForm } from 'element-plus'
import { MessagePrompt } from '@/utils/message'
import { exportToExcel } from '@/utils/export'
import * as echarts from 'echarts'
import type { Table } from '@/types'
import * as financialApi from '@/api/financialRecord'
import type { FinancialRecord } from '@/api/financialRecord'
import { useLoading } from '@/composables/useLoading'
import { useLoginStore } from '@/store/login'

const loginStore = useLoginStore()
const { loading, startLoading, stopLoading } = useLoading(500)
const allData = ref<any[]>([])
const data = ref<any[]>([])
const current = ref(1)
const pageSize = ref(10)
const total = ref(0)
const reportType = ref('day')
const dateValue = ref('')
const revenueChartRef = ref<HTMLElement>()
const paymentChartRef = ref<HTMLElement>()

const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref<InstanceType<typeof ElForm>>()
const formData = reactive({
  recordId: null as number | null,
  type: 'income',
  amount: 0,
  paymentMethod: '',
  orderNo: '',
  remark: '',
  operator: '',
})

const rules = {
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  paymentMethod: [
    { required: true, message: '请选择支付方式', trigger: 'change' },
  ],
}

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

const datePickerType = computed(() => {
  const map: Record<string, string> = {
    day: 'date',
    week: 'week',
    month: 'month',
    year: 'year',
  }
  return map[reportType.value] || 'date'
})

const datePickerPlaceholder = computed(() => {
  const map: Record<string, string> = {
    day: '选择日期',
    week: '选择周',
    month: '选择月份',
    year: '选择年份',
  }
  return map[reportType.value] || '选择日期'
})

const tableOptions: Table[] = [
  { label: '记录ID', prop: 'id', align: 'center' },
  { label: '类型', prop: 'type', align: 'center', slot: 'type' },
  { label: '金额', prop: 'amount', align: 'right', slot: 'amount' },
  { label: '支付方式', prop: 'paymentMethod', align: 'center' },
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

const initRevenueChart = () => {
  if (!revenueChartRef.value) return
  const chart = echarts.init(revenueChartRef.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
    },
    yAxis: { type: 'value', axisLabel: { formatter: '¥{value}' } },
    series: [
      {
        name: '收入',
        type: 'line',
        smooth: true,
        data: [1200, 1500, 1800, 1300, 2000, 2500, 2200],
        itemStyle: { color: '#409eff' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.05)' },
          ]),
        },
      },
    ],
  })
}

const initPaymentChart = () => {
  if (!paymentChartRef.value) return
  const chart = echarts.init(paymentChartRef.value)
  chart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', right: 10, top: 'center' },
    series: [
      {
        name: '支付方式',
        type: 'pie',
        radius: ['40%', '70%'],
        data: [
          { value: 45, name: '微信支付', itemStyle: { color: '#67c23a' } },
          { value: 35, name: '支付宝', itemStyle: { color: '#409eff' } },
          { value: 15, name: '现金', itemStyle: { color: '#e6a23c' } },
          { value: 5, name: '其他', itemStyle: { color: '#909399' } },
        ],
      },
    ],
  })
}

const filterData = () => {
  let filtered = [...allData.value]
  if (dateValue.value) {
    // 根据日期类型过滤
  }
  const start = (current.value - 1) * pageSize.value
  const end = start + pageSize.value
  data.value = filtered.slice(start, end)
  total.value = filtered.length
}

const fetchList = async () => {
  startLoading()
  try {
    const result = await financialApi.getFinancialRecordList()
    if (result.code === 200) {
      allData.value = result.data
      filterData()
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
  Object.assign(formData, {
    recordId: null,
    type: 'income',
    amount: 0,
    paymentMethod: '',
    orderNo: '',
    remark: '',
    operator: '',
  })
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  Object.assign(formData, {
    recordId: row.recordId,
    type: row.type,
    amount: row.amount,
    paymentMethod: row.paymentMethod,
    orderNo: row.orderNo || '',
    remark: row.remark || '',
    operator: row.operator || '',
  })
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

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const submitData: FinancialRecord = {
          type: formData.type,
          amount: formData.amount,
          paymentMethod: formData.paymentMethod,
          orderNo: formData.orderNo,
          remark: formData.remark,
          operator: formData.operator,
        }

        let result
        if (isEdit.value) {
          result = await financialApi.updateFinancialRecord(
            formData.recordId!,
            submitData,
          )
        } else {
          result = await financialApi.addFinancialRecord(submitData)
        }

        if (result.code === 200) {
          MessagePrompt(isEdit.value ? '编辑成功' : '新增成功', 'success')
          dialogVisible.value = false
          fetchList()
        } else {
          MessagePrompt(result.message || '操作失败', 'error')
        }
      } catch (error) {
        MessagePrompt('操作失败', 'error')
      } finally {
        submitting.value = false
      }
    }
  })
}

const handleReportTypeChange = () => {
  dateValue.value = ''
}

const handleQuery = () => {
  current.value = 1
  updateCharts()
  filterData()
  MessagePrompt('查询成功', 'success')
}

const updateCharts = () => {
  // 根据选择的日期类型更新图表数据
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
  fetchList()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  fetchList()
}

onMounted(() => {
  fetchList()
  nextTick(() => {
    initRevenueChart()
    initPaymentChart()
  })
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

  .chart-item {
    background: #f5f7fa;
    padding: 20px;
    border-radius: 8px;
  }

  .chart-item h3 {
    margin: 0 0 16px 0;
    font-size: 16px;
    color: #303133;
  }

  .chart-wrapper {
    height: 300px;
  }

  :deep(.el-button) {
    background-color: var(--tabs);
    color: #fff;
    border: var(--tabs);
    transition: all 0.2s ease;
  }

  :deep(.el-button:nth-child(2)) {
    background-color: var(--success);
  }

  :deep(.el-button:nth-child(3)) {
    background-color: #909399;
  }

  :deep(.el-button:hover) {
    filter: brightness(1.1);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}
</style>
