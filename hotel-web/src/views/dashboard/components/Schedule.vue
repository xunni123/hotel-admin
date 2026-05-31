<template>
  <div class="schedule">
    <Card :bg-color="'#fff'" :padding="'20px'" :hoverable="false">
      <template #header>
        <div class="card-header">
          <span>今日日程</span>
        </div>
      </template>
      <div class="schedule-content">
        <div
          class="schedule-item"
          @click="gotoRoute(item)"
          v-for="(item, index) in taskList"
          :key="index"
        >
          <div class="schedule-text">{{ item }}</div>
          <el-icon class="schedule-icon"><CaretRight /></el-icon>
        </div>
      </div>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Card from '@/components/Card.vue'
import { useRouter } from 'vue-router'
const taskList = ['打扫任务', '预定申请', '客诉/留言', '维修报修', '发票申请']

const router = useRouter()
const gotoRoute = (route) => {
  switch (route) {
    case '打扫任务':
      route = '/analytics'
      break
  }
  router.push(route)
}
</script>

<style scoped lang="scss">
.schedule {
  width: 100%;
  margin-bottom: 20px;
  :deep(.card) {
    color: #333;

    .card-header {
      font-size: 16px;
      font-weight: bold;
      margin-bottom: 16px;
    }
  }

  .schedule-content {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .schedule-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    cursor: pointer;
    padding: 16px;
    background-color: #f9f9f9;
    border-radius: 8px;
    transition: all 0.3s ease;
    border: 1px solid #eaeaea;

    &:hover {
      background-color: #f0f0f0;
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      border-color: #3858bf;
    }

    .schedule-text {
      flex: 1;
      font-size: 14px;
      color: #333;
      font-weight: 500;
      line-height: 1.4;
    }

    .schedule-icon {
      color: #3858bf;
      font-size: 16px;
      transition: transform 0.3s ease;
    }

    &:hover .schedule-icon {
      transform: translateX(4px);
    }
  }
}
</style>
