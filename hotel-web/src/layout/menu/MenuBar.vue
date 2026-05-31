<template>
  <div class="menu-container">
    <el-menu
      :default-active="defaultRoute"
      class="el-menu-vertical-demo"
      :class="{ 'is-collapse': collapse }"
      :collapse="collapse"
      @open=""
      @close=""
      router
      background-color="var(--primary)"
      text-color="#ffffff"
      active-text-color="#ffffff"
      popper-class="hotel-sidebar-menu-popper"
      style="border-right: none"
    >
      <menu-item v-for="item in menusList" :key="item.menu_id" :menu="item" />
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import type { Menus } from '@/types/index'
import { useLoginStore } from '@/store/login'
import { ref, onMounted, computed } from 'vue'
import MenuItem from './MenuItem.vue'
import cache from '@/utils/cache.ts'
import { useRoute } from 'vue-router'

const route = useRoute()
const { localCache } = cache

const loginStore = useLoginStore()
const defaultRoute = computed(() => route.path)
const menusList = computed(() => loginStore.menus)

onMounted(async () => {
  const id = localCache.getCache('userId')
  await loginStore.getMenusInfo(id)
})

defineProps<{
  collapse?: boolean
}>()
</script>

<style lang="scss" scoped>
.menu-container {
  width: 100%;
  margin-top: 20px;
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background-color: rgba(255, 255, 255, 0.3);
    border-radius: 3px;
  }

  &::-webkit-scrollbar-track {
    background-color: transparent;
  }
}

.el-menu {
  background-color: var(--primary) !important;
  border-right: none;
  transition: width 0.3s;
}

// 嵌套子菜单容器（展开时内联）与侧栏同色，避免 Element 默认白底
:deep(.el-menu--inline) {
  background-color: var(--primary) !important;
}

// 展开时的菜单项样式（子组件 MenuItem 内需 :deep）
:deep(.el-menu-item),
:deep(.el-sub-menu__title) {
  background-color: var(--primary) !important;
  color: #fff !important;
  transition: background-color 0.2s;
  text-align: center;
}

// 悬浮效果
:deep(.el-menu-item:hover),
:deep(.el-sub-menu__title:hover) {
  background-color: #3858bf !important;
  color: #fff !important;
}

// 激活菜单项高亮
:deep(.el-menu-item.is-active) {
  background-color: rgb(85, 97, 228) !important;
  color: #fff !important;
  border-left: 5px solid #fff;
}

// 子菜单激活项（如果有二级菜单）
:deep(.el-sub-menu .el-menu-item.is-active) {
  background-color: #2c6b9e !important;
}

// 折叠状态样式
.is-collapse {
  :deep(.el-menu-item),
  :deep(.el-sub-menu__title) {
    background-color: transparent !important;
  }

  :deep(.el-menu-item:hover),
  :deep(.el-sub-menu__title:hover) {
    background-color: #3858bf !important;
  }

  :deep(.el-menu-item.is-active) {
    background-color: var(--info) !important;
    border-left: none;
  }
}

// 弹出菜单（折叠时悬浮显示；teleport 到 body，用全局类名在 common.scss 中补充）
:deep(.el-menu--popup) {
  background-color: var(--primary) !important;

  .el-menu-item {
    background-color: transparent !important;
    &:hover {
      background-color: #3858bf !important;
    }
    &.is-active {
      background-color: var(--info) !important;
    }
  }
}
</style>
