<template>
  <div class="breadcumb">
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item
        :to="{ path: '/' }"
        v-for="item in tabs"
        :key="item"
        >{{ item.meta.title }}</el-breadcrumb-item
      >
    </el-breadcrumb>
  </div>
</template>

<script setup lang="ts">
import { ArrowRight } from '@element-plus/icons-vue'
import { ref, watch } from 'vue'
import type { Ref } from 'vue'
import { useRoute, type RouteLocationMatched } from 'vue-router'

const route = useRoute()
let tabs: Ref<RouteLocationMatched[]> = ref([])
// 监听matched
const getBredcrumb = () => {
  let matched: any = route.matched.filter(
    (item) => item.meta && item.meta.title,
  )
  const first = matched[0]
  if (first.path !== '/') {
    matched = [{ path: '/', meta: { title: '首页' } as any }].concat(matched)
  }
  tabs.value = matched
}

watch(
  () => route.path,
  () => getBredcrumb(),
)
</script>

<style lang="scss" scoped>
.breadcumb {
  margin-left: 20px;
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  cursor: pointer !important;
}
</style>
