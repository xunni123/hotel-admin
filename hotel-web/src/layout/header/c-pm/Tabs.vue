<template>
  <div class="tabs">
    <el-tabs
      v-model="activeTab"
      type="card"
      class="demo-tabs"
      closable
      @tab-remove="removeTab"
      @tab-click="tabClick"
    >
      <el-tab-pane
        v-for="item in tabList"
        :key="item.path"
        :label="item.title"
        :name="item.path"
        :closable="item.path !== HOME_TAB.path"
      >
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed, onMounted } from 'vue'
import type { TabPaneName, TabsPaneContext } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import type { Tab } from '@/store/tabs'
import { HOME_TAB, useTabsStore } from '@/store/tabs'
import cache from '@/utils/cache'
const { localCache } = cache
const tabsStore = useTabsStore()

const router = useRouter()
const route = useRoute()

// 当前tab
const activeTab = ref('')

// 点击路由后的tab
const setActiveTab = () => {
  activeTab.value = route.path
}

const tabList = computed(() => {
  return tabsStore.getTab
})

// 添加到标签页
const addTab = () => {
  const { path, meta } = route
  if (path === '/' || path === '/dashboard') {
    if (path === '/') {
      tabsStore.addTab({ title: HOME_TAB.title, path: HOME_TAB.path })
    }
    return
  }
  const tabs: Tab = {
    title: meta.title as string,
    path: path,
  }
  tabsStore.addTab(tabs)
}

// 关闭tab
const removeTab = (targetName: TabPaneName) => {
  if (targetName === HOME_TAB.path) return
  const tabs = tabList.value
  let activeName = activeTab.value
  if (activeName === targetName) {
    const targetIndex = tabs.findIndex((tab) => tab.path === targetName)
    if (targetIndex !== -1) {
      const nextTab = tabs[targetIndex + 1] || tabs[targetIndex - 1]
      activeName = nextTab ? nextTab.path : HOME_TAB.path
    }
    activeTab.value = activeName
    router.push(activeName)
  }

  tabsStore.setTabList(tabs.filter((tab) => tab.path !== targetName))
}

// tab点击事件
const tabClick = (tab: TabsPaneContext) => {
  const { name } = tab.props
  router.push(name as string)
}

// 缓存tab
const beforeRefresh = () => {
  window.addEventListener('beforeunload', () => {
    localCache.setCache('tabsView', tabList.value)
  })
  let tabLocal = localCache.getCache('tabsView')
  if (tabLocal && tabLocal.length > 0) {
    tabsStore.setTabList(tabLocal)
  }
}

// 监听路由变化
watch(
  () => route.path,
  () => {
    ;(setActiveTab(), addTab())
  },
)

onMounted(() => {
  ;(beforeRefresh(), setActiveTab(), addTab())
})
</script>

<style lang="scss" scoped>
.tabs {
  background-color: #fff;
  padding: 10px 10px 0;

  :deep(.el-tabs__item.is-active),
  :deep(.el-tabs__item:hover) {
    color: var(--tabs) !important;
    font-weight: bold;
  }
  :deep(.el-tabs__item.is-active) {
    background-color: var(--tabs) !important;
    color: #fff !important;
  }
  :deep(.el-tabs__nav-close:hover) {
    background-color: rgba(0, 0, 0, 0.2) !important;
    border-radius: 50%;
    transition: all 0.2s;
  }
  :deep(.el-tabs--card > .el-tabs__header .el-tabs__nav) {
    border: none !important;
    margin: 0;
  }
  :deep(.el-tabs--card > .el-tabs__header .el-tabs__item) {
    margin: 0;
  }
  :deep(.el-tabs__header) {
    border-bottom: none;
  }
}
</style>
