<template>
  <div class="member-detail">
    <div class="detail-header">
      <div class="member-info">
        <span class="member-no">会员编号：{{ memberNo }}</span>
        <span class="member-name">姓名：{{ name }}</span>
      </div>
      <span :class="['status-tag', statusClass]">{{ statusText }}</span>
    </div>

    <div class="detail-section">
      <h3 class="section-title">基本信息</h3>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">手机号</span>
          <span class="info-value">{{ phone }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">邮箱</span>
          <span class="info-value">{{ email }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">会员等级</span>
          <span :class="['info-value', levelClass]">{{ levelText }}</span>
        </div>
      </div>
    </div>

    <div class="detail-section">
      <h3 class="section-title">积分信息</h3>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">当前积分</span>
          <span class="info-value points">{{ points }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">累计消费</span>
          <span class="info-value amount">¥{{ totalConsumption }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">最后消费日期</span>
          <span class="info-value">{{ lastConsumptionDate || '暂无' }}</span>
        </div>
      </div>
    </div>

    <div class="detail-section">
      <h3 class="section-title">注册信息</h3>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">注册日期</span>
          <span class="info-value">{{ registerDate }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  selectedMember: any
}>()

const memberNo = computed(() => props.selectedMember?.memberNo || '')
const name = computed(() => props.selectedMember?.name || '')
const phone = computed(() => props.selectedMember?.phone || '')
const email = computed(() => props.selectedMember?.email || '')
const level = computed(() => props.selectedMember?.level || '')
const points = computed(() => props.selectedMember?.points || 0)
const registerDate = computed(() => props.selectedMember?.registerDate || '')
const status = computed(() => props.selectedMember?.status || '')
const totalConsumption = computed(() => props.selectedMember?.totalConsumption || 0)
const lastConsumptionDate = computed(() => props.selectedMember?.lastConsumptionDate || '')

const statusText = computed(() => {
  const statusMap: Record<string, string> = {
    active: '正常',
    inactive: '停用',
    expired: '过期',
  }
  return statusMap[status.value] || status.value
})

const statusClass = computed(() => {
  const classMap: Record<string, string> = {
    active: 'status-active',
    inactive: 'status-inactive',
    expired: 'status-expired',
  }
  return classMap[status.value] || ''
})

const levelText = computed(() => {
  const levelMap: Record<string, string> = {
    vip: 'VIP会员',
    gold: '黄金会员',
    silver: '白银会员',
    bronze: '青铜会员',
    diamond: '钻石会员',
  }
  return levelMap[level.value] || level.value
})

const levelClass = computed(() => {
  const classMap: Record<string, string> = {
    vip: 'level-vip',
    gold: 'level-gold',
    silver: 'level-silver',
    bronze: 'level-bronze',
    diamond: 'level-diamond',
  }
  return classMap[level.value] || ''
})
</script>

<style scoped lang="scss">
.member-detail {
  padding: 20px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;

  .member-info {
    display: flex;
    gap: 24px;

    .member-no {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }

    .member-name {
      font-size: 16px;
      color: #606266;
    }
  }

  .status-tag {
    padding: 4px 12px;
    border-radius: 4px;
    font-size: 14px;
    font-weight: 500;

    &.status-active {
      background-color: #f6ffed;
      color: #52c41a;
    }

    &.status-inactive {
      background-color: #fff7e6;
      color: #d48806;
    }

    &.status-expired {
      background-color: #fff2f0;
      color: #ff4d4f;
    }
  }
}

.detail-section {
  margin-bottom: 20px;

  .section-title {
    font-size: 14px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 12px;
    padding-left: 8px;
    border-left: 4px solid #1890ff;
  }

  .info-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 16px;
  }

  .info-item {
    display: flex;
    flex-direction: column;
    padding: 12px;
    background-color: #fafafa;
    border-radius: 8px;

    .info-label {
      font-size: 12px;
      color: #909399;
      margin-bottom: 4px;
    }

    .info-value {
      font-size: 14px;
      color: #303133;

      &.points {
        font-size: 18px;
        font-weight: 600;
        color: #faad14;
      }

      &.amount {
        font-size: 18px;
        font-weight: 600;
        color: #ff4d4f;
      }

      &.level-vip {
        color: #faad14;
      }

      &.level-gold {
        color: #d4a373;
      }

      &.level-silver {
        color: #909399;
      }

      &.level-bronze {
        color: #cd7f32;
      }

      &.level-diamond {
        color: #b9f2ff;
      }
    }
  }
}
</style>