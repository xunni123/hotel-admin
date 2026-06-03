import service from '@/services'
import type { AddRole } from '@/types/addRole'

// 添加
export const addRole = (param: AddRole) => {
  return service.post('/role/add', param)
}


// 角色列表
export const roleList = (params?: any) => {
  return service.get('/role/list', { params })
}

// 更新角色
export const updateRole = (id: number, data?: AddRole) => {
  if (data) {
    return service.post('/role/update', { ...data, roleId: id })
  }
}

// 删除角色
export const deleteRole = (roleId: number) => {
  return service.delete(`/role/delete/${roleId}`)
}

// 获取角色权限
export const getRolePermissions = (roleId: number) => {
  return service.get(`/role/${roleId}/permissions`)
}

// 分配权限
export const assignPermissions = (roleId: number, menuIds: number[]) => {
  return service.post(`/role/${roleId}/permissions`, { menuIds })
}
