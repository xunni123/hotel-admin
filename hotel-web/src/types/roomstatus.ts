import type { T } from 'vue-router/dist/router-CWoNjPRp.mjs'

export interface FiltersParam {
  searchText: string
  location: LocationParam
  roomStatus: RoomStatusParam
  order: OrderParam
}

export interface LocationParam {
  building: string
  floor: string
  roomType: string
  features: Array<T>
}

export interface RoomStatusParam {
  idle: Boolean
  dirty: Boolean
  repair: Boolean
  booked: Boolean
  checkedIn: Boolean
  locked: Boolean
  selfUse: Boolean
  todayCheckout: Boolean
}

export interface OrderParam {
  checkinType: string
  channel: string
  specialTags: Array<T>
}

export interface RoomStatus {
  idle: boolean // 空闲
  dirty: boolean // 脏房
  repair: boolean // 维修
  booked: boolean // 已预订
  checkedIn: boolean // 已入住
  locked: boolean // 锁房
  selfUse: boolean //自用
  todayCheckout: boolean // 今日预离
}

export interface Rooms {
  roomId: string
  roomNumber: string
  roomType: string
  status: string
  building: string
  checkInTime: string
  checkOutTime: string
  floor: string
  guestName: string
  price: number
  currentGuests: number
}
