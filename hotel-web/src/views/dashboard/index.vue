<template>
  <div class="home-header">
    <!-- 欢迎 -->
    <Welcome />
  </div>

  <!-- 卡片 -->
  <div class="home-card">
    <HomeCard />
  </div>

  <!-- echart图 -->
  <div class="home-charts">
    <LineEcharts />
    <PieChart />
    <RevenueChart />
  </div>

  <!-- 待办事项 -->
  <!-- <div class="home-schedule">
    <Schedule />
  </div> -->
</template>

<script setup>
import Welcome from './components/Welcome.vue'
import LineEcharts from './components/LineEcharts.vue'
import HomeCard from './components/HomeCard.vue'
import PieChart from './components/PieChart.vue'
import RevenueChart from './components/RevenueChart.vue'
import Schedule from './components/Schedule.vue'
</script>

<style lang="scss" scoped>
.home-header {
  padding: 10px;
  margin: 0 auto;
}
.home-card {
  padding: 10px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: nowrap;
  gap: 20px;
  cursor: pointer;
  overflow-x: auto;

  &::-webkit-scrollbar {
    display: none;
  }

  scrollbar-width: none;
  -ms-overflow-style: none;
}

.home-charts {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-template-rows: auto;
  gap: 20px;
  align-items: start;
  margin-top: 40px;

  // 让第三个卡片占据整行
  > div:nth-child(3) {
    grid-column: 1 / -1;
  }
}

.home-schedule {
  width: 100%;
}
</style>
