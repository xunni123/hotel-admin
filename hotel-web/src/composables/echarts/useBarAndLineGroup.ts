import { da } from 'element-plus/es/locale/index.mjs'
import { ref, computed, isRef } from 'vue'
import type { BarAndLineGroupOptions,CharDataOptions } from '@/types/echarts'



// 柱形and折线配置
export const useBarAndLineGroup: Function = (
  options: BarAndLineGroupOptions,
) => {
  const {
    title = '近7天营收趋势',
    tooltip = { trigger: 'axis' },
    legend = {
      data: ['每日总营收（元）', 'RevPAR（元）'],
      top: '0',
    },
  } = options

  const chartData = ref<CharDataOptions>({
    dates: [],
    revenue: [],
    revpar: [],
  })

  // 配置项
  const chartOption = computed(() => {
    const dataList = chartData.value
    const { dates, revenue, revpar }: CharDataOptions = dataList
    return {
      title: { text: title, left: 'center', top: '0' },
      tooltip: tooltip,
      legend: legend,
      xAxis: { type: 'category', data: dates },
      yAxis: [
        {
          type: 'value',
          name: '营收（元）',
          nameLocation: 'middle',
          nameGap: 50,
          axisLabel: { formatter: '{value}' },
        },
        {
          type: 'value',
          name: 'RevPAR（元）',
          nameLocation: 'middle',
          nameGap: 50,
          axisLabel: { formatter: '{value}' },
          splitLine: { show: false }, //  y 轴不显示分割线
        },
      ],
      series: [
        {
          name: '每日总营收（元）',
          type: 'bar',
          data: revenue,
          barWidth: '40%',
          itemStyle: { color: '#5470c6', borderRadius: [4, 4, 0, 0] },
          yAxisIndex: 0,
        },
        {
          name: 'RevPAR（元）',
          type: 'line',
          data: revpar,
          smooth: true,
          lineStyle: { width: 3, color: '#fac858' },
          symbol: 'circle',
          symbolSize: 8,
          yAxisIndex: 1,
        },
      ],
    }
  })

  const setData = (data) => {
    chartData.value = data
  }
  return {
    chartOption,
    setData,
  }
}
