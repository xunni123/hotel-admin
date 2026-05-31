import service from '@/services/index'
import type { Login } from '@/types/index'

export const login = (data: Login) => {
  return service.post('/auth/login', data)
}
