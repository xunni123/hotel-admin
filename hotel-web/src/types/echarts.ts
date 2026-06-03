import type { EChartsOption } from 'echarts'

export interface Echartsinstance {
  width?: string
  height?: string
  option: EChartsOption
  theme?: string | object
}

// 饼图
export interface PieChartOptions {
  title?: string
  valueKey?: string
  nameKey?: string
  colors?: string[]
  titlePosition?: {
    left: 'center' | 'left' | 'right' | number | string
    top: 'center' | 'top' | 'bottom' | number | string
  }
  pieRadius?: [string, string] | string[]
  pieCenter?: [string, string] | string[]
}

//柱型and折线
export interface BarAndLineGroupOptions {
  title?: string
  tooltip?: {
    trigger: string
  }
  legend?: {
    data: string[]
    top: string
  }
}

//柱型and折线数据配置项
export interface CharDataOptions {
  dates: string[]
  revenue: number[]
  revpar: number[]
}

// 入住率
export interface CheckInRates {
  date: string
  occupancyRate: number
}

export interface CheckIn {
  halfYear: CheckInRates[]
  month: CheckInRates[]
  week: CheckInRates[]
}

// 客源分布
export interface RegionItem {
  avgOrderAmount: number
  orderCount: number
  orderPercent: number
  region: string
  revenue: number
  revenuePercent: number
}

export type Region = RegionItem[]
