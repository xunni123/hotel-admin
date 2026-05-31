import { createRouter, createWebHistory } from 'vue-router'

import type { Menus } from '@/types/index'
import { setupAuth } from './auth'
import 'nprogress/nprogress.css'
import type { RouteRecordRaw } from 'vue-router'

// 页面
import Layout from '@/layout/index.vue'
import Login from '@/views/login/index.vue'
import NotFound from '@/views/404/index.vue'
import BookingList from '@/views/booking/list/index.vue'
import DashBoard from '@/views/dashboard/index.vue'
import RoomStatus from '@/views/roomstatus/index.vue'
import User from '@/views/system/user/index.vue'
import Role from '@/views/system/role/index.vue'
import Menu from '@/views/system/menu/index.vue'
import RoomType from '@/views/base/roomType/index.vue'
import Floor from '@/views/base/floor/index.vue'
import Channel from '@/views/base/channel/index.vue'
// 入住列表
import List from '@/views/List/index.vue'
// 订单列表
import OrderList from '@/views/order/OrderList.vue'
// 会员列表
import MemberList from '@/views/member/MemberList.vue'

//清洁管理
import RoomCleaning from '@/views/room/cleaning/index.vue'
//公告管理
import NoticeCreate from '@/views/notice/create/index.vue'
import NoticeList from '@/views/notice/list/index.vue'
//财务管理
import HistoryOrder from '@/views/finance/historyOrder/index.vue'
import FinancialReport from '@/views/finance/financialReport/index.vue'
//会员消费
import ConsumeRecord from '@/views/memberConsume/consumeRecord/index.vue'
//商品库存
import GoodsManage from '@/views/goodsInventory/goodsManage/index.vue'
//系统管理 - 操作日志
import OperationLog from '@/views/system/operationLog/index.vue'
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
        component: DashBoard,
      },
    ],
  },
  {
    path: '/login',
    name: 'Login',
    meta: {
      title: '登录页',
    },
    component: Login,
  },
  {
    path: '/404',
    name: 'NotFound',
    meta: {
      title: '404页',
    },
    component: NotFound,
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
    component: BookingList,
    meta: {
      title: '预定列表',
    },
  },
  {
    path: '/dashboard',
    name: 'DashboardPanel',
    component: DashBoard,
    meta: {
      title: '房态面板',
    },
  },
  {
    path: '/roomstatus',
    name: 'Roomstatus',
    component: RoomStatus,
    meta: {
      title: '房态面板',
    },
  },
  {
    path: '/system/role',
    name: 'Role',
    component: Role,
    meta: {
      title: '角色管理',
    },
  },
  {
    path: '/system/user',
    name: 'User',
    component: User,
    meta: {
      title: '用户管理',
    },
  },
  {
    path: '/system/menu',
    name: 'Menu',
    component: Menu,
    meta: {
      title: '菜单管理',
    },
  },
  {
    path: '/base/roomType',
    name: 'RoomType',
    component: RoomType,
    meta: {
      title: '房型管理',
    },
  },
  {
    path: '/base/floor',
    name: 'Floor',
    component: Floor,
    meta: {
      title: '楼层管理',
    },
  },
  {
    path: '/base/channel',
    name: 'Channel',
    component: Channel,
    meta: {
      title: '渠道管理',
    },
  },
  {
    path: '/check/list',
    name: 'List',
    component: List,
    meta: {
      title: '入住列表',
    },
  },
  {
    path: '/order/list',
    name: 'OrderList',
    component: OrderList,
    meta: {
      title: '订单列表',
    },
  },
  {
    path: '/member',
    name: 'MemberList',
    component: MemberList,
    meta: {
      title: '会员管理',
    },
  },
  {
    path: '/room/cleaning',
    name: 'RoomCleaning',
    component: RoomCleaning,
    meta: {
      title: '清洁管理',
    },
  },
  {
    path: '/notice/create',
    name: 'NoticeCreate',
    component: NoticeCreate,
    meta: {
      title: '新增公告',
    },
  },
  {
    path: '/notice/list',
    name: 'NoticeList',
    component: NoticeList,
    meta: {
      title: '公告列表',
    },
  },
  // 财务管理
  {
    path: '/finance/history-order',
    name: 'HistoryOrder',
    component: HistoryOrder,
    meta: {
      title: '历史订单',
    },
  },
  {
    path: '/finance/financial-report',
    name: 'FinancialReport',
    component: FinancialReport,
    meta: {
      title: '财务报表',
    },
  },
  // 会员消费
  {
    path: '/member-consume/consume-record',
    name: 'ConsumeRecord',
    component: ConsumeRecord,
    meta: {
      title: '消费记录',
    },
  },
  // 商品库存
  {
    path: '/goods-inventory/goods-manage',
    name: 'GoodsManage',
    component: GoodsManage,
    meta: {
      title: '商品管理',
    },
  },
  // 系统管理 - 操作日志
  {
    path: '/system/operation-log',
    name: 'OperationLog',
    component: OperationLog,
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
