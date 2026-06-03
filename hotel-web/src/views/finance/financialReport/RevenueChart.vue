<template>
  <div class="chart-item">
    <h3>收入趋势</h3>
    <div ref="chartRef" class="chart-wrapper"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'

const chartRef = ref<HTMLElement>()

const initChart = () => {
  if (!chartRef.value) return
  const chart = echarts.init(chartRef.value)
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

  window.addEventListener('resize', () => {
    chart.resize()
  })
}

onMounted(() => {
  nextTick(() => {
    initChart()
  })
})
</script>

<style scoped lang="scss">
.chart-item {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 8px;

  h3 {
    margin: 0 0 16px 0;
    font-size: 16px;
    color: #303133;
  }
}

.chart-wrapper {
  height: 300px;
}
</style>
