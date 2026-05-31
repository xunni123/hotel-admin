import service from '@/services/index'

// 查询菜单列表
export const getMenus = () => {
  return service.get('/menu/list')
}

// 根据id查询菜单
export const getMenusByUserId = (userId: string | number) => {
  return service.get(`/menu/tree/${userId}`)
}

export const getAllMenus2 = () => {
  return service.get('/menu/tree2')
}

//查找角色菜单树
export const getRoleIdTree = (id: number) => {
  return service.get(`/menu/role/${id}/tree`)
}


//add菜单
export const addMenu = (data: any) => {
  return service.post('/menu/add', data)
}

//update菜单
export const updateMenu = (data: any) => {
  return service.post('/menu/update', data)
}

//delete菜单
export const deleteMenu = (menuId: number) => {
  return service.delete(`/menu/delete/${menuId}`)
}
