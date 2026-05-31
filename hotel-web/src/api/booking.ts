import service from '@/services'

//所有预订单
export const getAllBooking = (page = 1, pageSize = 10) => {
  return service.get('/booking/list', { params: { page, pageSize } })
}

//by-phone-search
export const getBookingByPhone = (guestPhone: string) => {
  return service.get(`/booking/phone/${guestPhone}`)
}

//update
export const updateBooking = (data: any) => {
  return service.put('/booking/update', data)
}

//add
export const addBooking = (data: any) => {
  return service.post('/booking/add', data)
}
