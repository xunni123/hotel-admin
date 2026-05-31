<template>
  <!-- is children -->
  <el-sub-menu
    v-if="menu.children && menu.children.length"
    :index="menu.menu_id.toString()"
    popper-append-to-body
  >
    <template #title>
      <el-icon style="color: #fff">
        <component :is="menu.icon"></component>
      </el-icon>
      <span style="color: #fff">{{ menu.menu_name }}</span>
    </template>

    <!-- 递归 -->
    <menu-item
      v-for="child in menu.children"
      :key="child.menu_id"
      :menu="child"
    />
  </el-sub-menu>

  <!-- not children  -->
  <el-menu-item v-else :index="menu.path || menu.menu_id.toString()">
    <el-icon style="color: #fff">
      <component :is="menu.icon"></component>
    </el-icon>
    <span style="color: #fff">{{ menu.menu_name }}</span>
  </el-menu-item>
</template>

<script setup lang="ts">
import type { Menus } from '@/types/index'

const props = defineProps<{
  menu: Menus
}>()
</script>

<style lang="scss" scoped></style>
