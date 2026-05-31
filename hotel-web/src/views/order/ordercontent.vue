<template>
  <div class="order-detail">
    <div class="detail-header">
      <div class="order-info">
        <span class="order-no">订单编号：{{ orderNo }}</span>
        <span class="room-number">房间号：{{ roomNumber }}</span>
      </div>
      <span :class="['status-tag', statusClass]">{{ statusText }}</span>
    </div>

    <div class="detail-section">
      <h3 class="section-title">客人信息</h3>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">姓名</span>
          <span class="info-value">{{ guestName }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">手机号</span>
          <span class="info-value">{{ guestPhone }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">入住人数</span>
          <span class="info-value">{{ guestsCount }}人</span>
        </div>
      </div>
    </div>

    <div class="detail-section">
      <h3 class="section-title">入住信息</h3>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">入住日期</span>
          <span class="info-value">{{ checkInDate }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">退房日期</span>
          <span class="info-value">{{ checkOutDate }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">入住天数</span>
          <span class="info-value">{{ nights }}晚</span>
        </div>
      </div>
    </div>

    <div class="detail-section">
      <h3 class="section-title">房间信息</h3>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">房型</span>
          <span class="info-value">{{ roomType }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">楼栋</span>
          <span class="info-value">{{ building }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">楼层</span>
          <span class="info-value">{{ floor }}</span>
        </div>
      </div>
    </div>

    <div class="detail-section">
      <h3 class="section-title">支付信息</h3>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">总金额</span>
          <span class="info-value amount">¥{{ totalAmount }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">支付状态</span>
          <span :class="['info-value', paymentStatusClass]">{{ paymentStatusText }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">预订渠道</span>
          <span class="info-value">{{ bookChannel }}</span>
        </div>
      </div>
    </div>

    <div v-if="remarks" class="detail-section">
      <h3 class="section-title">备注</h3>
      <div class="remarks-content">{{ remarks }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  selectedOrder: any
}>()

const orderNo = computed(() => props.selectedOrder?.orderNo || '')
const roomNumber = computed(() => props.selectedOrder?.roomNumber || '')
const guestName = computed(() => props.selectedOrder?.guestName || '')
const guestPhone = computed(() => props.selectedOrder?.guestPhone || '')
const guestsCount = computed(() => props.selectedOrder?.guestsCount || 1)
const checkInDate = computed(() => props.selectedOrder?.checkInDate || '')
const checkOutDate = computed(() => props.selectedOrder?.checkOutDate || '')
const nights = computed(() => props.selectedOrder?.nights || 1)
const roomType = computed(() => props.selectedOrder?.roomType || '')
const building = computed(() => props.selectedOrder?.building || '')
const floor = computed(() => props.selectedOrder?.floor || '')
const totalAmount = computed(() => props.selectedOrder?.totalAmount || 0)
const bookChannel = computed(() => props.selectedOrder?.bookChannel || '')
const remarks = computed(() => props.selectedOrder?.remarks || '')

const orderStatus = computed(() => props.selectedOrder?.orderStatus || '')

const statusText = computed(() => {
  const statusMap: Record<string, string> = {
    pending: '待确认',
    confirmed: '已确认',
    checked_in: '已入住',
    checked_out: '已退房',
    cancelled: '已取消',
  }
  return statusMap[orderStatus.value] || orderStatus.value
})

const statusClass = computed(() => {
  const classMap: Record<string, string> = {
    pending: 'status-pending',
    confirmed: 'status-confirmed',
    checked_in: 'status-checked-in',
    checked_out: 'status-checked-out',
    cancelled: 'status-cancelled',
  }
  return classMap[orderStatus.value] || ''
})

const paymentStatus = computed(() => props.selectedOrder?.paymentStatus || '')

const paymentStatusText = computed(() => {
  const statusMap: Record<string, string> = {
    paid: '已支付',
    unpaid: '未支付',
    partial: '部分支付',
    refunded: '已退款',
  }
  return statusMap[paymentStatus.value] || paymentStatus.value
})

const paymentStatusClass = computed(() => {
  const classMap: Record<string, string> = {
    paid: 'payment-paid',
    unpaid: 'payment-unpaid',
    partial: 'payment-partial',
    refunded: 'payment-refunded',
  }
  return classMap[paymentStatus.value] || ''
})
</script>

<style scoped lang="scss">
.order-detail {
  padding: 20px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;

  .order-info {
    display: flex;
    gap: 24px;

    .order-no {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }

    .room-number {
      font-size: 16px;
      color: #606266;
    }
  }

  .status-tag {
    padding: 4px 12px;
    border-radius: 4px;
    font-size: 14px;
    font-weight: 500;

    &.status-pending {
      background-color: #fff7e6;
      color: #d48806;
    }

    &.status-confirmed {
      background-color: #e6f7ff;
      color: #1890ff;
    }

    &.status-checked-in {
      background-color: #f6ffed;
      color: #52c41a;
    }

    &.status-checked-out {
      background-color: #f5f5f5;
      color: #999;
    }

    &.status-cancelled {
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

      &.amount {
        font-size: 18px;
        font-weight: 600;
        color: #ff4d4f;
      }

      &.payment-paid {
        color: #52c41a;
      }

      &.payment-unpaid {
        color: #d48806;
      }

      &.payment-partial {
        color: #fa8c16;
      }

      &.payment-refunded {
        color: #909399;
      }
    }
  }

  .remarks-content {
    padding: 12px;
    background-color: #fafafa;
    border-radius: 8px;
    font-size: 14px;
    color: #606266;
    line-height: 1.6;
  }
}
</style>