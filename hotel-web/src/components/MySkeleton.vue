<template>
  <div class="skeleton-wrapper" :class="{ 'is-table': isTable }">
    <template v-if="isTable">
      <div v-for="i in rows" :key="i" class="skeleton-table-row">
        <div
          v-for="(col, index) in columns"
          :key="index"
          class="skeleton-table-cell"
          :style="{ width: typeof col === 'number' ? `${col}px` : 'auto' }"
        >
          <div
            class="skeleton-content"
            :class="{ circle: col === 'circle', text: col === 'text' }"
            :style="{
              width: getCellWidth(col, index),
              height: cellHeight,
            }"
          ></div>
        </div>
      </div>
    </template>

    <template v-else-if="isCard">
      <div v-for="i in rows" :key="i" class="skeleton-card">
        <div class="skeleton-card-header">
          <div class="skeleton-content" :style="{ width: '60%', height: '16px' }"></div>
        </div>
        <div class="skeleton-card-content">
          <div class="skeleton-content" :style="{ width: '80%', height: '14px' }"></div>
          <div class="skeleton-content" :style="{ width: '50%', height: '14px' }"></div>
        </div>
      </div>
    </template>

    <template v-else>
      <div
        class="skeleton"
        :class="{ animated, 'inline-block': inline }"
        :style="customStyle"
      >
        <div
          class="skeleton-content"
          :style="{
            width,
            height,
            borderRadius: borderRadius,
          }"
        ></div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

type SkeletonVariant = 'circle' | 'rect' | 'text'
type SkeletonMode = 'default' | 'table' | 'card'

interface Props {
  width?: string
  height?: string
  borderRadius?: string
  animated?: boolean
  inline?: boolean
  variant?: SkeletonVariant
  mode?: SkeletonMode
  rows?: number
  columns?: (number | string)[]
  cellHeight?: string
}

const props = withDefaults(defineProps<Props>(), {
  width: '100%',
  height: '16px',
  borderRadius: '4px',
  animated: true,
  inline: false,
  variant: 'rect',
  mode: 'default',
  rows: 3,
  columns: () => [80, 100, 120, 80, 100],
  cellHeight: '16px',
})

const isTable = computed(() => props.mode === 'table')
const isCard = computed(() => props.mode === 'card')

const customStyle = computed(() => {
  const style: Record<string, string> = {}

  if (props.variant === 'circle') {
    style.borderRadius = '50%'
    style.width = props.height
  } else if (props.variant === 'text') {
    style.borderRadius = '4px'
    style.height = '1em'
  } else {
    style.borderRadius = props.borderRadius
  }

  return style
})

const getCellWidth = (col: number | string, index: number): string => {
  if (typeof col === 'number') {
    return `${col}px`
  }
  if (col === 'circle') {
    return props.cellHeight
  }
  if (col === 'text') {
    return `${50 + (index % 3) * 15}%`
  }
  return 'auto'
}
</script>

<style lang="scss" scoped>
.skeleton-wrapper {
  &.is-table {
    width: 100%;
  }
}

.skeleton {
  display: block;

  &.inline-block {
    display: inline-block;
  }

  .skeleton-content {
    background: linear-gradient(
      90deg,
      #f0f0f0 25%,
      #e8e8e8 50%,
      #f0f0f0 75%
    );
    background-size: 200% 100%;
  }

  &.animated .skeleton-content {
    animation: skeleton-loading 1.5s ease-in-out infinite;
  }
}

.skeleton-table-row {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;

  &:last-child {
    border-bottom: none;
  }
}

.skeleton-table-cell {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 8px;

  .skeleton-content {
    background: linear-gradient(
      90deg,
      #f0f0f0 25%,
      #e8e8e8 50%,
      #f0f0f0 75%
    );
    background-size: 200% 100%;
    border-radius: 4px;

    &.circle {
      border-radius: 50%;
    }

    &.text {
      border-radius: 4px;
      height: 1em;
    }
  }

  &.animated .skeleton-content {
    animation: skeleton-loading 1.5s ease-in-out infinite;
  }
}

.skeleton-card {
  padding: 16px;
  margin-bottom: 16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

  .skeleton-card-header {
    margin-bottom: 12px;
  }

  .skeleton-card-content {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .skeleton-content {
    background: linear-gradient(
      90deg,
      #f0f0f0 25%,
      #e8e8e8 50%,
      #f0f0f0 75%
    );
    background-size: 200% 100%;
    border-radius: 4px;
    animation: skeleton-loading 1.5s ease-in-out infinite;
  }
}

@keyframes skeleton-loading {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}
</style>
