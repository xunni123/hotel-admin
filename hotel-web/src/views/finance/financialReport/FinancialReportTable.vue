<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>财务报表</h2>
        <div class="query-form">
          <el-radio-group v-model="reportType" @change="handleReportTypeChange">
            <el-radio-button label="day">日报</el-radio-button>
            <el-radio-button label="week">周报</el-radio-button>
            <el-radio-button label="month">月报</el-radio-button>
            <el-radio-button label="year">年报</el-radio-button>
          </el-radio-group>
          <el-date-picker
            v-model="dateValue"
            :type="datePickerType"
            :placeholder="datePickerPlaceholder"
            value-format="YYYY-MM-DD"
            style="width: 200px"
          />
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleExport">导出</el-button>
        </div>
      </div>

      <!-- 统计卡片 -->
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

      <!-- 图表区域 -->
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

      <!-- 明细表格 -->
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
import { ref, onMounted, nextTick, computed } from 'vue'
import MyTable from '@/components/MyTable.vue'
import Pagination from '@/components/Pagination.vue'
import Card from '@/components/Card.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { MessagePrompt } from '@/utils/message'
import { exportToExcel } from '@/utils/export'
import * as echarts from 'echarts'
import type { Table } from '@/types'
import * as financialApi from '@/api/financialRecord'
import type { FinancialRecord } from '@/api/financialRecord'

const loading = ref(false)
const allData = ref<any[]>([])
const data = ref<any[]>([])
const current = ref(1)
const pageSize = ref(10)
const total = ref(0)
const reportType = ref('day')
const dateValue = ref('')
const revenueChartRef = ref<HTMLElement>()
const paymentChartRef = ref<HTMLElement>()

// 导出列定义
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

// 统计数据
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
]

const initRevenueChart = () => {
  if (!revenueChartRef.value) return

  const chart = echarts.init(revenueChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis',
    },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '¥{value}',
      },
    },
    series: [
      {
        name: '收入',
        type: 'line',
        data: [1200, 1500, 1800, 1300, 2000, 2500, 2200],
        smooth: true,
        itemStyle: { color: '#409eff' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.05)' },
          ]),
        },
      },
    ],
  }
  chart.setOption(option)
}

const initPaymentChart = () => {
  if (!paymentChartRef.value) return

  const chart = echarts.init(paymentChartRef.value)
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)',
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center',
    },
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
  }
  chart.setOption(option)
}

// 过滤数据
const filterData = () => {
  let filtered = [...allData.value]

  // 按日期类型过滤
  if (dateValue.value) {
    filtered = filtered.filter((item) => {
      // 根据日期类型进行过滤
      return true // 实际应该根据日期进行过滤
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
    const result = await financialApi.getFinancialRecordList()
    if (result.code === 200) {
      // 将类型转换为中文
      allData.value = result.data.map((item) => ({
        ...item,
        type: item.type === 'income' ? '收入' : '支出',
        paymentMethod: getPaymentMethodText(item.paymentMethod),
      }))
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
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const getPaymentMethodText = (method: string) => {
  const map: Record<string, string> = {
    wechat: '微信支付',
    alipay: '支付宝',
    cash: '现金',
    bank: '银行卡',
  }
  return map[method] || method
}

const handleReportTypeChange = () => {
  dateValue.value = ''
}

const handleQuery = () => {
  current.value = 1
  // 根据报表类型和日期更新图表
  updateCharts()
  filterData()
  MessagePrompt('查询成功', 'success')
}

const updateCharts = () => {
  // 根据选择的日期类型更新图表数据
  // 这里可以调用API获取不同时间段的数据
}

const handleReset = () => {
  reportType.value = 'day'
  dateValue.value = ''
  current.value = 1
  // 重置图表
  fetchList()
  MessagePrompt('已重置', 'success')
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
      // 获取所有数据
      const exportData = [...allData.value]

      exportToExcel(exportData, exportColumns, '财务报表')
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
    background-color: #909399;
  }

  :deep(.el-button:hover) {
    filter: brightness(1.1);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}
</style>
