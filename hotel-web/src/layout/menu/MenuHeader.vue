<template>
  <div class="menu-header" :class="{ 'is-collapse': collapse }">
    <div class="logo">
      <el-image :src="avatarUrl" fit="fill" class="logo-avartar"></el-image>
    </div>
    <div class="logo-title" v-if="show">{{ TITLE_SECOND }}</div>
    <div class="version" v-if="show">{{ version }}</div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import avatarImg from '@/assets/avatar.jpg'
import { TITLE_SECOND } from '@/global/title'

// 头像地址
const avatarUrl = ref(avatarImg)


const version = ref('v1.0.0')

const props = defineProps<{
  collapse?: boolean
}>()

const show = ref(true)
watch(
  () => props.collapse,
  (collapse: boolean) => {
    if (!collapse) {
      setTimeout(() => {
        show.value = !collapse
      }, 300)
    } else {
      show.value = !collapse
    }
  },
)
</script>

<style lang="scss" scoped>
.menu-header {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  transition: all 0.3s;

  .logo {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    overflow: hidden;
    box-shadow: 0px 0px 3px 5px rgba(209, 199, 199, 0.1);
    .logo-avatar {
      width: 100%;
      height: 100%;
    }
  }
  .logo-title {
    margin-top: 10px;
    letter-spacing: 1.3px;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
    font-weight: bold;
  }
  .version {
    font-weight: normal;
    font-size: 12px;
    letter-spacing: 1.5px;
    margin: 5px 0 10px 0;
  }

  // 折叠时只显示 Logo，并适当缩小间距
  &.is-collapse {
    .logo {
      width: 40px;
      height: 40px;
    }
    margin-top: 20px;
  }
}
</style>
