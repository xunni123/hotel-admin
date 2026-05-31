import type { EChartsOption } from 'echarts'

export interface Echartsinstance {
  width?: string
  height?: string
  option: EChartsOption
  theme?: string | object
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
