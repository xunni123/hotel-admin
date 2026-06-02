export interface Table {
  prop: string
  label: string
  type?: string
  width?: string | number
  minWidth?: string | number
  align?: 'left' | 'center' | 'right'
  fixed?: boolean | 'left' | 'right'
  showOverflowTooltip?: boolean
  editable?: boolean
  slot?: string
  actions?: boolean
}

export interface TableApi<T> {
  fetchList?: (
    params?: Record<string, any>,
  ) => Promise<{ data: T[]; total?: number; code?: number; message?: string }>
  create?: (data: any) => Promise<any>
  select?: (roleName: string) => Promise<any>
  update?: (id: string | number, data: T) => Promise<any>
  delete?: (id: string | number) => Promise<any>
}

export interface PaginationConfig {
  current: number
  pageSize: number
  total: number
  pageSizes: number[]
}
