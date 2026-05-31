import service from "@/services"

export const getOrderList = () => {
  return service.get("/order/list")
}

export const updateOrder = (orderId: number, data: any) => {
  return service.put(`/order/update/${orderId}`, data)
}

export const addOrder = (data: any) => {
  return service.post("/order/add", data)
}