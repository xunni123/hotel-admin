import { ref, computed, isRef } from 'vue'
import type { CheckInRates, CheckIn } from '@/types'

// 折线
export function useLineChart(options = {}) {
  const {
    title = '趋势图',
    xAxisName = '日期',
    yAxisName = '数值',
    defaultRange = 'week',
    dataAdapter = (data: CheckIn, range: keyof CheckIn) => ({
      dates: data[range]?.map((item: any) => item.date) || [],
      values: data[range]?.map((item: any) => item.value) || [],
    }),
  } = options

  const titleText = computed(() => {
    if (typeof title === 'function') {
      return title(selectedRange.value, chartData.value)
    }
    if (isRef(title)) {
      return title.value
    }
    return title
  })

  const chartData = ref({})
  const selectedRange = ref(defaultRange)

  // 配置项
  const chartOption = computed(() => {
    const { dates, values } = dataAdapter(chartData.value, selectedRange.value)
    return {
      title: { text: titleText.value },
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', name: xAxisName, data: dates },
      yAxis: { type: 'value', name: yAxisName },
      series: [
        {
          type: 'line',
          data: values,
          smooth: true,
          areaStyle: {
            color: 'rgba(20, 108, 196, 0.2)',
          },
        },
      ],
    }
  })

  // 存取数据
  const setData = (data: CheckIn) => {
    chartData.value = data
  }

  // 存取当前时间数据
  const setRange = (range: string) => {
    selectedRange.value = range
  }

  return { chartOption, setData, setRange, selectedRange }
}
