import service from '@/services'

export interface Goods {
  goodsId?: number
  goodsCode: string
  goodsName: string
  category: string
  price: number
  stock: number
  description?: string
  status?: string
  createTime?: string
  updateTime?: string
}

export interface GoodsQuery {
  keyword?: string
  category?: string
}

// 获取商品列表
export const getGoodsList = (params?: GoodsQuery): Promise<{
  code: number
  message: string
  data: Goods[]
}> => {
  return service.get('/goods/list', { params })
}

// 获取单个商品
export const getGoodsById = (id: number): Promise<{
  code: number
  message: string
  data: Goods
}> => {
  return service.get(`/goods/${id}`)
}

// 新增商品
export const addGoods = (data: Partial<Goods>): Promise<{
  code: number
  message: string
  data: Goods
}> => {
  return service.post('/goods', data)
}

// 更新商品
export const updateGoods = (id: number, data: Partial<Goods>): Promise<{
  code: number
  message: string
  data: Goods
}> => {
  return service.put(`/goods/${id}`, data)
}

// 删除商品
export const deleteGoods = (id: number): Promise<{
  code: number
  message: string
}> => {
  return service.delete(`/goods/${id}`)
}

// 切换商品状态
export const toggleGoodsStatus = (id: number): Promise<{
  code: number
  message: string
  data: Goods
}> => {
  return service.put(`/goods/toggleStatus/${id}`)
}
