import { defineStore } from 'pinia'

export type Tab = {
  title: string
  path: string
}

export type TabState = {
  tabList: Tab[]
}

// 固定标签
export const HOME_TAB: Tab = {
  title: '首页',
  path: '/',
}

function isHomePath(path: string) {
  return path === HOME_TAB.path
}

function normalizeTabOrder(list: Tab[]): Tab[] {
  const home = list.find((t) => isHomePath(t.path))
  const rest = list.filter((t) => !isHomePath(t.path))
  const homeTab: Tab = { ...HOME_TAB, ...(home || {}) }
  homeTab.title = HOME_TAB.title
  return [homeTab, ...rest]
}

export const useTabsStore = defineStore('tabs', {
  state: (): TabState => ({
    tabList: [],
  }),
  getters: {
    getTab(state) {
      return state.tabList
    },
  },
  actions: {
    addTab(tab: Tab) {
      if (this.tabList.some((item) => item.path === tab.path)) {
        this.tabList = normalizeTabOrder(this.tabList)
        return
      }
      this.tabList.push(tab)
      this.tabList = normalizeTabOrder(this.tabList)
    },
    setTabList(list: Tab[]) {
      this.tabList = normalizeTabOrder(list)
    },
  },
})
