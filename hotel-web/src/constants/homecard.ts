import type { ColorMap } from '@/types/dashboardCard'

const colorMap: ColorMap = {
  todayCheckIns: '#3B82F6',
  todayCheckOuts: '#10B981',
  emptyRooms: '#F59E0B',
  occupancyRate: '#8B5CF6',
  todayRevenue: '#EF4444',
}

const cardDataList = [
  { key: 'todayCheckIns', label: '今日入住', unit: '间' },
  { key: 'todayCheckOuts', label: '今日退房', unit: '间' },
  { key: 'emptyRooms', label: '空房数', unit: '间' },
  { key: 'occupancyRate', label: '入住率', unit: '%' },
  { key: 'todayRevenue', label: '今日营收', unit: '元' },
]

export { colorMap, cardDataList }
