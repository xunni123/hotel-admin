import axios from 'axios'
import { TIME_OUT } from './config/index'
import { PRIMARY_TOKEN } from '@/global/primary_key'
import { MessagePrompt } from '@/utils/message'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

import cache from '@/utils/cache.ts'
import { getServerUrl } from '@/config/index'

const { localCache } = cache

NProgress.configure({ showSpinner: false })
let requestCount = 0

const service = axios.create({
  baseURL: '/api',
  timeout: TIME_OUT,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const updateBaseURL = () => {
  service.defaults.baseURL = getServerUrl()
}

service.interceptors.request.use(
  (config) => {
    if (!config.baseURL) {
      config.baseURL = getServerUrl()
    }
    
    const token = localCache.getCache(PRIMARY_TOKEN)
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    if (requestCount === 0) {
      NProgress.start()
    }
    requestCount++
    return config
  },
  (error) => {
    requestCount--
    if (requestCount === 0) NProgress.done()
    return Promise.reject(error)
  },
)

service.interceptors.response.use(
  (response) => {
    const res = response.data
    requestCount--
    if (requestCount === 0) NProgress.done()
    
    if (res.code !== 200) {
      const errorMessages: Record<number, string> = {
        400: '请求参数错误',
        401: '登录已失效，请重新登录',
        403: '权限不足',
        404: '请求资源不存在',
        500: '服务器内部错误',
      }
      
      const message = res.message || errorMessages[res.code] || '系统开小差了'
      MessagePrompt(message, 'error')
      
      if (res.code === 401) {
        localCache.deleteCache(PRIMARY_TOKEN)
        setTimeout(() => {
          window.location.href = '/login'
        }, 1500)
      }
    }
    return res
  },
  (error) => {
    requestCount--
    if (requestCount === 0) NProgress.done()
    
    if (error.response) {
      const status = error.response.status
      const errorMessages: Record<number, string> = {
        400: '请求参数错误',
        401: '登录已失效，请重新登录',
        403: '权限不足',
        404: '请求资源不存在',
        408: '请求超时',
        500: '服务器内部错误',
        502: '网关错误',
        503: '服务不可用',
        504: '网关超时',
      }
      
      const message = errorMessages[status] || `网络错误 [${status}]`
      MessagePrompt(message, 'error')
      
      if (status === 401) {
        localCache.deleteCache(PRIMARY_TOKEN)
        setTimeout(() => {
          window.location.href = '/login'
        }, 1500)
      }
    } else if (error.request) {
      MessagePrompt('请求超时，请检查网络连接', 'error')
    } else {
      MessagePrompt('请求配置错误', 'error')
    }
    
    return Promise.reject(error)
  },
)

export default service
