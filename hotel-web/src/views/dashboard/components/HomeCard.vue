<template>
  <Card
    v-for="item in cards"
    :key="item"
    class="card-item"
    :bg-color="item.bgColor"
    :width="'180px'"
  >
    <template #header>
      <div class="card-header">
        <span>{{ item.label }}</span>
      </div>
    </template>
    <div class="card-content">
      <p class="text item">{{ item.value }}{{ item.unit }}</p>
    </div>
  </Card>
</template>

<script setup lang="ts">
import Card from '@/components/Card.vue'
import { ref, computed } from 'vue'
import type { Ref } from 'vue'
import type { ColorMap, Stats } from '@/types'
// 卡片
import { fetchDashboardData } from '@/api/dashboard'

// 卡片color
const colorMap: ColorMap = {
  todayCheckIns: '#3B82F6',
  todayCheckOuts: '#10B981',
  emptyRooms: '#F59E0B',
  occupancyRate: '#8B5CF6',
  todayRevenue: '#EF4444',
}

// 卡片data
const cardDataList = [
  { key: 'todayCheckIns', label: '今日入住', unit: '间' },
  { key: 'todayCheckOuts', label: '今日退房', unit: '间' },
  { key: 'emptyRooms', label: '空房数', unit: '间' },
  { key: 'occupancyRate', label: '入住率', unit: '%' },
  { key: 'todayRevenue', label: '今日营收', unit: '元' },
]

// 菜单项
const cards = computed(() => {
  if (!cardData.value || Object.keys(cardData.value).length === 0) return []
  return cardDataList.map((item) => ({
    label: item.label,
    value: cardData.value[item.key as keyof Stats] ?? '--',
    unit: item.unit,
    bgColor: colorMap[item.key as keyof ColorMap],
  }))
})

// 卡片请求数据
const cardData: Ref<Stats> = ref({})
fetchDashboardData().then((res) => {
  cardData.value = res.data
})
</script>

<style lang="scss" scoped>
.card-item {
  flex: 1;
  min-width: 180px;
  transition: transform 0.3s ease;

  &:hover {
    transform: translateY(-2px);
  }

  :deep(.card) {
    height: 120px;
    display: flex;
    flex-direction: column;
    justify-content: center;

    .card-header {
      text-align: center;
      margin-bottom: 10px;
      font-size: 14px;
      opacity: 0.9;
    }

    .card-content {
      text-align: center;

      .text {
        font-size: 24px;
        font-weight: bold;
        margin: 0;
      }
    }
  }
}
</style>
