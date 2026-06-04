import type { Booking } from '@/types/booking'

export const getInitBookingForm = (): Booking => ({
  orderNo: '',
  roomId: '',
  roomNumber: '',
  guestName: '',
  guestPhone: '',
  idCard: '',
  checkInDate: '',
  checkOutDate: '',
  nights: 0,
  adults: 1,
  children: 0,
  totalAmount: 0,
  paidAmount: 0,
  orderStatus: '',
  paymentStatus: '',
  paymentMethod: '',
  bookChannel: '',
  checkInType: '',
  customerType: '',
  remarks: '',
})
