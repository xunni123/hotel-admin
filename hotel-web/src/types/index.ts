// 分页参数
export interface PageParams {
  page: number
  limit: number
}

// 分页响应
export interface PageResponse<T> {
  code: number
  message: string
  data: {
    list: T[]
    total: number
  }
}

// 基础响应
export interface ApiResponse<T = unknown> {
  code: number
  message: string
  data: T
}

// 表格选项
export interface TableColumn {
  label: string
  prop: string
  width?: string | number
  align?: 'left' | 'center' | 'right'
  slot?: string
  editable?: boolean
  showOverflowTooltip?: boolean
  type?: 'selection' | 'index' | 'expand'
}

// 操作选项
export interface ActionOptions {
  label?: string
  width?: string | number
  align?: 'left' | 'center' | 'right'
}

// 搜索字段配置
export interface SearchField {
  type: 'input' | 'select' | 'date' | 'daterange'
  prop: string
  placeholder: string
  width?: string
  options?: { label: string; value: string | number }[]
}

// 排序配置
export interface SortConfig {
  field: string
  order: 'asc' | 'desc'
}

// 用户信息
export interface UserInfo {
  userId: number
  username: string
  realName: string
  phone: string
  email?: string
  avatar?: string
  roleIds: number[]
  roleNames: string[]
}

// 时间范围
export interface DateRange {
  start: string
  end: string
}

// 对话框配置
export interface DialogModel {
  title?: string
  width?: string | number
  height?: string | number
  visible?: boolean
}


