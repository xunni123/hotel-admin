import service from '@/services'

export interface MemberConsume {
  consumeId?: number
  memberId?: number
  memberNo: string
  memberName: string
  phone?: string
  type: string
  amount: number
  pointsChange: number
  currentPoints: number
  remark?: string
  operator?: string
  createTime?: string
}

export interface ConsumeQuery {
  keyword?: string
  type?: string
}

export const getConsumeList = (params?: ConsumeQuery): Promise<{
  code: number
  message: string
  data: MemberConsume[]
}> => {
  return service.get('/member-consume/list', { params })
}

export const getConsumeById = (id: number): Promise<{
  code: number
  message: string
  data: MemberConsume
}> => {
  return service.get(`/member-consume/${id}`)
}

export const addConsume = (data: MemberConsume): Promise<{
  code: number
  message: string
  data: MemberConsume
}> => {
  return service.post('/member-consume', data)
}

export const updateConsume = (id: number, data: MemberConsume): Promise<{
  code: number
  message: string
  data: MemberConsume
}> => {
  return service.put(`/member-consume/${id}`, data)
}

export const deleteConsume = (id: number): Promise<{
  code: number
  message: string
  data: any
}> => {
  return service.delete(`/member-consume/${id}`)
}

export const getConsumeStatistics = (): Promise<{
  code: number
  message: string
  data: {
    totalAmount: number
    totalCount: number
    roomAmount: number
    goodsAmount: number
  }
}> => {
  return service.get('/member-consume/statistics')
}
