<template>
  <div>
    <el-tree
      style="max-width: 600px"
      :data="data"
      :props="defaultProps"
      highlight-current="true"
      default-expand-all="true"
      empty-text="无数据"
      show-checkbox
      icon="setting"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useLoginStore } from '@/store/login'
import cache from '@/utils/cache'
const { localCache } = cache
const loginStore = useLoginStore()
const defaultProps = {
  children: 'children',
  label: 'label',
  disabled: 'disabled',
}

const data = [
  {
    id: 1,
    label: 'Level one 1',
    children: [
      {
        id: 3,
        label: 'Level two 2-1',
        children: [
          {
            id: 4,
            label: 'Level three 3-1-1',
          },
          {
            id: 5,
            label: 'Level three 3-1-2',
            disabled: true,
          },
        ],
      },
      {
        id: 2,
        label: 'Level two 2-2',
        disabled: true,
        children: [
          {
            id: 6,
            label: 'Level three 3-2-1',
          },
          {
            id: 7,
            label: 'Level three 3-2-2',
            disabled: true,
          },
        ],
      },
    ],
  },
]
</script>

<style scoped></style>
