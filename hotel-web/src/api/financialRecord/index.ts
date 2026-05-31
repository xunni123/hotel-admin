import service from '@/services'

export interface FinancialRecord {
  recordId?: number
  type: string
  amount: number
  paymentMethod: string
  orderNo?: string
  remark?: string
  operator?: string
  createTime?: string
}

export interface RecordQuery {
  type?: string
}

export const getFinancialRecordList = (params?: RecordQuery): Promise<{
  code: number
  message: string
  data: FinancialRecord[]
}> => {
  return service.get('/financial-record/list', { params })
}

export const getFinancialSummary = (): Promise<{
  code: number
  message: string
  data: {
    income: number
    expense: number
    profit: number
    orderCount: number
    paymentStats: Record<string, number>
  }
}> => {
  return service.get('/financial-record/summary')
}
