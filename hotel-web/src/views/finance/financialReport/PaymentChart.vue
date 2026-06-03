<template>
  <div class="chart-item">
    <h3>支付方式占比</h3>
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
