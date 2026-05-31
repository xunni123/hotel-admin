export interface PaginationProps {
  total?: number
  page?: number
  limit?: number
  pageSizes?: number[]
  pagerCount?: number
  layout?: string
  background?: boolean
  autoScroll?: boolean
  hidden?: boolean
}

export interface PaginationEvent {
  (payload: { page: number; pageSize: number }): void
}
