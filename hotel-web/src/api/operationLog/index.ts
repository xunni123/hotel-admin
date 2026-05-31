import service from '@/services'

export interface OperationLog {
  logId?: number
  type: string
  module: string
  content: string
  operator: string
  operatorId?: number
  ip?: string
  params?: string
  result?: string
  createTime?: string
}

export interface LogQuery {
  keyword?: string
  module?: string
  type?: string
}

export const getOperationLogList = (params?: LogQuery): Promise<{
  code: number
  message: string
  data: OperationLog[]
}> => {
  return service.get('/operation-log/list', { params })
}

export const getOperationLogById = (id: number): Promise<{
  code: number
  message: string
  data: OperationLog
}> => {
  return service.get(`/operation-log/${id}`)
}
