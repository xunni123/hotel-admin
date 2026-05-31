import { defineStore } from 'pinia'
import { getMenusByUserId } from '@/api/menus'
import { login } from '@/api/login'
import type { Menus, Login } from '@/types'
import cache from '@/utils/cache'
const { localCache } = cache
import { PRIMARY_TOKEN } from '@/global/primary_key'
import { showConfirm } from '@/utils/confirm'
import { MessagePrompt } from '@/utils/message'
import { addRoutes } from '@/router/index'
import type { PermissionDTO } from '@/types'

export const useLoginStore = defineStore('login', {
  state: () => ({
    user: '',
    token: '',
    userId: '' as string,
    menus: [] as Menus[],
    role: '',
    isAdmin: false,
    permissions: {
      userManagement: false,
      roleManagement: false,
      menuManagement: false,
      bookingManagement: false,
      roomManagement: false,
      orderManagement: false,
      memberManagement: false,
      reportManagement: false,
      financialManagement: false,
      canEdit: false,
      canDelete: false,
      canAssignPermission: false,
    } as PermissionDTO,
  }),
  persist: true,
  getters: {
    hasPermission: (state) => {
      return state.isAdmin || state.role === 'admin' || state.role === '管理员'
    },
  },
  actions: {
    async getMenusInfo(id: string) {
      const res = await getMenusByUserId(id)
      this.menus = res.data

      localCache.setCache('menus', this.menus)

      addRoutes(res.data)
    },

    async loginAction(loginData: Login) {
      try {
        const res = await login(loginData)

        this.userId = res.userId
        this.user = res.username
        this.role = res.role || ''
        this.isAdmin = res.isAdmin || false
        this.permissions = res.permissions || {
          userManagement: false,
          roleManagement: false,
          menuManagement: false,
          bookingManagement: false,
          roomManagement: false,
          reportManagement: false,
          financialManagement: false,
          canEdit: false,
          canDelete: false,
          canAssignPermission: false,
        }

        localCache.setCache('username', res.username)
        localCache.setCache(PRIMARY_TOKEN, res.token)
        localCache.setCache('userId', res.userId)
        localCache.setCache('role', res.role || '')
        localCache.setCache('isAdmin', res.isAdmin || false)
        localCache.setCache('permissions', JSON.stringify(this.permissions))
      } catch (err) {
        throw err
      }
    },
    async logoutAction() {
      try {
        const confirmRes = await showConfirm('确定要退出登录吗？', '提示')
        if (confirmRes) {
          this.userId = ''
          this.token = ''
          this.role = ''
          this.isAdmin = false
          this.permissions = {
            userManagement: false,
            roleManagement: false,
            menuManagement: false,
            bookingManagement: false,
            roomManagement: false,
            reportManagement: false,
            financialManagement: false,
            canEdit: false,
            canDelete: false,
            canAssignPermission: false,
          }
          localCache.removeCache(PRIMARY_TOKEN)
          localCache.removeCache('userId')
          localCache.removeCache('role')
          localCache.removeCache('isAdmin')
          localCache.removeCache('permissions')
          MessagePrompt('退出成功', 'success')
          return true
        }
        return false
      } catch {}
    },
  },
})
