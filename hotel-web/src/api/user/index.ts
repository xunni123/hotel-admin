import service from '@/services'
import type { Users } from '@/types/users'

// 所有用户
export const getAllUser = (): Promise<{
  code: number
  message: string
  data: Users[]
  total?: number
}> => {
  return service.get('/user/all')
}

// 按条件查询用户
export const getUserByUsername = (
  params?: any,
): Promise<{
  code: number
  message: string
  data: Users[]
  total?: number
}> => {
  return service.get('/user/list', { params })
}

// 更新用户
export const updateUser = (id: string | number, data?: Users) => {
  return service.post('/user/update', { ...data, userId: id })
}

// 删除用户
export const deleteUser = (userId: number) => {
  return service.delete(`/user/delete/${userId}`)
}

// 更新用户密码
export const updatePassword = (userId: number, password: string) => {
  return service.put(`/user/updatePassword/${userId}`, password)
}

//新增用户
export const createUser = (data: any) => {
  return service.post(`/user/add`, data)
}
