import { useLoginStore } from '@/store/login'
import { PRIMARY_TOKEN } from '@/global/primary_key'
import { MessagePrompt } from '@/utils/message'
import NProgress from 'nprogress'
import type { Router } from 'vue-router'
import cache from '@/utils/cache.ts'
import { addRoutes } from './index'

const { localCache } = cache

export const setupAuth = (router: Router) => {
  router.beforeEach(async (to, from, next) => {
    let title = to.meta.title
    document.title = `${title}` + '-' + '寻思谨倪'
    NProgress.start()
    const token = localCache.getCache(PRIMARY_TOKEN)
    const loginStore = useLoginStore()

    // 1. 未登录处理
    if (to.path !== '/login' && !token) {
      next('/login')
      MessagePrompt('请登录!', 'error')
      return
    }

    // 2. 已登录处理
    if (token) {
      if (to.path === '/login') {
        next('/')
        return
      }
      const user = loginStore.user

      if (!loginStore.menus.length) {
      }

      // dashboard
      if (user == '') {
        loginStore.user = localCache.getCache('username')
      }
      const rdPath = to.redirectedFrom?.path
      const tPath = rdPath || to.path
      const hasTRoute = router.getRoutes().some((route) => route.path === tPath)
      const RecoverRoute = !hasTRoute || (to.path === '/404' && !!rdPath)

      if (RecoverRoute) {
        const menus = localCache.getCache('menus')

        if (menus?.length) {
          loginStore.menus = menus
          addRoutes(menus)
        } else {
          const userId = localCache.getCache('userId')
          if (userId) {
            await loginStore.getMenusInfo(userId)
          }
        }

        const recovered = router
          .getRoutes()
          .some((route) => route.path === tPath)
        if (recovered) {
          next({ path: tPath, replace: true })
          return
        }
      }
    }

    next()
  })

  router.afterEach(() => {
    NProgress.done()
  })
}
