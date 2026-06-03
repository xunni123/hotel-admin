<template>
  <div class="home-echarts-container">
    <div class="home-echarts-header">
      <h2>营收趋势图</h2>
    </div>
    <div class="home-echarts-content">
      <Echarts :option="revenueOption" :height="'300px'" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { fetchRevenue } from '@/api/dashboard'
import { useBarAndLineGroup } from '@/composables/echarts/useBarAndLineGroup'

const { chartOption: revenueOption, setData } = useBarAndLineGroup({
  title: '近7天营收趋势',
  tooltip: { trigger: 'axis' },
  legend: { data: ['每日总营收（元）', 'RevPAR（元）'], bottom: 0 },
})

// getData
onMounted(async () => {
  try {
    const res = await fetchRevenue()
    if (res.data && res.data.length > 0) {
      setData(res.data[0])
    }
  } catch {
    // chart empty
    
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
