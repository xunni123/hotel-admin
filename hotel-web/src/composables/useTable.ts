import { ref, reactive } from 'vue'
import { MessagePrompt } from '@/utils/message'
export interface TableOptions {
  fetchList: (query: Record<string, any>) => Promise<any>
  update?: (id: number, data: Record<string, any>) => Promise<any>
  delete?: (id: number) => Promise<any>
}
export interface UseTableOptions {
  cacheKey?: string
  cacheTTL?: number
}
export function useTable(api: TableOptions, options: UseTableOptions = {}) {
  const loading = ref(false)
  const data = ref<any[]>([])
  const current = ref(1)
  const pageSize = ref(10)
  const total = ref(0)
  const selectedRows = ref<any[]>([])
  const cache = reactive<
    Record<
      string,
      {
        data: any[]
        total: number
        timestamp: number
      }
    >
  >({})
  const fetchList = async (query: Record<string, any> = {}) => {
    const cacheKey = options.cacheKey
    const cacheTTL = options.cacheTTL || 5 * 60 * 1000
    if (cacheKey && cache[cacheKey]) {
      const cached = cache[cacheKey]
      if (Date.now() - cached.timestamp < cacheTTL) {
        data.value = cached.data
        total.value = cached.total
        return
      }
    }
    loading.value = true
    try {
      const params = {
        page: query.page || current.value,
        pageSize: query.pageSize || pageSize.value,
        ...query,
      }
      const res = await api.fetchList(params)
      if (res && res.data) {
        data.value = res.data.list || res.data
        total.value = res.data.total || res.data.length
        if (cacheKey) {
          cache[cacheKey] = {
            data: data.value,
            total: total.value,
            timestamp: Date.now(),
          }
        }
      }
    } catch (error) {
      MessagePrompt('获取数据失败', 'error')
    } finally {
      loading.value = false
    }
  }
  const handleSizeChange = (size: number) => {
    pageSize.value = size
    fetchList({ page: 1, pageSize: size })
  }
  const handleCurrentChange = (page: number) => {
    current.value = page
    fetchList({ page, pageSize: pageSize.value })
  }
  const updateItem = async (
    rowIdx: number,
    newRow: Record<string, any>,
    oldRow: Record<string, any>,
    getId: (row: Record<string, any>) => number,
  ) => {
    if (!api.update) return
    loading.value = true
    try {
      const id = getId(newRow)
      const res = await api.update(id, newRow)
      if (res && res.code === 200) {
        data.value[rowIdx] = { ...data.value[rowIdx], ...newRow }
        MessagePrompt('更新成功', 'success')
        if (options.cacheKey) {
          delete cache[options.cacheKey]
        }
      } else {
        MessagePrompt(res?.message || '更新失败', 'error')
        data.value[rowIdx] = { ...data.value[rowIdx], ...oldRow }
      }
    } catch (error) {
      MessagePrompt('更新失败', 'error')
      data.value[rowIdx] = { ...data.value[rowIdx], ...oldRow }
    } finally {
      loading.value = false
    }
  }
  const deleteItem = async (
    row: Record<string, any>,
    getId: (row: Record<string, any>) => number,
  ) => {
    if (!api.delete) return
    loading.value = true
    try {
      const id = getId(row)
      const res = await api.delete(id)
      if (res && res.code === 200) {
        const idx = data.value.findIndex((item: any) => getId(item) === id)
        if (idx > -1) {
          data.value.splice(idx, 1)
          total.value--
        }
        MessagePrompt('删除成功', 'success')
        if (options.cacheKey) {
          delete cache[options.cacheKey]
        }
      } else {
        MessagePrompt(res?.message || '删除失败', 'error')
      }
    } catch (error) {
      MessagePrompt('删除失败', 'error')
    } finally {
      loading.value = false
    }
  }
  const select = (rows: any[]) => {
    selectedRows.value = rows
  }
  const loadCache = () => {
    const cacheKey = options.cacheKey
    if (cacheKey && cache[cacheKey]) {
      data.value = cache[cacheKey].data
      total.value = cache[cacheKey].total
    }
  }
  const clearCache = () => {
    const cacheKey = options.cacheKey
    if (cacheKey && cache[cacheKey]) {
      delete cache[cacheKey]
    }
  }
  return {
    loading,
    data,
    current,
    pageSize,
    total,
    selectedRows,
    handleSizeChange,
    handleCurrentChange,
    fetchList,
    updateItem,
    deleteItem,
    select,
    loadCache,
    clearCache,
  }
}
