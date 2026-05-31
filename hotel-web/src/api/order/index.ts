import service from "@/services"

export interface OrderQuery {
  keyword?: string
  status?: string
  startDate?: string
  endDate?: string
}

export const getOrderList = (params?: OrderQuery) => {
  return service.get("/order/list", { params })
}

export const updateOrder = (orderId: number, data: any) => {
  return service.put(`/order/update/${orderId}`, data)
}

export const addOrder = (data: any) => {
  return service.post("/order/add", data)
}

export const deleteOrder = (orderId: number) => {
  return service.delete(`/order/${orderId}`)
}