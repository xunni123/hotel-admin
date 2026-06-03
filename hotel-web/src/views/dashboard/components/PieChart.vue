<template>
  <div class="home-echarts-container">
    <div class="home-echarts-header">
      <h2>客源分布图</h2>
    </div>
    <div class="home-echarts-content">
      <Echarts :option="regionOption" :height="'300px'" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { fetchRegionData } from '@/api/dashboard'
import { usePieCharts } from '@/composables/echarts/usePieChart'
import { MessagePrompt } from '@/utils/message'
import { onMounted } from 'vue'

const { chartOption: regionOption, setData } = usePieCharts({
  title: '客源分布图',
  valueKey: 'orderCount',
  nameKey: 'region',
  colors: [
    '#ee6666',
    '#5470c6',
    '#fac858',
    '#73c0de',
    '#3ba272',
    '#fc8452',
    '#9a60b4',
    '#ea7ccc',
  ],
  pieRadius: ['50%', '70%'],
})

fetchRegionData().then((res) => {
  setData(res.data)
})
onMounted(async () => {
  try {
    const res = await fetchRegionData()
    setData(res.data)
  } catch (err) {
    MessagePrompt(err, 'error')
  }
})
</script>

<style scoped>
.home-echarts-container {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;

  .home-echarts-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h3 {
      margin: 0;
      font-size: 16px;
      font-weight: bold;
      color: #333;
    }
  }

  .home-echarts-content {
    width: 100%;
  }
}
</style>
