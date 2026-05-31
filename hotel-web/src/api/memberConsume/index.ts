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
