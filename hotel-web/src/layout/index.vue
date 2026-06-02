<template lang="">
  <div class="common-layout hotel-layout">
    <el-container>
      <el-aside class="hotel-aside" :width="collapse ? '64px' : '200px'">
        <menu-header :collapse="collapse" />
        <menu-bar :collapse="collapse" />
      </el-aside>
      <el-container>
        <el-header class="hotel-header"
          ><header-bar :collapse="collapse" @toggleCollapse="toggleCollapse"
        /></el-header>

        <el-main class="hotel-main">
          <div class="tabs-wrapper">
            <Tabs />
          </div>
          <div class="content-wrapper">
            <router-view></router-view>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import HeaderBar from '@/layout/header/Header.vue'
import MenuBar from '@/layout/menu/MenuBar.vue'
import MenuHeader from './menu/MenuHeader.vue'
import Breadcrumb from './header/c-pm/Breadcrumb.vue'
import Tabs from '@/layout/header/c-pm/Tabs.vue'
import { collapseStore } from '@/store/collapse'
import { ref, watch } from 'vue'
import { storeToRefs } from 'pinia'
const clspStore = collapseStore()

const { collapse } = storeToRefs(clspStore)
const { setCollapse } = clspStore

// 切换
const toggleCollapse = () => {
  setCollapse(!collapse.value)
}
</script>

<style lang="scss" scoped>
.hotel-layout {
  color: var(--bg-page);
  height: 100vh;

  :deep(.el-container) {
    height: 100%;
  }

  .hotel-header {
    width: 100%;
    height: 60px;
    line-height: 60px;
    background-color: var(--text-primary);
  }
  .header-breadcrumb {
    color: #000;
    background-color: #fff;
  }
  .hotel-aside {
    height: 100%;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    background-color: var(--primary);
    transition: width 0.3s;
  }
  :deep(.el-main) {
    --el-main-padding: 0px !important;
    margin: 0 !important;
    scrollbar-width: none; // Firefox
    -ms-overflow-style: none; // IE 和 Edge

    &::-webkit-scrollbar {
      // Chrome, Safari, Edge
      display: none;
    }
  }
  .hotel-main {
    color: #000;
    background-color: #f4f7f9;
    overflow-y: auto;
    position: relative;

    .tabs-wrapper {
      position: sticky;
      top: 0;
      z-index: 100;
      background-color: #fff;
    }

    .content-wrapper {
      padding: 20px;
      min-height: calc(100vh - 60px - 50px);
    }
  }
}
</style>
