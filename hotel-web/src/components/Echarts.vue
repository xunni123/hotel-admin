<template>
  <div ref="chartRef" :style="{ width, height }"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import type { EChartsOption } from 'echarts'


import echarts from '@/plugins/echart'

interface Props {
  width?: string
  height?: string
  option: EChartsOption
  theme?: string | object
}
const props = withDefaults(defineProps<Props>(), {
  width: '100%',
  height: '400px',
})

const chartRef = ref<HTMLElement>()

let chartInstance: ReturnType<typeof echarts.init> | null = null

// init
const initChart = () => {
  if (!chartRef.value) return
  chartInstance = echarts.init(chartRef.value)
  chartInstance?.setOption(props.option)
}

// resize
const handleResize = () => {
  chartInstance?.resize()
}

// 更新图表
watch(
  () => props.option,
  () => {
    chartInstance?.setOption(props.option)
  },
  { deep: true },
)

onMounted(() => {
  nextTick(() => {
    initChart()
    window.addEventListener('resize', handleResize)
  })
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  chartInstance?.dispose()
})
</script>

<style scoped></style>
