<template>
  <div class="skeleton-table" :style="{ width, height }">
    <div class="skeleton-table-header">
      <div
        v-for="(col, index) in columns"
        :key="'header-' + index"
        class="skeleton-table-header-cell"
        :style="{
          width: getColumnWidth(col),
          textAlign: col.align || 'center',
        }"
      >
        <MySkeleton width="60%" height="14px" :animated="true" />
      </div>
    </div>

    <div class="skeleton-table-body">
      <div
        v-for="(row, rowIndex) in rows"
        :key="'row-' + rowIndex"
        class="skeleton-table-row"
      >
        <div
          v-for="(col, colIndex) in columns"
          :key="'cell-' + rowIndex + '-' + colIndex"
          class="skeleton-table-cell"
          :style="{
            width: getColumnWidth(col),
            textAlign: col.align || 'center',
          }"
        >
          <MySkeleton
            :width="col.skeletonWidth || '70%'"
            :height="cellHeight"
            :variant="col.variant || 'text'"
            :animated="true"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import MySkeleton from './MySkeleton.vue'

export interface SkeletonColumn {
  width?: number | string
  skeletonWidth?: string
  align?: 'left' | 'center' | 'right'
  variant?: 'circle' | 'rect' | 'text'
}

interface Props {
  width?: string
  height?: string
  rows?: number
  columns?: SkeletonColumn[]
  cellHeight?: string
}

const props = withDefaults(defineProps<Props>(), {
  width: '100%',
  height: '400px',
  rows: 5,
  columns: () => [
    { width: 80, skeletonWidth: '50px' },
    { width: 100 },
    { width: 120 },
    { width: 100 },
    { width: 100 },
    { width: 80 },
  ],
  cellHeight: '16px',
})

const getColumnWidth = (col: SkeletonColumn): string => {
  if (typeof col.width === 'number') {
    return `${col.width}px`
  }
  if (col.width) {
    return String(col.width)
  }
  return 'auto'
}
</script>

<style lang="scss" scoped>
.skeleton-table {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.skeleton-table-header {
  display: flex;
  align-items: center;
  padding: 16px;
  background: linear-gradient(
    135deg,
    rgba(56, 88, 191, 0.08) 0%,
    rgba(72, 104, 192, 0.04) 100%
  );
  border-bottom: 1px solid #f0f0f0;
}

.skeleton-table-header-cell {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.skeleton-table-body {
  padding: 8px 16px;
}

.skeleton-table-row {
  display: flex;
  align-items: center;
  padding: 14px 0;
  border-bottom: 1px solid #fafafa;

  &:last-child {
    border-bottom: none;
  }
}

.skeleton-table-cell {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
