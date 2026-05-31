export interface ColorMap {
  todayCheckIns: string
  todayCheckOuts: string
  emptyRooms: string
  occupancyRate: string
  todayRevenue: string
}

export interface Stats {
  emptyRooms?: number
  occupancyRate?: number
  todayCheckIns?: number
  todayCheckOuts?: number
  todayRevenue?: number
  totalRooms?: number
}
