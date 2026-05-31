<template>
  <div class="pagination-container">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="pageSizes"
      :layout="layout"
      :total="total"
      :background="background"
      :pager-count="pagerCount"
      :prev-text="prevText"
      :next-text="nextText"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'

interface Props {
  total?: number
  page?: number
  limit?: number
  pageSizes?: number[]
  pagerCount?: number
  layout?: string
  background?: boolean
  prevText?: string
  nextText?: string
}

const props = withDefaults(defineProps<Props>(), {
  total: 0,
  page: 1,
  limit: 10,
  pageSizes: () => [5, 10, 15, 20],
  pagerCount: 7,
  layout: 'total, sizes, prev, pager, next, jumper',
  background: false,
  prevText: '上一页',
  nextText: '下一页',
})

const emit = defineEmits<{
  'update:page': [value: number]
  'update:limit': [value: number]
  'current-change': [value: number]
  'size-change': [value: number]
}>()

const currentPage = ref(props.page)
const pageSize = ref(props.limit)

watch(
  () => props.page,
  (val) => {
    currentPage.value = val
  },
)
watch(
  () => props.limit,
  (val) => {
    pageSize.value = val
  },
)

const handleSizeChange = (val: number) => {
  emit('update:limit', val)
  emit('size-change', val)
}

const handleCurrentChange = (val: number) => {
  emit('update:page', val)
  emit('current-change', val)
}
</script>

<style scoped lang="scss">
.pagination-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
  padding: 16px 0;
  gap: 10px;
}

.pagination-info {
  font-size: 14px;
  color: var(--text-primary);
  font-weight: 500;
}

:deep(.el-pagination) {
  display: flex;
  align-items: center;
  gap: 10px;

  .el-pagination__total {
    color: var(--text-primary);
    font-size: 14px;
  }

  .el-pagination__sizes {
    margin: 0;
    .el-select .el-input__wrapper {
      border-radius: 4px;
      border: 1px solid #dcdfe6;
    }
  }

  .el-pagination__jump {
    margin: 0;
    .el-input__wrapper {
      border-radius: 4px;
      border: 1px solid #dcdfe6;
    }
  }

  .btn-prev,
  .btn-next {
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    color: var(--text-primary);
    background-color: #fff;

    &:hover {
      color: var(--tabs);
      border-color: var(--tabs);
    }
  }

  .el-pager {
    li {
      border-radius: 4px;
      margin: 0 2px;
      border: 1px solid #dcdfe6;
      background-color: #fff;

      &.is-active {
        background-color: var(--tabs);
        color: #fff;
        border-color: var(--tabs);
      }

      &:hover {
        border-color: var(--tabs);
      }
    }
  }

  .el-pagination__editor.is-invalid .el-input__wrapper {
    box-shadow: 0 0 0 1px var(--danger) inset;
  }

  .el-pagination__editor .el-input__inner {
    border-radius: 4px;
  }
}
</style>
