<template>
  <div class="cards-container">
    <template v-if="loading">
      <Card v-for="i in 5" :key="i" class="card-item" :width="'180px'">
        <MySkeleton mode="card" :rows="2" />
      </Card>
    </template>

    <template v-else>
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
  </div>
</template>

<script setup lang="ts">
import Card from '@/components/Card.vue'
import { ref, computed, onMounted } from 'vue'
import MySkeleton from '@/components/MySkeleton.vue'
import type { ColorMap } from '@/types/dashboardCard'
import { useLoading } from '@/composables/useLoading'

import { fetchDashboardData } from '@/api/dashboard'
import { colorMap, cardDataList } from '@/constants/homecard'

const { loading, startLoading, stopLoading } = useLoading(800)

const cards = computed(() => {
  if (!cardData.value || Object.keys(cardData.value).length === 0) return []
  return cardDataList.map((item) => ({
    label: item.label,
    value: cardData.value[item.key] ?? '--',
    unit: item.unit,
    bgColor: colorMap[item.key as keyof ColorMap],
  }))
})

const cardData = ref({})

onMounted(async () => {
  startLoading()
  try {
    const res = await fetchDashboardData()
    cardData.value = res.data
  } catch {
  } finally {
    stopLoading()
  }
})
</script>

<style lang="scss" scoped>
.cards-container {
  display: flex;
  align-items: center;
  gap: 30px;
  justify-content: space-between;
}
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
