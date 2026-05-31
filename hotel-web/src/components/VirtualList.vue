<template>
  <div
    class="virtual-list"
    ref="containerRef"
    @scroll="handleScroll"
    :style="{ height: height }"
  >
    <div class="scroll-area" :style="{ height: totalHeight + 'px' }">
      <div
        v-for="item in visibleItems"
        :key="item[keyField]"
        class="list-item"
        :style="{ top: getItemTop(item) + 'px' }"
      >
        <slot :item="item" :index="getItemIndex(item)"></slot>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

interface VirtualItem {
  [key: string]: unknown
}

const props = defineProps<{
  list: VirtualItem[]
  itemHeight: number
  keyField: string
  height?: string
}>()

const containerRef = ref<HTMLElement | null>(null)
const scrollTop = ref(0)

const totalHeight = computed(() => props.list.length * props.itemHeight)

const startIndex = computed(() => {
  return Math.floor(scrollTop.value / props.itemHeight)
})

const endIndex = computed(() => {
  const containerHeight = containerRef.value?.clientHeight || 500
  return Math.min(
    startIndex.value + Math.ceil(containerHeight / props.itemHeight) + 1,
    props.list.length,
  )
})

const visibleItems = computed(() => {
  return props.list.slice(startIndex.value, endIndex.value)
})

const getItemTop = (item: VirtualItem) => {
  const index = props.list.findIndex(
    (i) => i[props.keyField] === item[props.keyField],
  )
  return index * props.itemHeight
}

const getItemIndex = (item: VirtualItem) => {
  return props.list.findIndex((i) => i[props.keyField] === item[props.keyField])
}

const handleScroll = (e: Event) => {
  const target = e.target as HTMLElement
  scrollTop.value = target.scrollTop
}
</script>

<style scoped>
.virtual-list {
  overflow-y: auto;
  position: relative;
  border: 1px solid #ebeef5;
  border-radius: 8px;
}

.scroll-area {
  position: relative;
}

.list-item {
  position: absolute;
  left: 0;
  right: 0;
  width: 100%;
}
</style>
