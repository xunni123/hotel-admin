import service from '@/services'
import type { FiltersParam } from '@/types'

export const getMenuContent = (param: FiltersParam) => {
  const flatParams = {
    searchText: param.searchText,
    building: param.location.building,
    floor: param.location.floor,
    roomType: param.location.roomType,
    features: param.location.features,
    idle: param.roomStatus.idle,
    dirty: param.roomStatus.dirty,
    repair: param.roomStatus.repair,
    booked: param.roomStatus.booked,
    checkedIn: param.roomStatus.checkedIn,
    locked: param.roomStatus.locked,
    selfUse: param.roomStatus.selfUse,
    todayCheckout: param.roomStatus.todayCheckout,
    checkinType: param.order.checkinType,
    channel: param.order.channel,
    specialTags: param.order.specialTags,
  }
  return service.get('/room/list', { params: { ...flatParams, pageSize: 999 } })
}

// get all房间
export const getAllMenu = () => {
  return service.get('/room/all')
}

// get 楼栋列表
export const getBuilding = () => {
  return service.get('/room/building')
}

//get 楼层列表
export const getFloor = () => {
  return service.get('/room/floor')
}

//get 房型
export const getRoomType = () => {
  return service.get('/room/type')
}

//get 房间状态
export const getRoomStatus = () => {
  return service.get('/room/status')
}

//get 房间入住类型
export const getCheckinType = () => {
  return service.get('/room/checkinType')
}

//get 渠道

export const getChannel = () => {
  return service.get('/room/channel')
}


//get 未入住
export const getFreeRoom=()=>{
  return service.get("/room/free")
}

// get 已入住列表
export const getCheckedInList=()=>{
  return service.get("/room/checkedIn")
}

// update 入住信息
export const updateCheckinInfo=(data:any)=>{
  return service.put("/room/updateCheckin",data)
}

// checkout 退房
export const checkoutRoom=(data:any)=>{
  return service.put("/room/checkout",data)
}

