export interface Booking {
  orderNo: string
  roomId: string
  roomNumber: string
  guestName: string
  guestPhone: string
  idCard: string
  checkInDate: string
  checkOutDate: string
  nights: number
  adults: number
  children: number
  totalAmount: number
  paidAmount: number
  orderStatus: string
  paymentStatus: string
  paymentMethod: string
  bookChannel: string
  checkInType: string
  customerType: string
  remarks: string
}
