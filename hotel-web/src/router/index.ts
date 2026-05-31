import { createRouter, createWebHistory } from 'vue-router'

import type { Menus } from '@/types/index'
import { setupAuth } from './auth'
import 'nprogress/nprogress.css'
import type { RouteRecordRaw } from 'vue-router'

// Layout is always needed (shell), so eager import is correct
import Layout from '@/layout/index.vue'
const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: Layout,
    meta: {
      title: '首页',
    },
    children: [
      {
        path: '/',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
      },
    ],
  },
  {
    path: '/login',
    name: 'Login',
    meta: {
      title: '登录页',
    },
    component: () => import('@/views/login/index.vue'),
  },
  {
    path: '/404',
    name: 'NotFound',
    meta: {
      title: '404页',
    },
    component: () => import('@/views/404/index.vue'),
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404',
  },
]

// 动态路由
const asyncRoutes = [
  {
    path: '/',
    name: '/',
    component: Layout,
    meta: {
      title: '首页',
    },
  },

  {
    path: '/booking/list',
    name: 'BookingList',
    component: () => import('@/views/booking/list/index.vue'),
    meta: {
      title: '预定列表',
    },
  },
  {
    path: '/dashboard',
    name: 'DashboardPanel',
    component: () => import('@/views/dashboard/index.vue'),
    meta: {
      title: '房态面板',
    },
  },
  {
    path: '/roomstatus',
    name: 'Roomstatus',
    component: () => import('@/views/roomstatus/index.vue'),
    meta: {
      title: '房态面板',
    },
  },
  {
    path: '/system/role',
    name: 'Role',
    component: () => import('@/views/system/role/index.vue'),
    meta: {
      title: '角色管理',
    },
  },
  {
    path: '/system/user',
    name: 'User',
    component: () => import('@/views/system/user/index.vue'),
    meta: {
      title: '用户管理',
    },
  },
  {
    path: '/system/menu',
    name: 'Menu',
    component: () => import('@/views/system/menu/index.vue'),
    meta: {
      title: '菜单管理',
    },
  },
  {
    path: '/base/roomType',
    name: 'RoomType',
    component: () => import('@/views/base/roomType/index.vue'),
    meta: {
      title: '房型管理',
    },
  },
  {
    path: '/base/floor',
    name: 'Floor',
    component: () => import('@/views/base/floor/index.vue'),
    meta: {
      title: '楼层管理',
    },
  },
  {
    path: '/base/channel',
    name: 'Channel',
    component: () => import('@/views/base/channel/index.vue'),
    meta: {
      title: '渠道管理',
    },
  },
  {
    path: '/check/list',
    name: 'List',
    component: () => import('@/views/List/index.vue'),
    meta: {
      title: '入住列表',
    },
  },
  {
    path: '/order/list',
    name: 'OrderList',
    component: () => import('@/views/order/OrderList.vue'),
    meta: {
      title: '订单列表',
    },
  },
  {
    path: '/member',
    name: 'MemberList',
    component: () => import('@/views/member/MemberList.vue'),
    meta: {
      title: '会员管理',
    },
  },
  {
    path: '/room/cleaning',
    name: 'RoomCleaning',
    component: () => import('@/views/room/cleaning/index.vue'),
    meta: {
      title: '清洁管理',
    },
  },
  {
    path: '/notice/create',
    name: 'NoticeCreate',
    component: () => import('@/views/notice/create/index.vue'),
    meta: {
      title: '新增公告',
    },
  },
  {
    path: '/notice/list',
    name: 'NoticeList',
    component: () => import('@/views/notice/list/index.vue'),
    meta: {
      title: '公告列表',
    },
  },
  // 财务管理
  {
    path: '/finance/history-order',
    name: 'HistoryOrder',
    component: () => import('@/views/finance/historyOrder/index.vue'),
    meta: {
      title: '历史订单',
    },
  },
  {
    path: '/finance/financial-report',
    name: 'FinancialReport',
    component: () => import('@/views/finance/financialReport/index.vue'),
    meta: {
      title: '财务报表',
    },
  },
  // 会员消费
  {
    path: '/member-consume/consume-record',
    name: 'ConsumeRecord',
    component: () => import('@/views/memberConsume/consumeRecord/index.vue'),
    meta: {
      title: '消费记录',
    },
  },
  // 商品库存
  {
    path: '/goods-inventory/goods-manage',
    name: 'GoodsManage',
    component: () => import('@/views/goodsInventory/goodsManage/index.vue'),
    meta: {
      title: '商品管理',
    },
  },
  // 系统管理 - 操作日志
  {
    path: '/system/operation-log',
    name: 'OperationLog',
    component: () => import('@/views/system/operationLog/index.vue'),
    meta: {
      title: '操作日志',
    },
  },
]

export function addRoutes(menus: Menus[]) {
  const hasRouteByPath = (path: string) => {
    return router.getRoutes().some((route) => route.path === path)
  }

  const findAndAddRouteByMenus = (arr) => {
    arr.forEach((e) => {
      let item = asyncRoutes.find((o) => o.path == e.path)
      if (item && !hasRouteByPath(item.path)) {
        router.addRoute('Home', item)
      }
      if (e.children && e.children.length > 0) {
        findAndAddRouteByMenus(e.children)
      }
    })
  }
  findAndAddRouteByMenus(menus)
}

export const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫
setupAuth(router)
