<template>
  <div class="home-echarts-container">
    <template v-if="loading">
      <Card v-for="i in 5" :key="i" class="card-item" :width="'180px'">
        <MySkeleton mode="card" :rows="2" />
      </Card>
    </template>
    <template v-else>
      <div class="home-echarts-header">
        <h2>入住率趋势</h2>
        <div class="home-echarts-container-btns">
          <el-button
            :class="{ 'active-btn': selectedRange === 'week' }"
            round
            @click="setRange('week')"
            >近7天</el-button
          >
          <el-button
            :class="{ 'active-btn': selectedRange === 'month' }"
            round
            @click="setRange('month')"
            >近一个月</el-button
          >
          <el-button
            :class="{ 'active-btn': selectedRange === 'halfYear' }"
            round
            @click="setRange('halfYear')"
            >近一年</el-button
          >
        </div>
      </div>
      <div class="home-echarts-content">
        <Echarts :option="lineOptions" :height="'300px'" />
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import type { CheckIn } from '@/types/echarts'
import { useLineChart } from '@/composables/echarts/useLineChart'
import { fetchOccupancy } from '@/api/dashboard'
import { onMounted } from 'vue'
import Card from '@/components/Card.vue'
import MySkeleton from '@/components/MySkeleton.vue'
import { useLoading } from '@/composables/useLoading'

const { loading, startLoading, stopLoading } = useLoading(800)

onMounted(async () => {
  startLoading()
  try {
    const res = await fetchOccupancy()
    setData(res.data)
  } catch {
  } finally {
    stopLoading()
  }
})

const getRangeText = (range: string) => {
  switch (range) {
    case 'week':
      return '近7天'
    case 'month':
      return '近一个月'
    default:
      return '近一年'
  }
}

// 入住率配置引用
const {
  chartOption: lineOptions,
  setData,
  setRange,
  selectedRange,
} = useLineChart({
  title: (range: string) => `入住率趋势图（${getRangeText(range)}）`,
  xAxisName: '日期',
  yAxisName: '数值',
  defaultRange: 'week',
  dataAdapter: (data: CheckIn, range: keyof CheckIn) => ({
    dates: data[range]?.map((item) => item.date) || [],
    values: data[range]?.map((item) => item.occupancyRate) || [],
  }),
})
</script>

<style lang="scss" scoped>
.home-echarts-container {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  .home-echarts-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h3 {
      margin: 0;
      font-size: 16px;
      font-weight: bold;
      color: #333;
    }
  }

  .home-echarts-container-btns {
    display: flex;
    gap: 10px;
    align-items: center;

    :deep(.el-button) {
      width: 100px;
      transition: all 0.3s;
    }

    // 激活按钮样式
    :deep(.active-btn) {
      background-color: var(--card);
      border-color: var(--card);
      color: white;

      &:hover {
        background-color: var(--card);
        border-color: var(--card);
      }
    }
  }

  .home-echarts-content {
    width: 100%;
  }
}
</style>
