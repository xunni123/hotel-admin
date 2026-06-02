<template>
  <div class="header-bar">
    <div class="header-left">
      <el-icon size="25" @click="toggleCollapse">
        <Fold v-if="collapse" />
        <Expand v-else />
      </el-icon>
      <Breadcrumb />
    </div>
    <div class="user-info">
      <div @click="toggleFullsscreen" class="header-screen">
        <FullScreen v-if="isFullscreen" />
        <Aim v-else />
      </div>
      <div class="user-avatar">
        <el-image :src="avatarUrl" fit="fill"></el-image>
      </div>

      <div class="user-name">{{ username }}</div>
      <el-dropdown placement="bottom" @command="handleCommand">
        <el-icon style="color: #fff" class="icon-arrow"><ArrowDown /></el-icon>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="logout"> 退出登录 </el-dropdown-item>
            <el-dropdown-item command="respass">修改密码</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

    <!-- 密码修改抽屉 -->
    <PasswordDrawer
      v-model:visible="passwordDrawerVisible"
      :user-id="userId"
      @success="handlePasswordSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Breadcrumb from '@/layout/header/c-pm/Breadcrumb.vue'
import PasswordDrawer from '@/layout/header/PasswordDrawer.vue'
import { useLoginStore } from '@/store/login'

import screenfull from 'screenfull'

import {
  ArrowDown,
  Expand,
  Fold,
  FullScreen,
  Aim,
} from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const isFullscreen = ref(true)
const toggleFullsscreen = () => {
  if (screenfull.isEnabled) {
    isFullscreen.value = screenfull.isFullscreen
    screenfull.toggle()
  }
}

const router = useRouter()
const loginStore = useLoginStore()

import avatarImg from '@/assets/avatar.jpg'
import { MessagePrompt } from '@/utils/message'

const avatarUrl = ref(avatarImg)
const title = ref('欢迎来到酒店后台')
const username = loginStore.user
const userId = Number(loginStore.userId)

const passwordDrawerVisible = ref(false)

const handlePasswordSuccess = () => {
  passwordDrawerVisible.value = false
  MessagePrompt('密码修改成功', 'success')
}

// 右侧功能判断
const handleCommand = async (command: string) => {
  switch (command) {
    case 'logout':
      const response = await loginStore.logoutAction()
      if (response) {
        router.push('/login')
      }
      break
    case 'respass':
      passwordDrawerVisible.value = true
      break
  }
}

const emits = defineEmits<{
  toggleCollapse: []
}>()

const toggleCollapse = () => {
  emits('toggleCollapse')
}

defineProps<{
  collapse?: boolean
}>()
</script>

<style lang="scss" scoped>
.header-bar {
  height: 60px;
  line-height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  padding: 0 20px;
  .header-left {
    display: flex;
    align-items: center;
    flex: 1;
    :deep(.el-breadcrumb__inner),
    :deep(.el-breadcrumb__inner a) {
      color: #fff !important;
    }
  }
  .header-screen {
    display: flex;
    align-items: center;
    height: 100%;
    width: 25px;
    margin-right: 20px;
  }
  .header-title {
    font-size: 14px;
  }
  .user-info {
    display: flex;
    gap: 15px;
    justify-content: flex-end;
    align-items: center;
    flex: 1;
    .user-avatar {
      width: 30px;
      height: 30px;
      border-radius: 50%;
      overflow: hidden;
    }
    .user-name {
      font-size: 12px;
    }
    .icon-arrow {
      transition: all 0.3s;
      padding: 10px;
      border-radius: 5px;
    }
    .icon-arrow:hover {
      background-color: var(--bg-page);
      color: #000 !important;
    }
  }
}
</style>
