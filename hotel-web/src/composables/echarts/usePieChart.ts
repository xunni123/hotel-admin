import { ref, computed, isRef } from 'vue'
import type { RegionItem, Region } from '@/types'

// 饼图
export function usePieCharts<T extends Record<string, any> = RegionItem>(
  options = {},
) {
  const {
    title = '客源分布图',
    valueKey = 'orderCount',
    nameKey = 'region',
    colors = [
      '#ee6666',
      '#5470c6',
      '#fac858',
      '#73c0de',
      '#3ba272',
      '#fc8452',
      '#9a60b4',
      '#ea7ccc',
    ],
    titlePosition = { left: 'center', top: '0' },
    pieRadius = ['60%', '80%'],
    pieCenter = ['50%', '50%'],
  } = options

  const transformData = (data: Region[]) => {
    if (!Array.isArray(data)) return []
    return data.map((item) => ({
      name: item[nameKey],
      value: item[valueKey],
      ...item,
    }))
  }

  const chartData = ref()

  // 配置项
  const chartOption = computed(() => {
    const datas = transformData(chartData.value)
    return {
      title: {
        text: title,
        left: titlePosition.left,
        top: titlePosition.top,
      },
      color: colors,
      tooltip: {
        trigger: 'item',

        formatter: (params: any) => {
          const data = params.data
          return `
            订单数量:${data.orderCount},
            收入:${data.revenue?.toFixed(2) || 0}
            `
        },
      },
      legend: {
        bottom: '0',
        left: 'center',
        orient: 'horizontal',
        data: datas.map((item) => item.name),
      },
      series: [
        {
          type: 'pie',
          radius: pieRadius,
          center: pieCenter,
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center',
            formatter: '{b}: {d}%',
          },
          emphasis: {
            label: {
              show: true,
              color: '#000',
              fontSize: 20,
              fontWeight: 'bold',
              align: 'center',
            },
          },
          labelLine: {
            show: false,
          },
          data: datas,
        },
      ],
    }
  })

  // save data
  const setData = (data: Region) => {
    chartData.value = data
  }

  return { chartOption, setData }
}
