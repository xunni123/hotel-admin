import { ref, onUnmounted } from 'vue'
import type { Ref } from 'vue'
import type { TableApi } from '@/types/table'
import { MessagePrompt } from '@/utils/message'
import { updateRole, deleteRole } from '@/api/role'
import { ElMessageBox } from 'element-plus'
import cache from '@/utils/cache'
import { useLoading } from '@/composables/useLoading'

const { sessionCache } = cache

const defaultPageSize = 5

interface UseTableOptions {
  cacheKey?: string
  cachePrefix?: string
}

//role
export const useRole = () => {
  const addRef = ref<{ show: () => void }>()

  const addRole = () => {
    addRef.value?.show()
  }

  return {
    addRef,
    addRole,
  }
}

//table
export const useTable = <T>(
  api: TableApi<T>,
  options: UseTableOptions = {},
) => {
  const data = ref<T[]>([]) as Ref<T[]>
  const { loading, startLoading, stopLoading } = useLoading(500)
  const queryParams = ref<Record<string, any>>({})

  const current = ref(1)
  const pageSize = ref(defaultPageSize)
  const total = ref(0)

  // 缓存键name
  const cacheKey =
    options.cacheKey ||
    `t_${api.fetchList?.name?.replace('fetch', '')?.replace('List', '') || 'default'}`

  // 缓存
  const saveCache = () => {
    const cacheData = {
      data: data.value,
      current: current.value,
      pageSize: pageSize.value,
      total: total.value,
      queryParams: queryParams.value,
      timestamp: Date.now(),
    }
    sessionCache.setCache(cacheKey, cacheData)
  }

  // 加载
  const loadCache = (): boolean => {
    const cached = sessionCache.getCache(cacheKey)
    if (cached) {
      data.value = cached.data || []
      current.value = cached.current || 1
      pageSize.value = cached.pageSize || defaultPageSize
      total.value = cached.total || 0
      queryParams.value = cached.queryParams || {}
      return true
    }
    return false
  }

  // clear
  const clearCache = () => {
    sessionCache.removeCache(cacheKey)
  }

  // 当前页码改变
  const handleCurrentChange = (val: number) => {
    current.value = val
    fetchList()
  }

  // 监听页码
  const handleSizeChange = (val: number) => {
    pageSize.value = val
    current.value = 1
    fetchList()
  }

  // 重置页
  const resetPagination = () => {
    current.value = 1
    pageSize.value = defaultPageSize
  }

  // 获取
  const fetchList = async (params?: Record<string, any>) => {
    startLoading()

    try {
      const query: Record<string, any> = {
        ...(params || queryParams.value),
        page: current.value,
        pageSize: pageSize.value,
      }
      const res = await api.fetchList(query)
      if (res.code === undefined || res.code === 200) {
        // 处理两种返回格式：{ data: [...] } 或 { data: { list: [...], total: N } }
        if (res.data && res.data.list !== undefined) {
          // 后端分页格式
          data.value = res.data.list || []
          total.value = res.data.total || 0
        } else {
          // 旧格式或不分页格式
          data.value = res.data || []
          if (res.total !== undefined) {
            total.value = res.total
          }
        }

        saveCache()
      } else {
        MessagePrompt(res.message || '获取失败', 'error')
      }
    } catch (error) {
      MessagePrompt('网络错误', 'error')
    } finally {
      stopLoading()
    }
  }

  // 查
  const select = async (query: any) => {
    const hasValue = Object.values(query).some(
      (val) => val !== null && val !== undefined && val !== '',
    )
    if (!hasValue) {
      MessagePrompt('请填写查询条件', 'warning')
      return false
    }
    queryParams.value = query
    current.value = 1
    try {
      const res = await api.select(query)
      if (res.code === 200) {
        MessagePrompt('查询成功', 'success')
        saveCache()
        return true
      } else {
        MessagePrompt('查询失败', 'error')
        return false
      }
    } catch (error) {
      MessagePrompt('查询失败', 'error')
      return false
    }
  }

  // 更新
  const updateItem = async (
    rowIdx: number,
    newRow: T,
    oldRow: T,
    getId: (row: T) => string | number,
  ) => {
    if (!api.update) {
      MessagePrompt('未配置接口', 'error')
      return false
    }
    const id = getId(newRow)

    if (id === undefined || id === null) {
      MessagePrompt('无法获取记录ID', 'error')
      return false
    }

    try {
      const res = await api.update(id, newRow)
      if (res.code === undefined || res.code === 200) {
        MessagePrompt('更新成功', 'success')
        data.value[rowIdx] = newRow
        saveCache()
        return true
      } else {
        MessagePrompt('更新失败', 'error')
        data.value[rowIdx] = oldRow
        return false
      }
    } catch (error) {
      MessagePrompt('更新失败,请重试', 'error')
      data.value[rowIdx] = oldRow
      return false
    }
  }

  // 删除
  const deleteItem = async (
    row: T,
    getId: (row: T) => string | number,
    getName: (row: T) => string,
  ) => {
    const id = getId(row)
    if (!id) {
      MessagePrompt('无法获取记录ID', 'error')
      return
    }
    const name = getName(row)
    await ElMessageBox.confirm(`确认删除"${name}"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
      .then(async () => {
        try {
          const res = await api.delete(id)
          if (res.code === undefined || res.code === 200) {
            MessagePrompt('删除成功', 'success')
            await fetchList()
          } else {
            MessagePrompt(res.message || '删除失败', 'error')
          }
        } catch (error) {
          MessagePrompt('删除失败，请重试', 'error')
        }
      })
      .catch(() => {})
  }

  // 增
  const createItem = async (data: T) => {
    if (!api.create) {
      MessagePrompt('未配置接口', 'error')
      return false
    }

    try {
      const res = await api.create(data)
      const msg = res.data

      if (res.code === undefined || res.code === 200) {
        MessagePrompt(msg, 'success')
        saveCache()
        return true
      } else {
        MessagePrompt(msg, 'error')
      }
    } catch (err) {}
  }

  return {
    data,
    loading,
    startLoading,
    stopLoading,
    queryParams,
    current,
    pageSize,
    total,
    fetchList,
    select,
    updateItem,
    deleteItem,
    createItem,
    handleCurrentChange,
    handleSizeChange,
    resetPagination,
    loadCache,
    clearCache,
  }
}
