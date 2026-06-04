import axios from 'axios'
import { TIME_OUT } from './config/index'
import { PRIMARY_TOKEN } from '@/global/primary_key'
import { MessagePrompt } from '@/utils/message'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import cache from '@/utils/cache.ts'
import { getServerUrl } from '@/config/index'

const { localCache } = cache

// 配置
NProgress.configure({ showSpinner: false })

// 请求次数
let requestCount = 0


const service = axios.create({
  baseURL: '/api',
  timeout: TIME_OUT,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 动态更新 baseURL
export const updateBaseURL = () => {
  service.defaults.baseURL = getServerUrl()
}

// 启动加载
const startLoading = () => {
  if (requestCount === 0) NProgress.start()
  requestCount++
}

// 关闭加载-防止负数
const finishLoading = () => {
  requestCount--
  if (requestCount <= 0) {
    requestCount = 0
    NProgress.done()
  }
}

// 统一错误消息
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

// 401 登录跳转锁（防止多请求重复跳转）
let isRedirecting = false

// 统一处理 401
const handle401 = () => {
  if (isRedirecting) return
  isRedirecting = true
  localCache.removeCache(PRIMARY_TOKEN)
  MessagePrompt('登录已失效，请重新登录', 'error')
  setTimeout(() => {
    window.location.href = '/login'
    isRedirecting = false
  }, 1000)
}

// 请求拦截器 
service.interceptors.request.use(
  (config) => {
    // 统一设置 baseURL
    config.baseURL = getServerUrl()

    // 携带 token
    const token = localCache.getCache(PRIMARY_TOKEN)
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }

    // 开启加载条
    startLoading()
    return config
  },
  (error) => {
    finishLoading()
    return Promise.reject(error)
  },
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    finishLoading()
    const res = response.data

    // 业务状态码判断
    if (res.code !== 200) {
      const message = res.message || errorMessages[res.code] || '系统开小差了'
      MessagePrompt(message, 'error')

      // 401 未授权
      if (res.code === 401) handle401()
      return Promise.reject(res)
    }

    return res
  },
  (error) => {
    finishLoading()

    // HTTP 状态码错误
    if (error.response) {
      const status = error.response.status
      if (status === 401) {
        handle401()
        return Promise.reject(error)
      }

      const message = errorMessages[status] || `网络错误 [${status}]`
      MessagePrompt(message, 'error')
    }
    // 无响应（网络超时）
    else if (error.request) {
      MessagePrompt('请求超时，请检查网络连接', 'error')
    }
    // 请求配置错误
    else {
      MessagePrompt('请求配置错误', 'error')
    }

    return Promise.reject(error)
  },
)

export default service
